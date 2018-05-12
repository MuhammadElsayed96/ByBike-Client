package com.muhammadelsayed.bybike.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.muhammadelsayed.bybike.R;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final float DEFAULT_ZOOM = 17f;
    private static final LatLngBounds LAT_LNG_BOUNDS =
            new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));

    // REQUEST_CODES
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int PLACE_PICKER_REQUEST = 2;


    // vars
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Context mContext;


    // widgets
    private RelativeLayout searchPlace;
    private TextView searchFrom, searchTo;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initializing activity widgets
        setupWidgets();

        // initializing navigation view
        setUpNavigationView();

        getLocationPermission();

        getDeviceLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupWidgets();
    }


    /******************** Map ********************/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing the map...");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);

    }


    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {
                FINE_LOCATION,
                COARSE_LOCATION
        };

        // getApplicationContext() <---> this.getApplicationContext()
        if(ContextCompat.checkSelfPermission(getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();

            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called !!");
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0) { // some permission was granted

                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionGranted = true;
                    // initialize our map
                    initMap();

                }
        }

    }


    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the device's current location");


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (mLocationPermissionGranted) {

            try {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            mMap.addMarker(new MarkerOptions().position(current).title("MyLocation"));
                            moveCamera(current, DEFAULT_ZOOM, "MyLocation");

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(mContext, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (SecurityException e) {
                Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
            }
        }

    }


    private void moveCamera(LatLng latlng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to lat: " + latlng.latitude + ", lng: " + latlng.longitude);

        // clear previous markers
        mMap.clear();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latlng)
                    .title(title);

            mMap.addMarker(options);

        }

    }




    /******************** LAYOUT ********************/

    private void setupWidgets() {
        mContext = MainActivity.this;

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        searchPlace = findViewById(R.id.search_layout);
        searchFrom = findViewById(R.id.searchFrom);
        searchTo = findViewById(R.id.searchTo);

        if (getIntent().hasExtra("SEARCH_FROM") && getIntent().getStringExtra("SEARCH_FROM").length() > 0) {
            Log.d(TAG, "setupWidgets: haaaaaaaaas");
            searchFrom.setText(getIntent().getStringExtra("SEARCH_FROM"));

        } else {
            Log.d(TAG, "setupWidgets: nooooooooot : " + getIntent().hasExtra("SEARCH_FROM"));
            searchFrom.setText("My Location");
        }

        if (getIntent().hasExtra("SEARCH_TO") && getIntent().getStringExtra("SEARCH_TO").length() > 0) {
            Log.d(TAG, "setupWidgets: haaaaaaaaas");
            searchTo.setText(getIntent().getStringExtra("SEARCH_TO"));

        } else {
            Log.d(TAG, "setupWidgets: nooooooooot");
            searchTo.setText("Set up the destination");
        }

        searchPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlaceSearchActivity.class);
                intent.putExtra("SEARCH_FROM", searchFrom.getText());

                if (!searchTo.getText().equals("Set up the destination")) {
                    intent.putExtra("SEARCH_TO", searchTo.getText());
                }

                startActivity(intent);
            }
        });

    }

    private void setUpNavigationView() {

        // Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // Check to see which item was clicked and perform appropriate action
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        Intent profile = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(profile);
                        break;
                    case R.id.logout:
                        Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();
                        break;
                }

                drawerLayout.closeDrawers();

                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.closeDrawer, R.string.openDrawer);

        // Setting he action bar Toggle to drawer layout
        drawerLayout.setDrawerListener(toggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        toggle.syncState();


    }



}
