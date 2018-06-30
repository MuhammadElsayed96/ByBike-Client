package com.muhammadelsayed.bybike.activity.network;

import com.muhammadelsayed.bybike.activity.model.Order;
import com.muhammadelsayed.bybike.activity.model.RetroResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderClient {

    @POST("user/order/create")
    Call<RetroResponse> createOrder(@Body Order order);
}
