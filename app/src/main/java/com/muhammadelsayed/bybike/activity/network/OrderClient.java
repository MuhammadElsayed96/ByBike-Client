package com.muhammadelsayed.bybike.activity.network;

import com.muhammadelsayed.bybike.activity.model.Order;

import retrofit2.Call;
import retrofit2.http.POST;

public interface OrderClient {

    @POST("user/order/create")
    Call<Void> createOrder(Order order);
}
