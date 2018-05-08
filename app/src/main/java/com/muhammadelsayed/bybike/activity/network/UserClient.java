package com.muhammadelsayed.bybike.activity.network;



import com.muhammadelsayed.bybike.activity.model.User;
import com.muhammadelsayed.bybike.activity.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface UserClient {

    @POST("api/user/login")
    Call<UserModel> loginUser(@Body User user);

}
