package com.muhammadelsayed.bybike.activity;

import android.app.Activity;
import android.content.Context;
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
import com.muhammadelsayed.bybike.activity.utils.Utils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    public static UserModel currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf(TAG, "onCreate() has been instantiated");

        checkUserSession(SplashActivity.this);


    }


    /**
     * checks users session
     *
     * @param context activity context
     */
    private void checkUserSession(final Context context) {
        Log.wtf(TAG, "checkUserSession() has been instantiated");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String token = prefs.getString("USER_TOKEN", "");

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

                        context.startActivity(new Intent(context, MainActivity.class));
                        ((Activity) context).finish();
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {

                    context.startActivity(new Intent(context, StartActivity.class));
                    ((Activity) context).finish();
                }
            });
        } else {
            context.startActivity(new Intent(context, StartActivity.class));
            ((Activity) context).finish();
        }
    }


}
