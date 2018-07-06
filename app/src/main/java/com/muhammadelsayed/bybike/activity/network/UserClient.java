package com.muhammadelsayed.bybike.activity.network;

import com.muhammadelsayed.bybike.activity.model.HistoryModel;
import com.muhammadelsayed.bybike.activity.model.SignupResponse;
import com.muhammadelsayed.bybike.activity.model.User;
import com.muhammadelsayed.bybike.activity.model.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface UserClient {

    @POST("api/user/login")
    Call<UserModel> loginUser(@Body User user);

    @POST("api/user")
    Call<SignupResponse> signupUser(@Body User user);

    @POST("api/user/update")
    Call<UserModel> updateUser(@Body User user);

    @Multipart
    @POST("api/user/update")
    Call<UserModel> updateUserProfileImage(@Part("api_token") RequestBody api_token, @Part MultipartBody.Part image);

    @POST("api/user/update/password")
    Call<UserModel> updateUserPassword(@Body User user);

    @POST("api/user/info")
    Call<UserModel> getUserInfo(@Body User user);

    @POST("api/user/trip")
    Call<HistoryModel> getHistory(@Body String api_token);

}
