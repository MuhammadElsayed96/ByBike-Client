package com.muhammadelsayed.bybike.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.model.RetroResponse;
import com.muhammadelsayed.bybike.activity.model.TripModel;
import com.muhammadelsayed.bybike.activity.network.OrderClient;
import com.muhammadelsayed.bybike.activity.network.RetrofitClientInstance;
import com.muhammadelsayed.bybike.activity.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.pcyan.sweetdialog.SweetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.muhammadelsayed.bybike.activity.ConfirmOrderActivity.currentOrder;
import static com.muhammadelsayed.bybike.activity.SplashActivity.currentUser;
import static com.muhammadelsayed.bybike.activity.WaitingActivity.orderInfo;


public class OrderTracking extends FragmentActivity implements OnMapReadyCallback,
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
    private DatabaseReference refOrders;
    private ValueEventListener mStatusChangedListener;

    //    private Order currentOrder;
    private LatLng riderLatLng;

    // widgets
    private Button btnCancelTrip, btnCallRider;
    private TextView txtRiderName;
    private ImageView riderProfilePhoto;
    private GoogleMap mMap;

    private boolean flag;
    private SweetDialog riderCancelDialog;
    private SweetDialog riderDeliveredPackageDialog;
    private SweetDialog riderOnSiteDialog;
    private SweetDialog cancelOrderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.wtf(TAG, "onCreate() has been instantiated");
        super.onCreate(savedInstanceState);
        Utils.checkUserSession(OrderTracking.this);

        setContentView(R.layout.activity_order_tracking);
        txtRiderName = findViewById(R.id.rider_name_textview);
        riderProfilePhoto = findViewById(R.id.rider_profile_image);
        btnCallRider = findViewById(R.id.call_rider_button);
        btnCancelTrip = findViewById(R.id.cancel_trip_btn);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupWidgets();

        Log.wtf(TAG, "currentOrder = " + currentOrder);
        Log.wtf(TAG, "currentUser = " + currentUser);

        refOrders = FirebaseDatabase.getInstance().getReference("orders").child(String.valueOf(currentOrder.getId()));

        mStatusChangedListener = refOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long status = (long) dataSnapshot.child("status").getValue();

                Log.wtf(TAG, "STATUS = " + status);
                Log.wtf(TAG, "STATUS = " + status);

                if (status == 5) {
                    Toast.makeText(OrderTracking.this, "Canceled By User !!", Toast.LENGTH_SHORT).show();
                    Log.wtf(TAG, "STATUS = Canceled By User");
                    btnCancelTrip.setVisibility(View.GONE);

                    Intent intent = new Intent(OrderTracking.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    refOrders.removeEventListener(mStatusChangedListener);
                }

                if (status == 4) {

                    if (riderCancelDialog != null)
                        riderCancelDialog = null;
                    riderCancelDialog = SweetDialog.build()
                            .setCancelAble(false)
                            .autoCancel(true)
                            .setTitle("Rider Cancel")
                            .setContent("Rider canceled the order")
                            .setConfirmText("Ok")
                            .setOnConfirmListener(new SweetDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetDialog sweetDialog) {
                                    Toast.makeText(OrderTracking.this, "Canceled By Rider !!", Toast.LENGTH_SHORT).show();
                                    Log.wtf(TAG, "STATUS = Canceled By Rider");
                                    Intent intent = new Intent(OrderTracking.this, MainActivity.class);
                                    startActivity(intent);
                                    sweetDialog.dismiss();
                                    finish();
                                    refOrders.removeEventListener(mStatusChangedListener);
                                }
                            });
                    riderCancelDialog.showDialog(getSupportFragmentManager(), "rider_cancel_order");

                } else if (status == 3) {

                    if (riderDeliveredPackageDialog != null)
                        riderDeliveredPackageDialog = null;
                    riderDeliveredPackageDialog = SweetDialog.build()
                            .autoCancel(true).setCancelAble(false)
                            .setTitle("Package Delivered")
                            .setContent("Rider delivered the package")
                            .setConfirmText("Ok")
                            .setOnConfirmListener(new SweetDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetDialog sweetDialog) {
                                    Toast.makeText(OrderTracking.this, "Received !!", Toast.LENGTH_SHORT).show();
                                    Log.wtf(TAG, "STATUS = Received");
                                    btnCancelTrip.setVisibility(View.GONE);

                                    Intent rate = new Intent(OrderTracking.this, RiderRating.class);
                                    rate.putExtra("riderName", orderInfo.getTransporter().getName());
                                    rate.putExtra("riderPhoto", orderInfo.getTransporter().getImage());
                                    sweetDialog.dismiss();
                                    startActivity(rate);
                                    refOrders.removeEventListener(mStatusChangedListener);
                                }
                            });
                    riderDeliveredPackageDialog.showDialog(getSupportFragmentManager(), "rider_delivered_package");

                } else if (status == 2) {

                    if (riderOnSiteDialog != null)
                        riderOnSiteDialog = null;
                    riderOnSiteDialog = SweetDialog.build()
                            .setCancelAble(false)
                            .autoCancel(true)
                            .setTitle("Rider Here")
                            .setContent("Rider at your doorstep")
                            .setConfirmText("Ok")
                            .setOnConfirmListener(new SweetDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetDialog sweetDialog) {
//                    Toast.makeText(OrderTracking.this, "Approved !!", Toast.LENGTH_SHORT).show();
                                    Log.wtf(TAG, "STATUS = Approved");

                                    btnCancelTrip.setVisibility(View.GONE);

                                }
                            });
                    riderOnSiteDialog.showDialog(getSupportFragmentManager(), "rider_on_site");


                } else if (status == 1) {
//                    Toast.makeText(OrderTracking.this, "Accepted !!", Toast.LENGTH_SHORT).show();
                    Log.wtf(TAG, "STATUS = Accepted");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.wtf(TAG, "onCancelled()");
            }
        });
    }

    private void setupWidgets() {
        Log.e(TAG, "setupWidgets() has been instantiated");

        txtRiderName.setText(orderInfo.getTransporter().getName());

        Picasso.get()
                .load(RetrofitClientInstance.BASE_URL + orderInfo.getTransporter().getImage())
                .placeholder(R.mipmap.icon_launcher)
                .error(R.mipmap.icon_launcher)
                .into(riderProfilePhoto);

        btnCallRider.setOnClickListener(btnCallRiderListener);

        btnCancelTrip.setOnClickListener(btnCancelTripListener);

    }

    private Button.OnClickListener btnCancelTripListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if (cancelOrderDialog != null)
                cancelOrderDialog = null;
            cancelOrderDialog = SweetDialog.build()
                    .setCancelAble(false)
                    .autoCancel(true)
                    .setTitle("Cancel order")
                    .setContent("Are you sure?")
                    .setConfirmText("Yes, Cancel order")
                    .setOnConfirmListener(new SweetDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(final SweetDialog sweetDialog) {
                            OrderClient service = RetrofitClientInstance.getRetrofitInstance()
                                    .create(OrderClient.class);
                            TripModel trip = new TripModel(currentUser.getToken(), currentOrder.getUuid());
                            Log.d(TAG, "currentUser: " + currentUser);
                            Call<RetroResponse> call = service.cancelOrder(trip);
                            call.enqueue(new Callback<RetroResponse>() {
                                @Override
                                public void onResponse(Call<RetroResponse> call, Response<RetroResponse> response) {

                                    if (response.body() != null) {

                                        Log.d(TAG, "onResponse: " + response.body().getMessage());
//                        Toast.makeText(OrderTracking.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        sweetDialog.dismiss();
                                    } else {
                                        Log.d(TAG, "onResponse: RESPONSE BODY = " + response.body());
                                    }
                                }

                                @Override
                                public void onFailure(Call<RetroResponse> call, Throwable t) {

                                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
//                    Toast.makeText(OrderTracking.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).setOnCancelListener(new SweetDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetDialog sweetDialog) {
                            sweetDialog.dismiss();
                        }
                    });
            cancelOrderDialog.showDialog(getSupportFragmentManager(), "client_cancel_order");


        }
    };

    private Button.OnClickListener btnCallRiderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e(TAG, "Call client btn has been clicked");

            try {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + orderInfo.getTransporter().getPhone()));
                if (ActivityCompat.checkSelfPermission(OrderTracking.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // requesting permission
                    ActivityCompat.requestPermissions(OrderTracking.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            10);
                    return;
                }
                startActivity(callIntent);
            } catch (ActivityNotFoundException activityException) {
                Log.e("Calling a Phone Number", "Call failed", activityException);
            }
        }
    };


    private void displayMarkers() {
        Log.wtf(TAG, "displayOriginDestination() has been instantiated");

        if (mMap != null)
            mMap.clear();

        double originLat = Double.parseDouble(currentOrder.getSender_Lat());
        double originLng = Double.parseDouble(currentOrder.getSender_Lng());
        double destinationLat = Double.parseDouble(currentOrder.getReceiver_lat());
        double destinationLng = Double.parseDouble(currentOrder.getReceiver_lng());

        origin = new LatLng(originLat, originLng);

        MarkerOptions optionsOrg = new MarkerOptions()
                .position(origin)
                .title("Start")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_origin));

        mMap.addMarker(optionsOrg);

        destination = new LatLng(destinationLat, destinationLng);

        MarkerOptions optionsDes = new MarkerOptions()
                .position(destination)
                .title("End")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination));

        mMap.addMarker(optionsDes);

        MarkerOptions optionsRider = new MarkerOptions()
                .position(riderLatLng)
                .title("Rider")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bicycle));

        mMap.addMarker(optionsRider);

        drawRoute(riderLatLng, origin, destination);
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
        mMap = googleMap;

        flag = false;

        Log.d(TAG, "onMapReady: currentOrder = " + currentOrder);
        refDriver = FirebaseDatabase.getInstance().getReference("Drivers").child(String.valueOf(orderInfo.getTransporter().getUuid())).child("l");
        Log.wtf(TAG, "refDriver = " + refDriver);

        refDriver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String riderLat = String.valueOf(dataSnapshot.child(String.valueOf(0)).getValue());
                String riderLng = String.valueOf(dataSnapshot.child(String.valueOf(1)).getValue());

                Log.wtf(TAG, "DataSnapshot = " + dataSnapshot);
                Log.wtf(TAG, "LAT = " + riderLat + " ---------- LNG = " + riderLng);

                if (!riderLat.equals("null") && !riderLng.equals("null")) {
                    riderLatLng = new LatLng(Double.parseDouble(riderLat), Double.parseDouble(riderLng));
                    displayMarkers();

                    if (!flag)
                        moveCamToProperZoom(riderLatLng, origin, destination);

                    flag = true;
//                        Toast.makeText(OrderTracking.this, riderLatLng.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.wtf(TAG, "onCancelled()");

            }
        });

    }


    /******************** Routing ********************/
    /**
     * draws shortest route between two points,
     * showing all the alternative routes,
     * but I disabled that here
     * <p>
     * library : https://github.com/jd-alexander/Google-Directions-Android
     *
     * @param riderLatLng the location of the rider (device)
     * @param origin      the location from which the trip will start
     * @param destination the location where the trip ends
     */
    private void drawRoute(LatLng riderLatLng, LatLng origin, LatLng destination) {
        Log.wtf(TAG, "drawRoute() has been instantiated");

        // getting the route
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(riderLatLng, origin, destination)
                .build();

        routing.execute();

    }

    /**
     * Moves the camera and changes its zoom in a way
     * that show all the three Markers.
     *
     * @param riderLatLng the location of the rider (device)
     * @param origin      the location from which the trip will start
     * @param destination the location where the trip ends
     */
    private void moveCamToProperZoom(LatLng riderLatLng, LatLng origin, LatLng destination) {
        Log.wtf(TAG, "moveCamToProperZoom() has been instantiated");

        // controlling the camera position in a way that show both markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(riderLatLng);
        builder.include(origin);
        builder.include(destination);
        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
    }

    /**
     * Removes all the Polyline (routes) from the map.
     */
    private void erasePolylines() {
        Log.wtf(TAG, "erasePolylines() has been instantiated");

        if (polylines != null)
            if (polylines.size() > 0)
                for (Polyline poly : polylines)
                    poly.remove();
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

        // removing old polylines
        erasePolylines();

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

        }
    }

    @Override
    public void onRoutingCancelled() {
        Log.wtf(TAG, "onRoutingCancelled() has been instantiated");

    }
}
