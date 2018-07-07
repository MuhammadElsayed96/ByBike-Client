package com.muhammadelsayed.bybike.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.model.HistoryModel;
import com.muhammadelsayed.bybike.activity.model.RetroResponse;
import com.muhammadelsayed.bybike.activity.model.TripModel;
import com.muhammadelsayed.bybike.activity.model.User;
import com.muhammadelsayed.bybike.activity.network.OrderClient;
import com.muhammadelsayed.bybike.activity.network.RetrofitClientInstance;
import com.muhammadelsayed.bybike.activity.network.UserClient;
import com.muhammadelsayed.bybike.activity.utils.HistoryAdapter;
import com.muhammadelsayed.bybike.activity.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.muhammadelsayed.bybike.activity.ConfirmOrderActivity.currentOrder;
import static com.muhammadelsayed.bybike.activity.SplashActivity.currentUser;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";

    // vars
    private Context mContext;
    private HistoryModel historyModel;


    // widgets
    private Toolbar toolbar;
    private ListView tripsListView;
    private HistoryAdapter historyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.wtf(TAG, "onCreate() has been instantiated");
        super.onCreate(savedInstanceState);
        Utils.checkUserSession(HistoryActivity.this);
        setContentView(R.layout.activity_history);
        Log.d(TAG, "onCreate: started !!");

        setupWidgets();
        getHistory();



    }

    private void getHistory() {
        historyModel = new HistoryModel();

        UserClient service = RetrofitClientInstance.getRetrofitInstance()
                .create(UserClient.class);

        Log.d(TAG, "currentUser: " + currentUser);

        Call<HistoryModel> call = service.getHistory(currentUser.getUser());

        call.enqueue(new Callback<HistoryModel>() {
            @Override
            public void onResponse(Call<HistoryModel> call, Response<HistoryModel> response) {

                if(response.body() != null) {

                    historyModel = response.body();
                    Log.d(TAG, "onResponse: " + response.body());
                    Log.d(TAG, "historyModel: " + historyModel);

                    for (int i = 0; i < historyModel.getTrips().size(); i++) {
                        Log.d(TAG, "\n ID = " + historyModel.getTrips().get(i).getId() + "\n Rate = " + historyModel.getTrips().get(i).getRate());
                    }

                    historyAdapter = new HistoryAdapter(HistoryActivity.this, historyModel.getTrips());
                    tripsListView.setAdapter(historyAdapter);


                } else {
                    Log.d(TAG, "onResponse: RESPONSE BODY = " + response.body());
                }
            }

            @Override
            public void onFailure(Call<HistoryModel> call, Throwable t) {

                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
                Toast.makeText(HistoryActivity.this, "Failed", Toast.LENGTH_SHORT).show();

            }
        });


    }



    /******************** LAYOUT ********************/

    /**
     * sets up all the widgets in this library
     */
    private void setupWidgets() {
        Log.wtf(TAG, "setupWidgets() has been instantiated");

        mContext = getApplicationContext();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tripsListView = findViewById(R.id.trips_listview);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.wtf(TAG, "onOptionsItemSelected() has been instantiated");

        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);

    }
}
