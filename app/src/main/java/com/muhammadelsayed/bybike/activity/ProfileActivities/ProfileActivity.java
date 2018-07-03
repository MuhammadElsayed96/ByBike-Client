package com.muhammadelsayed.bybike.activity.ProfileActivities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import com.muhammadelsayed.bybike.activity.model.UserModel;
import com.muhammadelsayed.bybike.activity.network.RetrofitClientInstance;
import com.muhammadelsayed.bybike.activity.network.UserClient;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.muhammadelsayed.bybike.activity.fragment.LoginFragment.currentUser;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final int INTENT_REQUEST_CODE = 100;

    private LinearLayout mLlEditLastname, mLlEditFirstname, mLlEditPhone, mLlEditEmail, mLlEditPassword;
    private RelativeLayout mRlProfileImage;
    private TextView mFirstname, mLastname, mPhone, mEmail, mPassword;
    private ImageView mProfileImage;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
            Intent firstnameIntent = new Intent(getApplicationContext(), EditFirstnameActivity.class);
            firstnameIntent.putExtra("firstname", mFirstname.getText());
            firstnameIntent.putExtra("current_user", currentUser);
            startActivity(firstnameIntent);
        }
    };
    private LinearLayout.OnClickListener mOnLlEditLastnameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent lastnameIntent = new Intent(getApplicationContext(), EditLastnameActivity.class);
            lastnameIntent.putExtra("lastname", mLastname.getText());
            startActivity(lastnameIntent);
        }
    };
    private LinearLayout.OnClickListener mOnLlEditPhoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent phoneIntent = new Intent(getApplicationContext(), EditPhoneActivity.class);
            phoneIntent.putExtra("phone", mPhone.getText());
            startActivity(phoneIntent);
        }
    };
    private LinearLayout.OnClickListener mOnLlEditPasswordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), EditPasswordActivity.class));
        }
    };

    private RelativeLayout.OnClickListener mOnRlProfileImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            try {
                startActivityForResult(intent, INTENT_REQUEST_CODE);

            } catch (ActivityNotFoundException e) {

                e.printStackTrace();
            }

        }
    };


    /**
     * Gets the real file path of the Uri because the built in method 'uri.getPath' is truly fucked.
     * Wasted an entire day to do only that :'(
     * I hate my life :)
     *
     * ref : https://stackoverflow.com/questions/2789276/android-get-real-path-by-uri-getpath
     *
     * @param uri The uri of the file.
     * @return the real path of that uri
     */
    private String getRealPathFromURI(Uri uri) {
        String filePath = "";
        if (uri.getHost().contains("com.android.providers.media")) {
            // Image pick from recent
            String wholeID = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                wholeID = DocumentsContract.getDocumentId(uri);
            }

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        } else {
            // image pick from gallery
            return  getRealPathFromURI(uri);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTENT_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                Uri uri = data.getData();
                Log.d(TAG, "onActivityResult: URI = " + uri);

                File imageFile = new File(getRealPathFromURI(uri));

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
                            if(response.body() != null) {
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

                        Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
                        Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                    }
                });



            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}
