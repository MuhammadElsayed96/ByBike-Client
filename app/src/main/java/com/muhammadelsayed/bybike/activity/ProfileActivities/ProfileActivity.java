package com.muhammadelsayed.bybike.activity.ProfileActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.muhammadelsayed.bybike.R;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private LinearLayout mLlEditLastname, mLlEditFirstname, mLlEditPhone, mLlEditEmail, mLlEditPassword;

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
        mLlEditEmail.setOnClickListener(mOnLlEditEmailClickListener);

    }

    // OnClick Listeners
    private LinearLayout.OnClickListener mOnLlEditFirstnameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), EditFirstnameActivity.class));
        }
    };
    private LinearLayout.OnClickListener mOnLlEditLastnameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), EditLastnameActivity.class));
        }
    };
    private LinearLayout.OnClickListener mOnLlEditPhoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), EditPhoneActivity.class));
        }
    };
    private LinearLayout.OnClickListener mOnLlEditEmailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), EditEmailActivity.class));
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
