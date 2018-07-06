package com.muhammadelsayed.bybike.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.ProfileActivities.ProfileActivity;
import com.muhammadelsayed.bybike.activity.model.Order;
import com.muhammadelsayed.bybike.activity.model.RetroResponse;
import com.muhammadelsayed.bybike.activity.model.UserModel;
import com.muhammadelsayed.bybike.activity.network.OrderClient;
import com.muhammadelsayed.bybike.activity.network.RetrofitClientInstance;
import com.muhammadelsayed.bybike.activity.network.UserClient;
import com.muhammadelsayed.bybike.activity.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.muhammadelsayed.bybike.activity.MainActivity.destination;
import static com.muhammadelsayed.bybike.activity.MainActivity.origin;
import static com.muhammadelsayed.bybike.activity.SplashActivity.currentUser;

public class ConfirmOrderActivity extends AppCompatActivity {


    private static final String TAG = "ConfirmOrderActivity";

    // vars
    private Context mContext;

    // widgets
    private Toolbar toolbar;
    private TextView placeFrom, placeTo, totalCost;
    private EditText notes;
    private Spinner spinnerPayment;
    private Button confirmOrder;

    public static Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.wtf(TAG, "onCreate() has been instantiated");
        super.onCreate(savedInstanceState);
        Utils.checkUserSession(ConfirmOrderActivity.this);
        setContentView(R.layout.activity_confirm_order);
        Log.d(TAG, "onCreate: started !!");

        currentOrder = null;
        setupWidgets();

    }


    /******************** LAYOUT ********************/

    /**
     * sets up all the widgets in this library
     */
    private void setupWidgets() {
        Log.wtf(TAG, "setupWidgets() has been instantiated");

        mContext = getApplicationContext();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        placeFrom = findViewById(R.id.placeFrom);
        placeTo = findViewById(R.id.placeTo);
        totalCost = findViewById(R.id.totalCost);
        notes = findViewById(R.id.notes);
        spinnerPayment = findViewById(R.id.spinnerPayment);
        confirmOrder = findViewById(R.id.confirmOrder);

        if (getIntent().hasExtra("placeFrom") && getIntent().hasExtra("placeTo") && getIntent().hasExtra("totalCost")) {

            placeFrom.setText(getIntent().getStringExtra("placeFrom"));
            placeTo.setText(getIntent().getStringExtra("placeTo"));
            totalCost.setText(""+getIntent().getIntExtra("totalCost", 0));

        }

        String[] payments = {"Cash", "Fawry"};

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, payments);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerPayment.setAdapter(spinnerArrayAdapter);

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderClient service = RetrofitClientInstance.getRetrofitInstance()
                        .create(OrderClient.class);

                currentOrder = new Order();

                currentOrder.setSenderLatLng(origin);
                currentOrder.setReceiverLatLng(destination);
                currentOrder.setApi_token(currentUser.getToken());
                Call<RetroResponse> call = service.createOrder(currentOrder);

                call.enqueue(new Callback<RetroResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RetroResponse> call, Response<RetroResponse> response) {

                        if (response.body() != null) {

                            currentOrder = response.body().getOrder();
                            Log.d(TAG, "onResponse: " + currentOrder);
                            Toast.makeText(ConfirmOrderActivity.this, "Succeeded", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(mContext, WaitingActivity.class);
                            startActivity(intent);

                        } else {

                            Toast.makeText(getApplicationContext(), "I have no idea what's happening\nbut, something is terribly wrong !!", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<RetroResponse> call, Throwable t) {

                        Log.e(TAG, "onFailure: " + t.getMessage() + " ----\n----" + t.getCause());
                        Log.d(TAG, "onFailure: Order = " + currentOrder);
                        Toast.makeText(getApplicationContext(), "logging out !!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(mContext, StartActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

            }

        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.wtf(TAG, "onOptionsItemSelected() has been instantiated");

        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
