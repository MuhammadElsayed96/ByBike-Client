package com.muhammadelsayed.bybike.activity.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.muhammadelsayed.bybike.activity.MainActivity;
import com.muhammadelsayed.bybike.activity.StartActivity;
import com.muhammadelsayed.bybike.activity.model.User;
import com.muhammadelsayed.bybike.activity.model.UserModel;
import com.muhammadelsayed.bybike.activity.network.RetrofitClientInstance;
import com.muhammadelsayed.bybike.activity.network.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.muhammadelsayed.bybike.activity.SplashActivity.currentUser;

public class Utils {

    private static final String TAG = "Utils";

    // Email Validation pattern
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    // Fragments Tags
    public static final String LoginFragment = "LoginFragment";
    public static final String SignUpFragment = "SingUpFragment";
    public static final String ForgotPasswordFragment = "ForgotPasswordFragment";


    /**
     * This method split any name into 2 names, first and last name
     *
     * @param name is the name that will be split
     * @return String array holding first and last name;
     */
    public static String[] splitName(String name) {
        String[] names = name.split(" ", 2);
        return names;
    }

    /**
     * This method concatenates first and last name into full name
     *
     * @param firstname Last Name
     * @param lastname First Name
     * @return Full Name
     */
    public static String concatNames(String firstname, String lastname) {
        return firstname + " " + lastname;
    }


    public static void checkUserSession(final Context context) {
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
                        Log.d(TAG, "onResponse: " + response.body());
                    }
                }
                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {

                    Intent intent = new Intent(context, StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });
        } else {
            Intent intent = new Intent(context, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            ((Activity)context).finish();
        }
    }

}
