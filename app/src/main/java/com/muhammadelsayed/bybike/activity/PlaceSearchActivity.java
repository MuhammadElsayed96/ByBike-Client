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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.model.PlaceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceSearchActivity extends AppCompatActivity {

    private static final String TAG = "PlaceSearchActivity";
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));
    private static final AutocompleteFilter mPlaceFilter = new AutocompleteFilter.Builder()
            .setCountry("EG")
            .build();
    private static final int FROM_PLACE_PICKER_REQUEST = 1;
    private static final int TO_PLACE_PICKER_REQUEST = 2;

    // vars
    private GeoDataClient mGeoDataClient;
    private Context mContext;

    // Contains results from the 2 PlaceAutocomplete Search
    List<PlaceModel> placesFrom = new ArrayList<>();
    List<PlaceModel> placesTo = new ArrayList<>();

    // The last specified places by user
    public static Place placeFrom, placeTo;

    // widgets
    private Toolbar toolbar;
    public static SearchView searchFrom, searchTo;
    private ListView resultsList;
    private LinearLayout locationControls, myLocation, chooseLocation;

    final ArrayList<Map<String,String>> itemDataList = new ArrayList<Map<String,String>>();



    /**
     * Uses simple adapter to show the search results in the ListView
     * @param places a List of all the search result
     */
    private void simpleAdapterListView(final List<PlaceModel> places) {


        itemDataList.clear();

        for(int i =0; i < places.size(); i++) {
            Map<String,String> listItemMap = new HashMap<String,String>();
            listItemMap.put("name", places.get(i).getPrimaryText());
            listItemMap.put("address", places.get(i).getSecondaryText());

            itemDataList.add(listItemMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,itemDataList,android.R.layout.simple_list_item_2,
                new String[]{"name","address"},new int[]{android.R.id.text1,android.R.id.text2});
        resultsList.setAdapter(simpleAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_search_activity);

        // initializing activity widgets
        setupWidgets();

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this);

        searchFrom.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    simpleAdapterListView(placesFrom);

                    locationControls.setVisibility(View.VISIBLE);

                    myLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchFrom.setQuery("My Location", true);
                            Log.d(TAG, "onClick: searchFrom.getQuery() ====" + searchFrom.getQuery());
                            searchFrom.clearFocus();
                        }
                    });

                    chooseLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Toast.makeText(PlaceSearchActivity.this, "Not Implemented Yet !!", Toast.LENGTH_SHORT).show();

                            setupPlacePicker("From");
                            searchFrom.clearFocus();


                        }
                    });

                    resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                            final int index = position;

                            mGeoDataClient.getPlaceById(placesFrom.get(position).getPlaceID())
                                    .addOnSuccessListener(new OnSuccessListener<PlaceBufferResponse>() {
                                        @Override
                                        public void onSuccess(PlaceBufferResponse places) {
                                            placeFrom = places.get(0);
                                            Log.d(TAG, "onSuccess: placeFrom == " + placeFrom);
                                            if (placesFrom.size() > 0){
                                                searchFrom.setQuery(placesFrom.get(index).getPrimaryText(), true);
//                                                searchFrom.clearFocus();
                                            }
                                        }
                                    });

                        }
                    });

                } else {

                    locationControls.setVisibility(View.GONE);
                    simpleAdapterListView(new ArrayList<PlaceModel>());
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

                placeFrom = null;
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

                            if (placesFrom.contains(place))
                                continue;

                            placesFrom.add(place);

                        }

                        // releasing the buffer to avoid memory leaks
                        autocompletePredictions.release();

                        // fill ListView with the result
                        simpleAdapterListView(placesFrom);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Exception : " + e.getMessage());
                    }
                });

                return false;
            }
        });



        searchTo.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    // show results on the ListView
                    simpleAdapterListView(placesTo);

                    // show the Location Controls {My Location, Set location on map}
                    locationControls.setVisibility(View.VISIBLE);

                    myLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchTo.setQuery("My Location", false);
                            Log.d(TAG, "onClick: searchTo.getQuery() ==== " + searchTo.getQuery());

                            searchTo.clearFocus();
                        }
                    });

                    chooseLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Toast.makeText(PlaceSearchActivity.this, "Not Implemented Yet !!", Toast.LENGTH_SHORT).show();

                            setupPlacePicker("To");
                            searchTo.clearFocus();

                        }
                    });


                    resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                            final int index = position;

                            Log.d(TAG, "onItemClick: list item clicked !!!!!!!!!!!!!!!!!!!");

                            Log.d(TAG, "onItemClick: searchTo.getQuery() ==== " + searchTo.getQuery());

                            mGeoDataClient.getPlaceById(placesTo.get(position).getPlaceID())
                                    .addOnSuccessListener(new OnSuccessListener<PlaceBufferResponse>() {
                                        @Override
                                        public void onSuccess(PlaceBufferResponse places) {
                                            placeTo = places.get(0);
                                            Log.d(TAG, "onSuccess: placeFrom == " + placesTo);

                                            if (placesTo.size() > 0) {
                                                searchTo.setQuery(placesTo.get(index).getPrimaryText(), true);
//                                                searchTo.clearFocus();
                                            }
                                            Log.d(TAG, "onSuccess: searchTo.getQuery() ==== " + searchTo.getQuery());
                                        }
                                    });


                        }
                    });

                } else {

                    locationControls.setVisibility(View.GONE);
                    simpleAdapterListView(new ArrayList<PlaceModel>());
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

                placeTo = null;
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

                            if (placesTo.contains(place))
                                continue;

                            placesTo.add(place);

                        }

                        // releasing the buffer to avoid memory leaks
                        autocompletePredictions.release();

                        // fill ListView with the results
                        simpleAdapterListView(placesTo);

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


    /**
     * launches the google place picker allowing the user to select the location from the map
     * @param placeType place type {from , to}
     */
    private void setupPlacePicker(String placeType) {
        placeType = placeType.toLowerCase();

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        switch (placeType) {
            case "from":
                try {

                    startActivityForResult(builder.build(this), FROM_PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

                break;

            case "to":
                try {

                    startActivityForResult(builder.build(this), TO_PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == FROM_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(mContext, data);
                searchFrom.setQuery(place.getName(), true);
                placeFrom = place;

            }
        } else if (requestCode == TO_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(mContext, data);
                searchTo.setQuery(place.getName(), true);
                placeTo = place;
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);

                if (placeFrom != null || searchFrom.getQuery().toString().equals("My Location")) {
                    intent.putExtra("PLACE_FROM", searchFrom.getQuery().toString());

                    if (placeFrom != null) {
                        intent.putExtra("PLACE_FROM_ID", placeFrom.getId());
                    }
                }


                Log.d(TAG, "onBackPressed: searchTo.getQuery() ==== " + searchTo.getQuery().toString());


                if (searchTo.getQuery().toString().equals("My Location")) {
                    intent.putExtra("PLACE_TO", searchTo.getQuery().toString());

                } else {

                    if (!searchTo.getQuery().toString().equals("") && placeTo != null) {

                        intent.putExtra("PLACE_TO", searchTo.getQuery().toString());
                        intent.putExtra("PLACE_TO_ID", placeTo.getId());
                        Log.d(TAG, "onBackPressed: PLACE TO ==== " + placeTo.getName());

                    }

                }

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


        if (placeFrom != null || searchFrom.getQuery().toString().equals("My Location")) {
            intent.putExtra("PLACE_FROM", searchFrom.getQuery().toString());

            if (placeFrom != null) {
                intent.putExtra("PLACE_FROM_ID", placeFrom.getId());
            }
        }



        Log.d(TAG, "onBackPressed: searchTo.getQuery() ==== " + searchTo.getQuery().toString());

        if (searchTo.getQuery().toString().equals("My Location")) {
            intent.putExtra("PLACE_TO", searchTo.getQuery().toString());

        } else {

            if (!searchTo.getQuery().toString().equals("") && placeTo != null) {

                intent.putExtra("PLACE_TO", searchTo.getQuery().toString());
                intent.putExtra("PLACE_TO_ID", placeTo.getId());
                Log.d(TAG, "onBackPressed: PLACE TO ==== " + placeTo.getName());

            }

        }


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();

    }


    /********************** LAYOUT **********************/

    /**
     * sets up all the activity widgets
     */
    private void setupWidgets() {
        mContext = getApplicationContext();


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select locations");

        resultsList = findViewById(R.id.listview_place_result);

        locationControls = findViewById(R.id.layout_location_controls);
        locationControls.setVisibility(View.GONE);
        myLocation = findViewById(R.id.myLocation);
        chooseLocation = findViewById(R.id.chooseLocation);

        searchFrom = findViewById(R.id.search_from);
        searchTo = findViewById(R.id.search_to);

        searchFrom.setQuery(getIntent().getStringExtra("SEARCH_FROM"), false);
        searchTo.setQuery(getIntent().getStringExtra("SEARCH_TO"), false);

    }



}