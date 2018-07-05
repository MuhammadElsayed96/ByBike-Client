package com.muhammadelsayed.bybike.activity.ProfileActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.MainActivity;
import com.muhammadelsayed.bybike.activity.fragment.LoginFragment;
import com.muhammadelsayed.bybike.activity.model.User;
import com.muhammadelsayed.bybike.activity.model.UserModel;
import com.muhammadelsayed.bybike.activity.network.RetrofitClientInstance;
import com.muhammadelsayed.bybike.activity.network.UserClient;
import com.muhammadelsayed.bybike.activity.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.muhammadelsayed.bybike.activity.fragment.LoginFragment.currentUser;

public class EditFirstnameActivity extends AppCompatActivity {

    private static final String TAG = "EditFirstnameActivity";

    private Toolbar toolbar;
    private EditText mFirstname;
    private Button mUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_firstname);
        Log.wtf(TAG, "onCreate() has been instantiated");


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

        mFirstname = findViewById(R.id.edit_firstname);
        mFirstname.setText(currentUser.getUser().getFirstname());

        mUpdate = findViewById(R.id.btn_update_firstname);
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(EditFirstnameActivity.this, "To Be Implemented", Toast.LENGTH_SHORT).show();
                updateUser();
            }
        });
    }

    private void updateUser() {
        Log.wtf(TAG, "updateUser() has been instantiated");

        if (checkValidation()) {
//
//            final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext(), R.style.ProgressDialogTheme);
//            progressDialog.setMessage("Updating...");
//            progressDialog.setCancelable(true);
//            progressDialog.show();

            UserClient service = RetrofitClientInstance.getRetrofitInstance()
                    .create(UserClient.class);

            String name = Utils.concatNames(mFirstname.getText().toString(),currentUser.getUser().getLastname());

            currentUser.getUser().setName(name);

            Call<UserModel> call = service.updateUser(currentUser.getUser());

            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(@NonNull Call<UserModel> call, Response<UserModel> response) {

                    if (response.body() != null) {

                        currentUser = response.body();
                        Log.d(TAG, "onResponse: " + response.body());
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {

                        Toast.makeText(getApplicationContext(), "I have no idea what's happening\nbut, something is terribly wrong !!", Toast.LENGTH_SHORT).show();

                    }

//                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {

//                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "network error !!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private boolean checkValidation() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.wtf(TAG, "onOptionsItemSelected() has been instantiated");

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }


}
