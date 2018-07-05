package com.muhammadelsayed.bybike.activity.ProfileActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.muhammadelsayed.bybike.R;

public class EditEmailActivity extends AppCompatActivity {

    private static final String TAG = "EditEmailActivity";

    private Toolbar toolbar;
    private EditText mEmail;
    private Button mUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);


        // initializing activity widgets
        setupWidgets();

    }



    /**
     * sets up all activity widgets
     */
    private void setupWidgets() {
        Log.wtf(TAG, "setupWidgets() has been instantiated");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmail = findViewById(R.id.edit_email);
        mEmail.setText(getIntent().getStringExtra("email"));
        mUpdate = findViewById(R.id.btn_update_email);
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditEmailActivity.this, "To Be Implemented", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.wtf(TAG, "onOptionsItemSelected() has been instantiated");

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }


}
