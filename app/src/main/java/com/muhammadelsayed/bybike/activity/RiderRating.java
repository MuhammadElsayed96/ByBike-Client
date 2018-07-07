package com.muhammadelsayed.bybike.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.ProfileActivities.EditLastnameActivity;
import com.muhammadelsayed.bybike.activity.ProfileActivities.ProfileActivity;
import com.muhammadelsayed.bybike.activity.model.RateModel;
import com.muhammadelsayed.bybike.activity.model.RetroResponse;
import com.muhammadelsayed.bybike.activity.model.UserModel;
import com.muhammadelsayed.bybike.activity.network.OrderClient;
import com.muhammadelsayed.bybike.activity.network.RetrofitClientInstance;
import com.muhammadelsayed.bybike.activity.network.UserClient;
import com.muhammadelsayed.bybike.activity.utils.Utils;
import com.squareup.picasso.Picasso;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.muhammadelsayed.bybike.activity.OrderTracking.orderInfo;
import static com.muhammadelsayed.bybike.activity.SplashActivity.currentUser;

public class RiderRating extends AppCompatActivity {

    private static final String TAG = "RiderRating";

    private TextView mRiderName;
    private ImageView mRiderPhoto;
    private MaterialRatingBar mRiderRate;
    private Button btnRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_rating);

        mRiderName = findViewById(R.id.rider_name);
        String riderName = getIntent().getStringExtra("riderName");
        mRiderName.setText(riderName);

        mRiderPhoto = findViewById(R.id.rider_photo);
        String riderPhoto = getIntent().getStringExtra("riderPhoto");

        Picasso.get()
                .load(RetrofitClientInstance.BASE_URL + riderPhoto)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(mRiderPhoto);

        mRiderRate = findViewById(R.id.ratingBar);
        mRiderRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(RiderRating.this, "RATE = " + rating, Toast.LENGTH_SHORT).show();
            }
        });


        btnRate = findViewById(R.id.trip_rate_btn);

        Log.d(TAG, "orderInfo = " + orderInfo);

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderClient service = RetrofitClientInstance.getRetrofitInstance()
                        .create(OrderClient.class);

                RateModel rate = new RateModel(currentUser.getToken(),
                        orderInfo.getTransporter().getUuid(),
                        orderInfo.getOrder().getUuid(),
                        (int) mRiderRate.getRating());

                Log.d(TAG, "currentUser = " + currentUser);
                Log.d(TAG, "riderUuid = " + orderInfo.getTransporter().getUuid());
                Log.d(TAG, "orderUuid = " + orderInfo.getOrder().getUuid());

                Log.d(TAG, "RATE = " + mRiderRate.getRating());

                Call<RetroResponse> call = service.rateRider(rate);

                call.enqueue(new Callback<RetroResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RetroResponse> call, Response<RetroResponse> response) {

                        if (response.body() != null) {

                            Log.d(TAG, "onResponse: " + response.body().getMessage());
                            Intent intent = new Intent(RiderRating.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), "I have no idea what's happening\nbut, something is terribly wrong !!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RetroResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "logging out !!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RiderRating.this, StartActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });


    }
}
