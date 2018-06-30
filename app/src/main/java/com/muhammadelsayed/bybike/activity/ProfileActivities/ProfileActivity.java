package com.muhammadelsayed.bybike.activity.ProfileActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.fragment.LoginFragment;
import com.muhammadelsayed.bybike.activity.model.UserModel;

import static com.muhammadelsayed.bybike.activity.fragment.LoginFragment.currentUser;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private LinearLayout mLlEditLastname, mLlEditFirstname, mLlEditPhone, mLlEditEmail, mLlEditPassword;
    private TextView mFirstname, mLastname, mPhone, mEmail, mPassword;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // initializing activity widgets
        setupWidgets();

    }

    /**
     * sets up all activity widgets
     */
    private void setupWidgets() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLlEditFirstname = findViewById(R.id.firstname_layout);
        mLlEditLastname = findViewById(R.id.lastname_layout);
        mLlEditPassword = findViewById(R.id.password_layout);
        mLlEditPhone = findViewById(R.id.phone_layout);
        mLlEditEmail = findViewById(R.id.email_layout);
        mLlEditFirstname.setOnClickListener(mOnLlEditFirstnameClickListener);
        mLlEditLastname.setOnClickListener(mOnLlEditLastnameClickListener);
        mLlEditPassword.setOnClickListener(mOnLlEditPasswordClickListener);
        mLlEditPhone.setOnClickListener(mOnLlEditPhoneClickListener);
//        mLlEditEmail.setOnClickListener(mOnLlEditEmailClickListener);


        // filling profile TextViews with current user's info
        mFirstname = findViewById(R.id.firstname_info);
        mFirstname.setText(currentUser.getUser().getFirstname());

        mLastname = findViewById(R.id.lastname_info);
        mLastname.setText(currentUser.getUser().getLastname());

        mPhone = findViewById(R.id.phone_info);
        mPhone.setText(currentUser.getUser().getPhone());

        mEmail = findViewById(R.id.email_info);
        mEmail.setText(currentUser.getUser().getEmail());

        mPassword = findViewById(R.id.password_info);
        mPassword.setText("********");

    }

    // OnClick Listeners
    private LinearLayout.OnClickListener mOnLlEditFirstnameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent firstnameIntent = new Intent(getApplicationContext(), EditFirstnameActivity.class);
            firstnameIntent.putExtra("firstname", mFirstname.getText());
            firstnameIntent.putExtra("current_user", currentUser);
            startActivity(firstnameIntent);
        }
    };
    private LinearLayout.OnClickListener mOnLlEditLastnameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent lastnameIntent = new Intent(getApplicationContext(), EditLastnameActivity.class);
            lastnameIntent.putExtra("lastname", mLastname.getText());
            startActivity(lastnameIntent);
        }
    };
    private LinearLayout.OnClickListener mOnLlEditPhoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent phoneIntent = new Intent(getApplicationContext(), EditPhoneActivity.class);
            phoneIntent.putExtra("phone", mPhone.getText());
            startActivity(phoneIntent);
        }
    };
    private LinearLayout.OnClickListener mOnLlEditEmailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent emailIntent = new Intent(getApplicationContext(), EditEmailActivity.class);
            emailIntent.putExtra("email", mEmail.getText());
            startActivity(emailIntent);
        }
    };
    private LinearLayout.OnClickListener mOnLlEditPasswordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), EditPasswordActivity.class));
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}
