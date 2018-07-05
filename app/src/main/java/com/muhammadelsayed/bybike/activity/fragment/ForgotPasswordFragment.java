package com.muhammadelsayed.bybike.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.utils.CustomToast;
import com.muhammadelsayed.bybike.activity.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {
    private View view;

    private EditText mEmail;
    private TextView submit, back;
    private static FragmentManager fragmentManager;

    private static final String TAG = "ForgotPasswordFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.wtf(TAG, "onCreateView() has been instantiated");

        view = inflater.inflate(R.layout.forgotpassword_layout, container,false);
        initViews();
        setListeners();

        return view;
    }

    // Initialize the views
    private void initViews() {
        Log.wtf(TAG, "initViews() has been instantiated");

        mEmail = view.findViewById(R.id.registered_emailid);
        submit = view.findViewById(R.id.forgot_button);
        back = view.findViewById(R.id.backToLoginBtn);

        fragmentManager = getActivity().getSupportFragmentManager();

    }

    // Set Listeners over buttons
    private void setListeners() {
        Log.wtf(TAG, "setListeners() has been instantiated");

        back.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.wtf(TAG, "onClick() has been instantiated");

        switch (v.getId()) {
            case R.id.backToLoginBtn:

                // Replace Login Fragment on Back Presses
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                        .replace(R.id.frameContainer,
                                new LoginFragment(),
                                Utils.LoginFragment).commit();
                break;

            case R.id.forgot_button:

                // Call Submit button task
                submitButtonTask();
                break;

        }

    }

    private void submitButtonTask() {
        Log.wtf(TAG, "submitButtonTask() has been instantiated");

        String email = mEmail.getText().toString();

        // Pattern for email id validation
        Pattern p = Pattern.compile(Utils.regEx);

        // Match the pattern
        Matcher m = p.matcher(email);

        // First check if email id is not null else show error toast
        if (email.equals("") || email.length() == 0)

            new CustomToast().showToast(getActivity(), view,
                    "Please enter your Email Id.");

            // Check if email id is valid or not
        else if (!m.find())
            new CustomToast().showToast(getActivity(), view,
                    "Your Email Id is Invalid.");

            // Else submit email id and fetch password or do your stuff
        else
            Toast.makeText(getActivity(), "Get Forgot Password.",
                    Toast.LENGTH_SHORT).show();
    }


}
