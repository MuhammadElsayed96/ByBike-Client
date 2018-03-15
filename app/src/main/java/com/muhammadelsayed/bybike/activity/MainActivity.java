package com.muhammadelsayed.bybike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.muhammadelsayed.bybike.R;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        // initializing navigation view
        setUpNavigationView();


    }

    private void setUpNavigationView() {

        // Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // Check to see which item was clicked and perform appropriate action
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        Intent profile = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(profile);
                        break;
                    case R.id.nav_orders:

                        Intent orders = new Intent(MainActivity.this, OrdersActivity.class);
                        startActivity(orders);
                        break;
                    case R.id.nav_promo:
                        Intent promo = new Intent(MainActivity.this, PromoCodeActivity.class);
                        startActivity(promo);
                        break;
                    case R.id.nav_invite_friends:
                        Intent inviteFriends = new Intent(MainActivity.this, InviteFriendsActivity.class);
                        startActivity(inviteFriends);
                        break;

                    case R.id.nav_get_help:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, GetHelpActivity.class));
                        break;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        break;
                    case R.id.nav_contact_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                        break;
                }

                drawerLayout.closeDrawers();

                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.closeDrawer, R.string.openDrawer);


        // Setting he action bar Toggle to drawer layout
        drawerLayout.setDrawerListener(toggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        toggle.syncState();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.action_logout) {

            Toast.makeText(getApplication(), "Logged out!", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
