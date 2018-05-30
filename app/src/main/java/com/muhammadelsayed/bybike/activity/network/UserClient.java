package com.muhammadelsayed.bybike.activity.network;



import com.muhammadelsayed.bybike.activity.model.User;
import com.muhammadelsayed.bybike.activity.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface UserClient {

    @POST("user/login")
    Call<UserModel> loginUser(@Body User user);

//I!038cK@FduzwJSsgV#SBgxV46K)No6J)0fin&kUv889c$c$!Q

}
