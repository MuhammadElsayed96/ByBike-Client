package com.muhammadelsayed.bybike.activity.ProfileActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.muhammadelsayed.bybike.R;

public class EditPhoneActivity extends AppCompatActivity {

    private static final String TAG = "EditPhoneActivity";

    private Toolbar toolbar;
    private EditText mPhone;
    private Button mUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);


        // initializing activity widgets
        setupWidgets();

    }



    /**
     * sets up all activity widgets
     */
    private void setupWidgets() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPhone = findViewById(R.id.edit_phone);
        mUpdate = findViewById(R.id.btn_update_phone);
        
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditPhoneActivity.this, "To Be Implemented", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }


}
