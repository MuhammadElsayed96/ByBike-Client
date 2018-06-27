package com.muhammadelsayed.bybike.activity.ProfileActivities;

import android.content.Intent;
import android.os.Bundle;
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
import com.muhammadelsayed.bybike.activity.model.UserModel;
import com.muhammadelsayed.bybike.activity.network.RetrofitClientInstance;
import com.muhammadelsayed.bybike.activity.network.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.muhammadelsayed.bybike.activity.fragment.LoginFragment.currentUser;

public class EditPasswordActivity extends AppCompatActivity {

    private static final String TAG = "EditPasswordActivity";

    private String error = "";


    private Toolbar toolbar;
    private EditText mNewPassword, mConfirmPassword;
    private Button mUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);


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

        mNewPassword = findViewById(R.id.edit_new_password);
        mConfirmPassword = findViewById(R.id.edit_confirm_password);

        mUpdate = findViewById(R.id.btn_update_password);
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(EditLastnameActivity.this, "To Be Implemented", Toast.LENGTH_SHORT).show();
                updateUser();
            }
        });
    }

    private void updateUser() {

        if (checkValidation()) {
//
//            final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext(), R.style.ProgressDialogTheme);
//            progressDialog.setMessage("Updating...");
//            progressDialog.setCancelable(true);
//            progressDialog.show();

            UserClient service = RetrofitClientInstance.getRetrofitInstance()
                    .create(UserClient.class);

            currentUser.getUser().setPassword(mNewPassword.getText().toString());

            Call<UserModel> call = service.updateUserPassword(currentUser.getUser());

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
        } else {
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkValidation() {

        if (mNewPassword.getText().toString().length() < 6) {
            error = "Password must be at least 6 characters";
            return false;
        }

        if (!mNewPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
            error = "The two passwords do not match";
            return false;
        }


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }


}
