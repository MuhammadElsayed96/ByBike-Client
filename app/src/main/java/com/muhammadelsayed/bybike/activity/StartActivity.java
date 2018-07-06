package com.muhammadelsayed.bybike.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.fragment.LoginFragment;
import com.muhammadelsayed.bybike.activity.utils.Utils;

public class StartActivity extends AppCompatActivity {


    private static FragmentManager fragmentManager;
    private Context mContext = StartActivity.this;

    private static final String TAG = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.wtf(TAG, "onCreate() has been instantiated");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        fragmentManager = getSupportFragmentManager();

        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new LoginFragment(),
                            Utils.LoginFragment).commit();
        }

    }


}