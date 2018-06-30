package com.muhammadelsayed.bybike.activity;

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
import static com.muhammadelsayed.bybike.activity.fragment.LoginFragment.currentUser;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Log.d(TAG, "onCreate: started !!");

        setupWidgets();

    }


    /******************** LAYOUT ********************/

    /**
     * sets up all the widgets in this library
     */
    private void setupWidgets() {

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
            totalCost.setText(getIntent().getStringExtra("totalCost"));

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

                final Order order = new Order();

                order.setSenderLatLng(origin);
                order.setReceiverLatLng(destination);
                order.setApi_token(currentUser.getToken());
                Call<RetroResponse> call = service.createOrder(order);

                call.enqueue(new Callback<RetroResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RetroResponse> call, Response<RetroResponse> response) {

                        if (response.body() != null) {

                            Log.d(TAG, "onResponse: " + response.body());
                            Toast.makeText(ConfirmOrderActivity.this, "Succeeded", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {

                            Toast.makeText(getApplicationContext(), "I have no idea what's happening\nbut, something is terribly wrong !!", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<RetroResponse> call, Throwable t) {

                        Log.e(TAG, "onFailure: " + t.getMessage() + " ----\n----" + t.getCause());
                        Log.d(TAG, "onFailure: Order = " + order);
                        Toast.makeText(getApplicationContext(), "network error !!", Toast.LENGTH_SHORT).show();
                    }
                });


                finish();
            }

        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
