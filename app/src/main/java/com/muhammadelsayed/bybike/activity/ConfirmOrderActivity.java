package com.muhammadelsayed.bybike.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.muhammadelsayed.bybike.R;

public class ConfirmOrderActivity extends AppCompatActivity {


    private static final String TAG = "ConfirmOrderActivity";

    // vars
    private Context mContext;

    // widgets
    private Toolbar toolbar;
    private TextView placeFrom, placeTo, totalCost;
    private EditText notes;
    private Spinner spinnerPayment;
    private Button confirmOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Log.d(TAG, "onCreate: started !!");

        setupWidgets();

    }


    /******************** LAYOUT ********************/

    /**
     * sets up all the widgets in this library
     */
    private void setupWidgets() {

        mContext = getApplicationContext();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        placeFrom = findViewById(R.id.placeFrom);
        placeTo = findViewById(R.id.placeTo);
        totalCost = findViewById(R.id.totalCost);
        notes = findViewById(R.id.notes);
        spinnerPayment = findViewById(R.id.spinnerPayment);
        confirmOrder = findViewById(R.id.confirmOrder);

        if (getIntent().hasExtra("placeFrom") && getIntent().hasExtra("placeTo") && getIntent().hasExtra("totalCost")) {

            placeFrom.setText(getIntent().getStringExtra("placeFrom"));
            placeTo.setText(getIntent().getStringExtra("placeTo"));
            totalCost.setText(getIntent().getStringExtra("totalCost"));

        }

        String[] payments = {"Cash", "Fawry"};

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,   android.R.layout.simple_spinner_item, payments);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerPayment.setAdapter(spinnerArrayAdapter);

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
