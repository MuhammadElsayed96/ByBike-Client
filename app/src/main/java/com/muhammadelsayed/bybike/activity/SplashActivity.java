package com.muhammadelsayed.bybike.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.model.User;
import com.muhammadelsayed.bybike.activity.model.UserModel;
import com.muhammadelsayed.bybike.activity.network.RetrofitClientInstance;
import com.muhammadelsayed.bybike.activity.network.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.muhammadelsayed.bybike.activity.fragment.LoginFragment.currentUser;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String token = prefs.getString("USER_TOKEN", "");

        Log.d(TAG, "onCreate: TOKEN = " + token);
        Log.d(TAG, "onCreate: Current User Before = " + currentUser);

        checkUserSession(token);

        Log.d(TAG, "onCreate: Current User After = " + currentUser);

    }


    private void checkUserSession(String token) {

        if (!token.equals("")) {

            User user = new User();
            user.setApi_token(token);

            UserClient service = RetrofitClientInstance.getRetrofitInstance()
                    .create(UserClient.class);
            Call<UserModel> call = service.getUserInfo(user);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(@NonNull Call<UserModel> call, Response<UserModel> response) {

                    if (response.body() != null) {
                        currentUser = response.body();
                        currentUser.setToken(response.body().getUser().getApi_token());
                        Log.d(TAG, "onResponse: " + currentUser);

                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();

                    }
                }


                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {

                    startActivity(new Intent(SplashActivity.this, StartActivity.class));
                    finish();

                }
            });
        } else {

            startActivity(new Intent(SplashActivity.this, StartActivity.class));
            finish();
        }
    }
}
