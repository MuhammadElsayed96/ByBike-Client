package com.muhammadelsayed.bybike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.muhammadelsayed.bybike.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Start home activity

        startActivity(new Intent(SplashActivity.this, MainActivity.class));

        // close splash activity

        finish();
    }
}
