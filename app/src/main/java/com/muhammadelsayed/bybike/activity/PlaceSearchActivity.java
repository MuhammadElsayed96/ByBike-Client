package com.muhammadelsayed.bybike.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.model.PlaceModel;
import com.muhammadelsayed.bybike.activity.utils.ListViewCustomAdapter;

import java.util.ArrayList;
import java.util.List;

public class PlaceSearchActivity extends AppCompatActivity {

    private static final String TAG = "PlaceSearchActivity";
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));
    private static final AutocompleteFilter mPlaceFilter = new AutocompleteFilter.Builder()
            .setCountry("EG")
            .build();

    // vars
    private GeoDataClient mGeoDataClient;
    private Context mContext;

    // Contains results from the 2 PlaceAutocomplete Search
    List<PlaceModel> placesFrom = new ArrayList<>();
    List<PlaceModel> placesTo = new ArrayList<>();


    public static SearchView searchFrom, searchTo;
    ListView resultsList;
    ListViewCustomAdapter listAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_search_activity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // initializing activity widgets
        setupWidgets();

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this);

        searchFrom.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    listAdapter = new ListViewCustomAdapter(placesFrom, mContext);
                    resultsList.setAdapter(listAdapter);
                } else {

                    listAdapter = new ListViewCustomAdapter(new ArrayList<PlaceModel>(), mContext);
                    resultsList.setAdapter(listAdapter);
                }
            }
        });

        searchFrom.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                placesFrom.clear();
                Task<AutocompletePredictionBufferResponse> results =
                        mGeoDataClient.getAutocompletePredictions(newText, LAT_LNG_BOUNDS,
                                mPlaceFilter);

                results.addOnSuccessListener(new OnSuccessListener<AutocompletePredictionBufferResponse>() {
                    @Override
                    public void onSuccess(AutocompletePredictionBufferResponse autocompletePredictions) {

                        for (AutocompletePrediction response : autocompletePredictions) {

                            PlaceModel place = new PlaceModel(
                                    response.getFullText(null).toString(),
                                    response.getPrimaryText(null).toString(),
                                    response.getSecondaryText(null).toString(),
                                    response.getPlaceId()
                            );

                            if (placesFrom.indexOf(place) == -1) {
                                placesFrom.add(place);
                            }

                            listAdapter = new ListViewCustomAdapter(placesFrom, mContext);
                            resultsList.setAdapter(listAdapter);
                        }

                        autocompletePredictions.release();

                    }
                });

                return false;
            }
        });



        searchTo.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    listAdapter = new ListViewCustomAdapter(placesTo, mContext);
                    resultsList.setAdapter(listAdapter);
                } else {

                    listAdapter = new ListViewCustomAdapter(new ArrayList<PlaceModel>(), mContext);
                    resultsList.setAdapter(listAdapter);
                }
            }
        });

        searchTo.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                placesTo.clear();
                Task<AutocompletePredictionBufferResponse> results =
                        mGeoDataClient.getAutocompletePredictions(newText, LAT_LNG_BOUNDS,
                                mPlaceFilter);

                results.addOnSuccessListener(new OnSuccessListener<AutocompletePredictionBufferResponse>() {
                    @Override
                    public void onSuccess(AutocompletePredictionBufferResponse autocompletePredictions) {

                        for (AutocompletePrediction response : autocompletePredictions) {

                            PlaceModel place = new PlaceModel(
                                    response.getFullText(null).toString(),
                                    response.getPrimaryText(null).toString(),
                                    response.getSecondaryText(null).toString(),
                                    response.getPlaceId()
                            );

                            if (placesTo.indexOf(place) == -1) {
                                placesTo.add(place);
                            }

                            Log.d(TAG, "onSuccess: PLACESTO : " + placesTo);

                            listAdapter = new ListViewCustomAdapter(placesTo, mContext);
                            resultsList.setAdapter(listAdapter);
                        }

                        autocompletePredictions.release();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: Exception : " + e.getMessage());
                    }
                });

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);

                Log.d(TAG, "onBackPressed: BACK BUTTON PRESSED");

                intent.putExtra("SEARCH_FROM", searchFrom.getQuery().toString());
                intent.putExtra("SEARCH_TO", searchTo.getQuery().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        Log.d(TAG, "onBackPressed: BACK BUTTON PRESSED");

        Intent intent = new Intent(this, MainActivity.class);


        intent.putExtra("SEARCH_FROM", searchFrom.getQuery().toString());
        intent.putExtra("SEARCH_TO", searchTo.getQuery().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();

    }


    /********************** LAYOUT **********************/
    private void setupWidgets() {
        mContext = getApplicationContext();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resultsList = findViewById(R.id.listview_place_result);

        searchFrom = findViewById(R.id.search_from);
        searchTo = findViewById(R.id.search_to);

        searchFrom.setQuery(getIntent().getStringExtra("SEARCH_FROM"), false);
        searchTo.setQuery(getIntent().getStringExtra("SEARCH_TO"), false);

    }
}