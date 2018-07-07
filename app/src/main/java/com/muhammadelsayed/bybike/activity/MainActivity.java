package com.muhammadelsayed.bybike.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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
import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.ProfileActivities.ProfileActivity;
import com.muhammadelsayed.bybike.activity.model.Transportation;
import com.muhammadelsayed.bybike.activity.model.User;
import com.muhammadelsayed.bybike.activity.model.UserModel;
import com.muhammadelsayed.bybike.activity.network.RetrofitClientInstance;
import com.muhammadelsayed.bybike.activity.network.UserClient;
import com.muhammadelsayed.bybike.activity.utils.CustomSpinnerAdapter;
import com.muhammadelsayed.bybike.activity.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.muhammadelsayed.bybike.activity.SplashActivity.currentUser;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        RoutingListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final float DEFAULT_ZOOM = 17f;

    // REQUEST_CODES
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int PLACE_PICKER_REQUEST = 210;
    private static final int REQUEST_LOCATION = 644;

    // vars
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest.Builder mLocationSettingsRequest;
    private Context mContext;

    PendingResult<LocationSettingsResult> pendingResult;

    private List<Transportation> transportations = Arrays.asList(new Transportation());

    public static LatLng origin = null;
    public static LatLng destination = null;

    // widgets
    private RelativeLayout searchPlace;
    private TextView searchFrom, searchTo;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView profilePhoto;
    private TextView mFullname;
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
        Log.wtf(TAG, "onCreate() has been instantiated");

        super.onCreate(savedInstanceState);
        Utils.checkUserSession(MainActivity.this);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: created !!");
        polylines = new ArrayList<>();

        getLocationPermission();


    }

    /******************** Map ********************/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.wtf(TAG, "onMapReady() has been instantiated");

        if (mMap == null) {
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
        Log.wtf(TAG, "initMap() has been instantiated");

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
        Log.wtf(TAG, "getLocationPermission() has been instantiated");

        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {
                FINE_LOCATION,
                COARSE_LOCATION
        };

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
        Log.wtf(TAG, "onRequestPermissionsResult() has been instantiated");
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
        Log.wtf(TAG, "getDeviceLocation() has been instantiated");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (placeType == null)
            placeType = "";
        placeType = placeType.toLowerCase();

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

                                    origin = current;

                                    drawRoute(origin, destination);

                                } else {
                                    mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                    if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                                        mEnableGps();

                                    }
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

                                    destination = current;

                                    drawRoute(origin, destination);

                                } else {
                                    Log.d(TAG, "onComplete: current location is null");
                                    mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                    if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                        mEnableGps();
                                    }

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
        Log.wtf(TAG, "getPlaceLocation() has been instantiated");

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

                                origin = place.getLatLng();

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

                                destination = place.getLatLng();

                                drawRoute(origin, destination);

                                Log.d(TAG, "onRoutingFailure: ORIGIN === " + origin + "\nDESTINTAION === " + destination);

                                // releasing the buffer to avoid memory leaaaaaks
                                places.release();
                            }
                        });

                break;
        }
    }

    /******************** Routing ********************/
    /**
     * draws shortest route between two points,
     * showing all the alternative routes
     * using this library : https://github.com/jd-alexander/Google-Directions-Android
     *
     * @param origin      the location from which the trip will start
     * @param destination the location where the trip ends
     */
    private void drawRoute(LatLng origin, LatLng destination) {
        Log.wtf(TAG, "drawRoute() has been instantiated");

        mMap.clear();
        markersList.clear();

        if (origin != null) {

            MarkerOptions originOptions = new MarkerOptions()
                    .position(origin)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_origin))
                    .title("Start");

            markersList.add(mMap.addMarker(originOptions));

            if (destination == null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, DEFAULT_ZOOM));
            } else {
                MarkerOptions destOptions = new MarkerOptions()
                        .position(destination)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination))
                        .title("End");

                markersList.add(mMap.addMarker(destOptions));

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
    }

    /**
     * removes all routes from map
     */
    private void ereasePolylines() {
        Log.wtf(TAG, "ereasePolylines() has been instantiated");

        for (Polyline line : polylines)
            line.remove();
        polylines.clear();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Log.wtf(TAG, "onRoutingFailure() has been instantiated");

        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "onRoutingFailure: Error: " + e.getMessage());
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
        Log.wtf(TAG, "onRoutingStart() has been instantiated");

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        Log.wtf(TAG, "onRoutingSuccess() has been instantiated");

        if (polylines.size() > 0)
            for (Polyline poly : polylines)
                poly.remove();

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
        Log.wtf(TAG, "onRoutingCancelled() has been instantiated");

    }

    /******************** GPS STATUS TRACKING ********************/

    /**
     * initializes GoogleApiClient object and requests the location settings to get the gps state
     */
    private void mEnableGps() {
        Log.wtf(TAG, "mEnableGps() has been instantiated");

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        mLocationSetting();
    }

    /**
     * requests the location settings to get the gps state
     */
    private void mLocationSetting() {
        Log.wtf(TAG, "mLocationSetting() has been instantiated");

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        mResult();
    }

    /**
     * gets the gps state and prompts the user to open the GPS
     */
    private void mResult() {
        Log.wtf(TAG, "mResult() has been instantiated");

        pendingResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest.build());
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {

                            status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);

                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "onResult: IntentSender.SendIntentException : " + e.getMessage());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.

                        break;
                }
            }

        });
    }

    //callback method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.wtf(TAG, "onActivityResult() has been instantiated");

        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Toast.makeText(mContext, "Gps enabled", Toast.LENGTH_SHORT).show();
                        getDeviceLocation(null);
                        drawRoute(origin, destination);

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(mContext, "Gps Canceled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
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


    /******************** LAYOUT ********************/


    @Override
    protected void onResume() {
        Log.wtf(TAG, "onResume() has been instantiated");

        super.onResume();
        navigationView = findViewById(R.id.nav_view);


        View headerView = navigationView.getHeaderView(0);

        profilePhoto = headerView.findViewById(R.id.profile_photo);
        Picasso.get()
                .load(RetrofitClientInstance.BASE_URL + currentUser.getUser().getImage())
                .error(R.mipmap.icon_launcher)
                .into(profilePhoto);

//        profilePhoto.setImageURI(Uri.parse(RetrofitClientInstance.BASE_URL + currentUser.getUser().getImage()));
        Log.d(TAG, "setUpNavigationView: IMAGE = " + profilePhoto);
        mFullname = headerView.findViewById(R.id.fullname);
        mFullname.setText(currentUser.getUser().getName());

    }

    /**
     * sets up all activity widgets
     */
    private void setupWidgets() {
        Log.wtf(TAG, "setupWidgets() has been instantiated");

        mContext = MainActivity.this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

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

                destination = null;
                origin = null;
                mMap.clear();

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
        Log.wtf(TAG, "showSubmitLayout() has been instantiated");

        transportations.get(0).setTransType("Bicycle");
//        transportations.get(1).setTransType("Motorcycle");
//        transportations.get(2).setTransType("Car");
        transportations.get(0).setTransImg(R.drawable.ic_bike);
//        transportations.get(1).setTransImg(R.drawable.ic_motorcycle);
//        transportations.get(2).setTransImg(R.drawable.ic_car);
        transportations.get(0).setTransCost(10);
//        transportations.get(1).setTransCost(18);
//        transportations.get(2).setTransCost(24);

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
        Log.wtf(TAG, "resetActivity() has been instantiated");

        submitLayout.setVisibility(View.GONE);
        ereasePolylines();
        mMap.clear();
        destination = null;
        origin = null;
        searchFrom.setText("My Location");
        searchTo.setText("Set up the destination");
        getDeviceLocation("");
        drawRoute(origin, destination);

    }

    /**
     * sets up the NavigationView
     */
    private void setUpNavigationView() {
        Log.wtf(TAG, "setUpNavigationView() has been instantiated");

        // Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // Check to see which item was clicked and perform appropriate action
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        Intent profile = new Intent(MainActivity.this, ProfileActivity.class);
//                        UserModel currentUser = (UserModel) getIntent().getSerializableExtra("current_user");
//                        profile.putExtra("current_user", currentUser);
                        startActivity(profile);
                        break;

                    case R.id.nav_history:
                        Intent history = new Intent(MainActivity.this, HistoryActivity.class);
                        startActivity(history);
                        break;

                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        LinearLayout logout = findViewById(R.id.logout_layout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                drawerLayout.closeDrawers();
                Intent start = new Intent(mContext, StartActivity.class);
                start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(start);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.closeDrawer, R.string.openDrawer);

        // Setting he action bar Toggle to drawer layout
        drawerLayout.setDrawerListener(toggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        toggle.syncState();

    }


}
