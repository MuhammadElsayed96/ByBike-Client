package com.muhammadelsayed.bybike.activity.network;



import com.muhammadelsayed.bybike.activity.model.User;
import com.muhammadelsayed.bybike.activity.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface UserClient {

    @POST("user/login")
    Call<UserModel> loginUser(@Body User user);

    @POST("user/update")
    Call<UserModel> updateUser(@Body User user);

    @POST("user/update/password")
    Call<UserModel> updateUserPassword(@Body User user);



}
