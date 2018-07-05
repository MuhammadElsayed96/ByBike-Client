package com.muhammadelsayed.bybike.activity;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.model.LatLng;
import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.model.Order;
import com.muhammadelsayed.bybike.activity.model.RetroResponse;
import com.muhammadelsayed.bybike.activity.model.TripModel;
import com.muhammadelsayed.bybike.activity.network.OrderClient;
import com.muhammadelsayed.bybike.activity.network.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.muhammadelsayed.bybike.activity.fragment.LoginFragment.currentUser;
import static com.muhammadelsayed.bybike.activity.ConfirmOrderActivity.currentOrder;

public class WaitingActivity extends AppCompatActivity {

    private static final String TAG = "WaitingActivity";
    private ValueEventListener mStatusChangedListener;


//    private Order currentOrder;

    private Button btnCancelOrder;

    private DatabaseReference refOrders;

    private LatLng riderLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        Log.wtf(TAG, "onCreate() has been instantiated");

        setupProgressBar();


//        if (getIntent() != null) {
//            currentOrder = (Order) getIntent().getSerializableExtra("currentOrder");
//        }

        refOrders = FirebaseDatabase.getInstance().getReference("orders").child(String.valueOf(currentOrder.getId()));

        mStatusChangedListener = refOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long status = (long) dataSnapshot.child("status").getValue();

                Log.wtf(TAG, "STATUS = " + status);

                if (status == 1) {


                    Intent intent = new Intent(WaitingActivity.this, OrderTracking.class);
//                    intent.putExtra("currentOrder", currentOrder);
                    startActivity(intent);
                    refOrders.removeEventListener(mStatusChangedListener);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.wtf(TAG, "onCancelled()");
            }
        });


    }

    private void setupProgressBar() {
        Log.wtf(TAG, "setupProgressBar() has been instantiated");

        ProgressBar progressBar = findViewById(R.id.spin_kit);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
    }

    private void setupWidgets() {
        Log.wtf(TAG, "setupWidgets() has been instantiated");

        btnCancelOrder = findViewById(R.id.btn_cancel_order);

        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderClient service = RetrofitClientInstance.getRetrofitInstance()
                        .create(OrderClient.class);

                TripModel tripModel = new TripModel();
                tripModel.setApi_token(currentUser.getToken());
                tripModel.setOrder(currentOrder.getUuid());


            }
        });
    }
}
