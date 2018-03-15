package com.muhammadelsayed.bybike.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.muhammadelsayed.bybike.R;


public class HomeFragment extends Fragment implements OnMapReadyCallback{


    // Map Related Variables;
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;


    private Button btnRider;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        btnRider = mView.findViewById(R.id.btnRider);

        btnRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyProgressDialog);
                progressDialog.setMessage("Finding rider...");
                progressDialog.setCancelable(true);
                progressDialog.show();

//                Toast.makeText(getActivity(), "Rider Triggered", Toast.LENGTH_LONG).show();
            }
        });
        return mView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = mView.findViewById(R.id.map);

        Log.i("TEST", "View Created");
        if(mMapView != null) {

            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }


    @Override
    public void onDestroyView() {
        Log.i("TEST", "View Destroyed");

        mGoogleMap.clear();
        mMapView.onDestroy();
        super.onDestroyView();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MapsInitializer.initialize(getContext());
        }

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.addMarker(new MarkerOptions().position(new LatLng(31.037933, 31.381523)).title("Al MANSOURA").snippet("this is where we start"));

        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(31.037933, 31.381523)).zoom(16).bearing(0).tilt(45).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
