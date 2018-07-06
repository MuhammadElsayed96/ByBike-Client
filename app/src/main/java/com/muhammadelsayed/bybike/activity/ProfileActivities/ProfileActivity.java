package com.muhammadelsayed.bybike.activity.ProfileActivities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.muhammadelsayed.bybike.R;
import com.muhammadelsayed.bybike.activity.WaitingActivity;
import com.muhammadelsayed.bybike.activity.model.UserModel;
import com.muhammadelsayed.bybike.activity.network.RetrofitClientInstance;
import com.muhammadelsayed.bybike.activity.network.UserClient;
import com.muhammadelsayed.bybike.activity.utils.RealPathUtil;
import com.muhammadelsayed.bybike.activity.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.muhammadelsayed.bybike.activity.SplashActivity.currentUser;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final int INTENT_REQUEST_CODE = 100;
    private static final int REQUEST_STORAGE_PERMISSION = 200;

    private LinearLayout mLlEditLastname, mLlEditFirstname, mLlEditPhone, mLlEditEmail, mLlEditPassword;
    private RelativeLayout mRlProfileImage;
    private TextView mFirstname, mLastname, mPhone, mEmail, mPassword;
    private ImageView mProfileImage;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.wtf(TAG, "onCreate() has been instantiated");

        super.onCreate(savedInstanceState);
        Utils.checkUserSession(ProfileActivity.this);

        setContentView(R.layout.activity_profile);

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

        mLlEditFirstname = findViewById(R.id.firstname_layout);
        mLlEditLastname = findViewById(R.id.lastname_layout);
        mLlEditPassword = findViewById(R.id.password_layout);
        mLlEditPhone = findViewById(R.id.phone_layout);
        mRlProfileImage = findViewById(R.id.profile_image_layout);
        mLlEditFirstname.setOnClickListener(mOnLlEditFirstnameClickListener);
        mLlEditLastname.setOnClickListener(mOnLlEditLastnameClickListener);
        mLlEditPassword.setOnClickListener(mOnLlEditPasswordClickListener);
        mLlEditPhone.setOnClickListener(mOnLlEditPhoneClickListener);
        mRlProfileImage.setOnClickListener(mOnRlProfileImageClickListener);


        // filling profile TextViews with current user's info
        mFirstname = findViewById(R.id.firstname_info);
        mFirstname.setText(currentUser.getUser().getFirstname());

        mLastname = findViewById(R.id.lastname_info);
        mLastname.setText(currentUser.getUser().getLastname());

        mPhone = findViewById(R.id.phone_info);
        mPhone.setText(currentUser.getUser().getPhone());

        mEmail = findViewById(R.id.email_info);
        mEmail.setText(currentUser.getUser().getEmail());

        mPassword = findViewById(R.id.password_info);
        mPassword.setText("********");

        mProfileImage = findViewById(R.id.profile_image);
        Picasso.get()
                .load(RetrofitClientInstance.BASE_URL + currentUser.getUser().getImage())
                .error(R.mipmap.icon_launcher)
                .into(mProfileImage);
    }

    // OnClick Listeners
    private LinearLayout.OnClickListener mOnLlEditFirstnameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.wtf(TAG, "mOnLlEditFirstnameClickListener() has been instantiated");

            Intent firstnameIntent = new Intent(getApplicationContext(), EditFirstnameActivity.class);
            firstnameIntent.putExtra("firstname", mFirstname.getText());
            firstnameIntent.putExtra("current_user", currentUser);
            startActivity(firstnameIntent);
        }
    };
    private LinearLayout.OnClickListener mOnLlEditLastnameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.wtf(TAG, "mOnLlEditLastnameClickListener() has been instantiated");

            Intent lastnameIntent = new Intent(getApplicationContext(), EditLastnameActivity.class);
            lastnameIntent.putExtra("lastname", mLastname.getText());
            startActivity(lastnameIntent);
        }
    };
    private LinearLayout.OnClickListener mOnLlEditPhoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.wtf(TAG, "mOnLlEditPhoneClickListener() has been instantiated");

            Intent phoneIntent = new Intent(getApplicationContext(), EditPhoneActivity.class);
            phoneIntent.putExtra("phone", mPhone.getText());
            startActivity(phoneIntent);
        }
    };
    private LinearLayout.OnClickListener mOnLlEditPasswordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.wtf(TAG, "mOnLlEditPasswordClickListener() has been instantiated");

            startActivity(new Intent(getApplicationContext(), EditPasswordActivity.class));
        }
    };

    private RelativeLayout.OnClickListener mOnRlProfileImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.wtf(TAG, "mOnRlProfileImageClickListener() has been instantiated");

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");


            if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                }
            }
            else
            {
                try {
                    startActivityForResult(intent, INTENT_REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    Log.d(TAG, "onClick: ActivityNotFoundException !!!!!!!!!!!");
                    e.printStackTrace();
                }
            }

        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200 :

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");

                    try {
                        startActivityForResult(intent, INTENT_REQUEST_CODE);
                    } catch (ActivityNotFoundException e) {
                        Log.d(TAG, "onClick: ActivityNotFoundException !!!!!!!!!!!");
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.wtf(TAG, "onActivityResult() has been instantiated");

        if (requestCode == INTENT_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                Log.d(TAG, "onActivityResult: ");

                Uri uri = data.getData();

                String path = RealPathUtil.getRealPath(getApplicationContext(), uri);
                if (path != null) {

                    File imageFile = new File(RealPathUtil.getRealPath(getApplicationContext(), uri));

                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);

                    MultipartBody.Part body = MultipartBody.Part.createFormData("photo", imageFile.getName(), requestFile);
                    RequestBody token = RequestBody.create(okhttp3.MultipartBody.FORM, currentUser.getUser().getApi_token());

                    UserClient service = RetrofitClientInstance.getRetrofitInstance()
                            .create(UserClient.class);

                    Call<UserModel> call = service.updateUserProfileImage(token, body);

                    call.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, retrofit2.Response<UserModel> response) {

                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    currentUser.setUser(response.body().getUser());
                                    Log.d(TAG, "onResponse: " + response.body());
                                    Picasso.get()
                                            .load(RetrofitClientInstance.BASE_URL + response.body().getUser().getImage())
                                            .error(R.mipmap.icon_launcher)
                                            .into(mProfileImage);
                                    Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d(TAG, "onResponse: RESPONSE BODY = " + response.body());
                                }
                            } else {
                                Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {

                            Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                            Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    Toast.makeText(this, "This image cannot be selected", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.wtf(TAG, "onOptionsItemSelected() has been instantiated");

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}
