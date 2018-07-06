package com.muhammadelsayed.bybike.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.utils.Utils;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";

    // vars
    private Context mContext;

    // widgets
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.wtf(TAG, "onCreate() has been instantiated");
        super.onCreate(savedInstanceState);
        Utils.checkUserSession(HistoryActivity.this);

        setContentView(R.layout.activity_history);
        Log.d(TAG, "onCreate: started !!");


        setupWidgets();

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
