package com.muhammadelsayed.bybike.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.muhammadelsayed.bybike.R;

import java.util.ArrayList;
import java.util.List;

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


    private static LatLng origin = null;
    private static LatLng destination = null;

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

        getLocationPermission();

        getDeviceLocation(null);


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

        // initializing activity widgets
        setupWidgets();

        // initializing navigation view
        setUpNavigationView();
    }

    /**
     * initializes the map fragment
     */
    private void initMap() {
        Log.d(TAG, "initMap: initializing the map...");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);

    }

    /**
     * gets the required permissions from the user to access his location
     */
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {
                FINE_LOCATION,
                COARSE_LOCATION
        };

        // getApplicationContext() <---> this.getApplicationContext()
        if (ContextCompat.checkSelfPermission(getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                if (grantResults.length > 0) { // some permission was granted

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


    /**
     * gets the device's current location and move the camera to it.
     *
     * @param placeType place type {from, to} YOU CAN SET IT TO NULL if you do not need it
     */
    private void getDeviceLocation(String placeType) {
        Log.d(TAG, "getDeviceLocation: getting the device's current location");

        if (placeType == null)
            placeType = "";

        placeType = placeType.toLowerCase();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (mLocationPermissionGranted) {

            switch (placeType) {
                case "from":
                case "":
                default:

                    try {
                        Task location = mFusedLocationProviderClient.getLastLocation();
                        location.addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful() && task.getResult() != null) {

                                    Log.d(TAG, "onComplete: found location");
                                    Location currentLocation = (Location) task.getResult();
                                    LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                                    /**/
                                    origin = current;
                                    /**/

                                    moveCamera(current, DEFAULT_ZOOM, "My Location", true);

                                } else {
                                    Log.d(TAG, "onComplete: current location is null");
                                    Toast.makeText(mContext, "unable to get current location", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (SecurityException e) {
                        Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
                    }

                    break;

                case "to":

                    try {
                        Task location = mFusedLocationProviderClient.getLastLocation();
                        location.addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful() && task.getResult() != null) {

                                    Log.d(TAG, "onComplete: found location");
                                    Location currentLocation = (Location) task.getResult();
                                    LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                                    /**/
                                    destination = current;
                                    /**/

                                    MarkerOptions options = new MarkerOptions()
                                            .position(current)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                            .title("My Location");

                                    if (mMap != null) {

                                        mMap.addMarker(options);

                                    }

                                } else {
                                    Log.d(TAG, "onComplete: current location is null");
                                    Toast.makeText(mContext, "unable to get current location", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (SecurityException e) {
                        Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
                    }

                    break;

            }

        }

    }

    /**
     * Gets the place latlng and sets a marker for it on the map an moves the camera to it
     *
     * @param placeId   place ID
     * @param placeType place type {from , to}
     */
    private void getPlaceLocation(String placeId, String placeType) {

        Log.d(TAG, "getPlaceLocation: getting the location...");

        if (placeId == null)
            return;

        placeType = placeType.toLowerCase();

        switch (placeType) {
            case "from":

                mGeoDataClient.getPlaceById(placeId)
                        .addOnSuccessListener(new OnSuccessListener<PlaceBufferResponse>() {
                            @Override
                            public void onSuccess(PlaceBufferResponse places) {

                                Place place = places.get(0);

                                MarkerOptions options = new MarkerOptions()
                                        .position(place.getLatLng())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                        .title(place.getName().toString());

                                /**/
                                origin = place.getLatLng();
                                /**/

                                if (mMap != null) {
                                    mMap.clear();

                                    mMap.addMarker(options);

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), DEFAULT_ZOOM));
                                }

                                // releasing the buffer to avoid memory leaaaaaks
                                places.release();
                            }
                        });

                break;

            case "to":

                mGeoDataClient.getPlaceById(placeId)
                        .addOnSuccessListener(new OnSuccessListener<PlaceBufferResponse>() {
                            @Override
                            public void onSuccess(PlaceBufferResponse places) {

                                Place place = places.get(0);

                                MarkerOptions options = new MarkerOptions()
                                        .position(place.getLatLng())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                        .title(place.getName().toString());


                                /**/
                                destination = place.getLatLng();
                                /**/

                                mMap.addMarker(options);

                                // releasing the buffer to avoid memory leaaaaaks
                                places.release();
                            }
                        });
                break;

            default:

                break;
        }


    }

    /**
     * moves the camera to a specific location on map and sets a marker
     */
    private void moveCamera(LatLng latlng, float zoom, String title, boolean clear) {
        Log.d(TAG, "moveCamera: moving the camera to lat: " + latlng.latitude + ", lng: " + latlng.longitude);

        // clear previous markers
        if (clear)
            mMap.clear();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

//        if (!title.equals("My Location")) {
        MarkerOptions options = new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(title);

        mMap.addMarker(options);

//        }

    }


    /******************** LAYOUT ********************/

    /**
     * sets up all activity widgets
     */
    private void setupWidgets() {
        mContext = MainActivity.this;

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        searchPlace = findViewById(R.id.search_layout);
        searchFrom = findViewById(R.id.searchFrom);
        searchTo = findViewById(R.id.searchTo);

        // User has specified the starting point.
        if (getIntent().hasExtra("PLACE_FROM") && getIntent().getStringExtra("PLACE_FROM").length() > 0) {

            searchFrom.setText(getIntent().getStringExtra("PLACE_FROM"));

            // if user set the starting location to his current location
            if (getIntent().getStringExtra("PLACE_FROM").equals("My Location")) {

                getDeviceLocation("From");

            } else { // if the user set the starting location to any where other than his location

                String placeId = getIntent().getStringExtra("PLACE_FROM_ID");

                getPlaceLocation(placeId, "From");

            }

        } else { // user has not specified the starting point

            // set the starting point to the user's current location.
            searchFrom.setText("My Location");
            getDeviceLocation("From");
        }

        // User has specified the destination.
        if (getIntent().hasExtra("PLACE_TO") && getIntent().getStringExtra("PLACE_TO").length() > 0) {

            searchTo.setText(getIntent().getStringExtra("PLACE_TO"));

            // if user set the destination to his current location
            if (getIntent().getStringExtra("PLACE_TO").equals("My Location")) {

                getDeviceLocation("To");


            } else { // if the user set the destination to any where other than his location

                String placeId = getIntent().getStringExtra("PLACE_TO_ID");

                getPlaceLocation(placeId, "To");

            }
        } else { // User has not set the destination

            searchTo.setText("Set up the destination");
        }


        // navigate the user to the SearchPlaceActivity
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

    /**
     * sets up the NavigationView
     */
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
