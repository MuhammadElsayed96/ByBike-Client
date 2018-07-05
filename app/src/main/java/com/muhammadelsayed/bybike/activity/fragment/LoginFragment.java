package com.muhammadelsayed.bybike.activity.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.MainActivity;
import com.muhammadelsayed.bybike.activity.model.User;
import com.muhammadelsayed.bybike.activity.model.UserModel;
import com.muhammadelsayed.bybike.activity.network.RetrofitClientInstance;
import com.muhammadelsayed.bybike.activity.network.UserClient;
import com.muhammadelsayed.bybike.activity.utils.CustomToast;
import com.muhammadelsayed.bybike.activity.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LoginFragment";

    public static UserModel currentUser;

    private View view;
    private EditText mEmail, mPassword;
    private Button loginButton;
    private TextView forgotPassword, signUp;
    private CheckBox show_hide_password;
    private LinearLayout loginLayout;
    private Animation shakeAnimation;
    private FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.wtf(TAG, "onCreateView() has been instantiated");
        view = inflater.inflate(R.layout.login_layout, container, false);
        initViews();
        setListeners();

        return view;
    }

    /**
     * initializes the view
     */
    private void initViews() {

        Log.wtf(TAG, "initViews() has been instantiated");
        fragmentManager = getActivity().getSupportFragmentManager();

        // widgets
        mEmail = view.findViewById(R.id.login_email);
        mPassword = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.loginBtn);
        forgotPassword = view.findViewById(R.id.forgot_password);
        signUp = view.findViewById(R.id.createAccount);
        loginLayout = view.findViewById(R.id.login_layout);
        show_hide_password = view.findViewById(R.id.show_hide_password);

        // load shake animation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

    }

    /**
     * Sets listeners for corresponding widgets
     */
    private void setListeners() {

        Log.wtf(TAG, "setListeners() has been instantiated");

        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);

        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    show_hide_password.setText(R.string.hide_pwd);


                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    mPassword.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());// show password
                } else {
                    show_hide_password.setText(R.string.show_pwd);

                    mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPassword.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password

                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        Log.wtf(TAG, "onClick() has been instantiated");

        switch (v.getId()) {
            case R.id.loginBtn:
                Log.d(TAG, "onClick: validating input...");

                if (checkValidation()) {
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();

                    final String email = mEmail.getText().toString();
                    final String password = mPassword.getText().toString();

                    currentUser = new UserModel();
                    currentUser.setUser(new User(email, password));

                    UserClient service = RetrofitClientInstance.getRetrofitInstance()
                            .create(UserClient.class);

                    Call<UserModel> call = service.loginUser(currentUser.getUser());

                    
                    call.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(@NonNull Call<UserModel> call, Response<UserModel> response) {

                            if (response.body() != null) {

                                currentUser = response.body();
                                currentUser.getUser().setApi_token(response.body().getToken());
                                Log.d(TAG, "onResponse: " + response.body());
                                Intent intent = new Intent(getActivity(), MainActivity.class);
//                                intent.putExtra("current_user", currentUser);
                                startActivity(intent);

                                SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                                prefEditor.putString("USER_TOKEN", response.body().getToken());
                                prefEditor.apply();

                                getActivity().finish();

                            } else {

                                Toast.makeText(getActivity(), "I have no idea what's happening\nbut, something is terribly wrong !!", Toast.LENGTH_SHORT).show();

                            }

                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {

                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "network error !!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                break;

            case R.id.forgot_password:
                Log.d(TAG, "onClick: navigating to ForgotPasswordFragment...");

                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new ForgotPasswordFragment(), Utils.ForgotPasswordFragment)
                        .commit();

                break;

            case R.id.createAccount:
                Log.d(TAG, "onClick: navigating to SignUpFragment...");

                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new SignUpFragment(), Utils.SignUpFragment)
                        .commit();

                break;
        }
    }

    /**
     * Validates user input before logging him in
     *
     * @return false if input is not valid, true if valid
     */
    private boolean checkValidation() {
        Log.wtf(TAG, "checkValidation() has been instantiated");

        boolean isValid = true;
        Log.d(TAG, "checkValidation: validating user input...");
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        // checking email pattern
        Pattern pattern = Pattern.compile(Utils.regEx);
        Matcher matcher = pattern.matcher(email);

        // Check for both field is empty or not
        if (email.equals("") || email.length() == 0
                || password.equals("") || password.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);

            isValid = false;

            new CustomToast().showToast(getActivity(), view,
                    "Enter both credentials.");
        }

        // Check if email id is valid or not
        else if (!matcher.find()) {

            isValid = false;

            new CustomToast().showToast(getActivity(), view,
                    "Your Email Id is Invalid.");
        }

        return isValid;
    }
}
