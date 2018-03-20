package com.muhammadelsayed.bybike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.muhammadelsayed.bybike.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Toolbar");        setSupportActionBar(toolbar);

        TextView dummy = findViewById(R.id.dummy);
        dummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}