package com.muhammadelsayed.bybike.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.model.Order;
import com.muhammadelsayed.bybike.activity.network.RetrofitClientInstance;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderTracking extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        RoutingListener {

    private static final String TAG = "OrderTracking";

    // for getting the route
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorPrimary, R.color.error};

    private static LatLng origin, destination;

    private DatabaseReference refDriver;

    private Order currentOrder;
    private LatLng riderLatLng;

    // widgets
    private Button btnCallRider, btnCancelTrip;
    private TextView txtRiderName;
    private ImageView riderProfilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.wtf(TAG, "onCreate() has been instantiated");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent() != null) {
            currentOrder = (Order) getIntent().getSerializableExtra("currentOrder");
            Log.d(TAG, "onCreate: currentOrder = " + currentOrder);
            refDriver = FirebaseDatabase.getInstance().getReference("Drivers");


            refDriver.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String riderLat = String.valueOf(dataSnapshot.child(String.valueOf(currentOrder.getId())).child("l").child(String.valueOf(0)).getValue());
                    String riderLng = String.valueOf(dataSnapshot.child(String.valueOf(currentOrder.getId())).child("l").child(String.valueOf(1)).getValue());

                    Log.wtf(TAG, "DataSnapshot = " + dataSnapshot);
                    Log.wtf(TAG, "LAT = " + riderLat + " ---------- LNG = " + riderLng);

                    if (!riderLat.equals("null") && !riderLng.equals("null")) {

                        riderLatLng = new LatLng(Double.parseDouble(riderLat), Double.parseDouble(riderLng));
                        Toast.makeText(OrderTracking.this, riderLatLng.toString(), Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.wtf(TAG, "onCancelled()");

                }
            });
        }


    }


    @Override
    public void onRoutingFailure(RouteException e) {
        Log.wtf(TAG, "onRoutingFailure() has been instantiated");

    }

    @Override
    public void onRoutingStart() {
        Log.wtf(TAG, "onRoutingStart() has been instantiated");

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
        Log.wtf(TAG, "onRoutingSuccess() has been instantiated");

    }

    @Override
    public void onRoutingCancelled() {
        Log.wtf(TAG, "onCreate() has been instantiated");

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.wtf(TAG, "onConnected() has been instantiated");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.wtf(TAG, "onConnectionSuspended() has been instantiated");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.wtf(TAG, "onConnectionFailed() has been instantiated");

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.wtf(TAG, "onLocationChanged() has been instantiated");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.wtf(TAG, "onCronMapReadyeate() has been instantiated");

    }
}
