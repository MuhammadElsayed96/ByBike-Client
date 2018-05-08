package com.muhammadelsayed.bybike.activity.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.utils.CustomToast;
import com.muhammadelsayed.bybike.activity.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment implements View.OnClickListener{
    private static View view;
    private static EditText mFullName, mEmail, mPhoneNumber,
            mPassword, mConfirmPassword;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    private static FragmentManager fragmentManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_layout, container, false);
        initViews();
        setListeners();
        return view;
    }

    private void initViews() {
        mFullName = view.findViewById(R.id.singup_fullname);
        mEmail = view.findViewById(R.id.signup_email);
        mPhoneNumber = view.findViewById(R.id.signup_phone_number);
        mPassword = view.findViewById(R.id.signup_password);
        mConfirmPassword = view.findViewById(R.id.confirmPassword);
        signUpButton = view.findViewById(R.id.signUpBtn);
        login = view.findViewById(R.id.alreadyUser);
        terms_conditions = view.findViewById(R.id.terms_conditions);
        fragmentManager = getActivity().getSupportFragmentManager();
    }


    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                if (checkValidation()) {
                    ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();


                }
                break;

            case R.id.alreadyUser:

                // Replace login fragment
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                        .replace(R.id.frameContainer,
                                new LoginFragment(),
                                Utils.LoginFragment).commit();
                break;
        }
    }

    // Check Validation Method
    private boolean checkValidation() {

        boolean isValid = true;

        // Get all edittext texts
        String fullName = mFullName.getText().toString();
        String email = mEmail.getText().toString();
        String phoneNumber = mPhoneNumber.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(email);

        // Check if all strings are null or not
        if (fullName.length() == 0
                || email.length() == 0
                || phoneNumber.length() == 0
                || password.length() == 0
                || confirmPassword.length() == 0) {

            isValid = false;
            new CustomToast().showToast(getActivity(), view,
                    "All fields are required.");
        }

        // Check if email id valid or not
        else if (!m.find()) {

            isValid = false;
            new CustomToast().showToast(getActivity(), view,
                    "Your Email Id is Invalid.");
        }
            // Check if both password should be equal
        else if (!confirmPassword.equals(password)) {

            isValid = false;

            new CustomToast().showToast(getActivity(), view,
                    "Both password doesn't match.");
        }
            // Make sure user should check Terms and Conditions checkbox
        else if (!terms_conditions.isChecked()) {

            isValid = false;

            new CustomToast().showToast(getActivity(), view,
                    "Please select Terms and Conditions.");
        }

        return isValid;
    }
}
