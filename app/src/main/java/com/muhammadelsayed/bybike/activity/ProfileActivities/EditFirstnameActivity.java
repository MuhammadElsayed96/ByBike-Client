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

public class EditFirstnameActivity extends AppCompatActivity {

    private static final String TAG = "EditFirstnameActivity";

    private Toolbar toolbar;
    private EditText mFirstname;
    private Button mUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_firstname);


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

        mFirstname = findViewById(R.id.edit_firstname);
        mFirstname.setText(getIntent().getStringExtra("firstname"));
        mUpdate = findViewById(R.id.btn_update_firstname);
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditFirstnameActivity.this, "To Be Implemented", Toast.LENGTH_SHORT).show();
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
