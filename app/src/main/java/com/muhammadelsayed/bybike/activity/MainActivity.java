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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.StaticMapsRequest;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.model.Transportation;
import com.muhammadelsayed.bybike.activity.utils.CustomSpinnerAdapter;
import com.muhammadelsayed.bybike.activity.utils.CustomToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, RoutingListener {

    private static final String TAG = "MainActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final float DEFAULT_ZOOM = 17f;


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

    private List<Transportation> transportations = Arrays.asList(new Transportation(), new Transportation(), new Transportation());


    private static LatLng origin = null;
    private static LatLng destination = null;

    // widgets
    private RelativeLayout searchPlace;
    private TextView searchFrom, searchTo;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private Spinner spinnerTransportation;
    private RelativeLayout submitLayout;
    private CustomSpinnerAdapter spinnerAdapter;
    private Button submitTrans;
    private Button cancelTrans;


    // for getting the route
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorPrimary, R.color.error};


    private ArrayList<Marker> markersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            origin = savedInstanceState.getParcelable("origin");
            destination = savedInstanceState.getParcelable("destination");
        }


        polylines = new ArrayList<>();

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
        if (mMap == null){
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

                                    drawRoute(origin, destination);

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

                                    drawRoute(origin, destination);

                                    Log.d(TAG, "onComplete: markersList = " + markersList.size());

                                    Log.d(TAG, "22222 # onComplete: ORIGIN === " + origin + "\n DESTINATION === " + destination);


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

                                /**/
                                origin = place.getLatLng();
                                /**/

                                drawRoute(origin, destination);

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

                                /**/
                                destination = place.getLatLng();
                                /**/

                                drawRoute(origin, destination);

                                Log.d(TAG, "onRoutingFailure: ORIGIN === " + origin + "\nDESTINTAION === " + destination);


                                // releasing the buffer to avoid memory leaaaaaks
                                places.release();
                            }
                        });
                break;

            default:

                break;
        }


    }


    /******************** Routing ********************/

    /**
     * draws shortest route between two points,
     * showing all the alternative routes
     * @param origin the location from which the trip will start
     * @param destination the location where the trip ends
     */
    private void drawRoute(LatLng origin, LatLng destination) {

        mMap.clear();
        markersList.clear();

        if (origin != null) {

            MarkerOptions originOptions = new MarkerOptions()
                    .position(origin)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title("Start");

            markersList.add(mMap.addMarker(originOptions));

        }

        if (destination != null) {

            MarkerOptions destOptions = new MarkerOptions()
                    .position(destination)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title("End");

            markersList.add(mMap.addMarker(destOptions));

        }

        if (origin != null && destination == null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, DEFAULT_ZOOM));
        } else if (origin != null) {

            // controlling the camera position in a way that show both markers
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker m : markersList) {
                builder.include(m.getPosition());
            }
            LatLngBounds bounds = builder.build();

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.12);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, (height / 2), padding));

            // getting the route
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(false)
                    .waypoints(origin, destination)
                    .build();

            routing.execute();

        }

    }


    /**
     * removes all routes from map
     */
    private void ereasePolylines() {

        for (Polyline line : polylines) {
            line.remove();
        }
        polylines.clear();

    }

    @Override
    public void onRoutingFailure(RouteException e) {

        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "onRoutingFailure: Error: " + e.getMessage());
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {


        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        String distance = "", duration = "";

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);


            distance = route.get(0).getDistanceText();
            duration = route.get(0).getDurationText();


        }

        for (int i = 0; i < transportations.size(); i++) {
            transportations.get(i).setTransDistance(distance + " - " + duration);
        }

        showSubmitLayout();


    }

    @Override
    public void onRoutingCancelled() {

    }


    /******************** LAYOUT ********************/

    /**
     * sets up all activity widgets
     */
    private void setupWidgets() {
        mContext = MainActivity.this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        searchPlace = findViewById(R.id.search_layout);
        searchFrom = findViewById(R.id.searchFrom);
        searchTo = findViewById(R.id.searchTo);

        submitLayout = findViewById(R.id.submit_layout);
        submitLayout.setVisibility(View.GONE);
        spinnerTransportation = findViewById(R.id.spinnerTransportation);

        submitTrans = findViewById(R.id.btnSubmit);
        cancelTrans = findViewById(R.id.btnCancel);


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
            destination = null;
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
     * shows the layout to allow the user to submit or cancel the order he made
     */
    private void showSubmitLayout() {

        for (int i = 0; i < transportations.size(); i++) {
            transportations.get(i).setTransType("Bicycle");
            transportations.get(i).setTransImg(R.drawable.ic_bike);
            transportations.get(i).setTransCost("12 L.E.");
        }


        submitLayout.setVisibility(View.VISIBLE);

        spinnerAdapter = new CustomSpinnerAdapter(mContext, R.layout.custom_spinner_transprotaiton, transportations);

        spinnerTransportation.setAdapter(spinnerAdapter);

        final Transportation order = new Transportation();

        spinnerTransportation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                order.setTransCost(transportations.get(position).getTransCost());
                order.setTransImg(transportations.get(position).getTransImg());
                order.setTransType(transportations.get(position).getTransType());
                order.setTransDistance(transportations.get(position).getTransDistance());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ConfirmOrderActivity.class);

                intent.putExtra("placeFrom", searchFrom.getText());
                intent.putExtra("placeTo", searchTo.getText());
                intent.putExtra("totalCost", order.getTransCost());

                startActivity(intent);

            }
        });

        cancelTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetActivity();
            }
        });
    }


    /**
     * resets everything to its default
     */
    private void resetActivity() {

        submitLayout.setVisibility(View.GONE);
        ereasePolylines();
        mMap.clear();
        destination = null;
        origin = null;
        searchFrom.setText("My Location");
        searchTo.setText("Set up the destination");
        getDeviceLocation(null);
        drawRoute(origin, destination);

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



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable("destination", destination);
            outState.putParcelable("origin", origin);
            super.onSaveInstanceState(outState);
        }
    }



}
