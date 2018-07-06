package com.muhammadelsayed.bybike.activity.network;

import com.muhammadelsayed.bybike.activity.model.Order;
import com.muhammadelsayed.bybike.activity.model.OrderInfoModel;
import com.muhammadelsayed.bybike.activity.model.RateModel;
import com.muhammadelsayed.bybike.activity.model.RetroResponse;
import com.muhammadelsayed.bybike.activity.model.TripModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderClient {

    @POST("api/user/order/create")
    Call<RetroResponse> createOrder(@Body Order order);


    @POST("api/user/order/info")
    Call<OrderInfoModel> getOrderInfo(@Body TripModel tripModel);

    @POST("api/user/order/cancel")
    Call<RetroResponse> cancelOrder(@Body TripModel tripModel);

    @POST("api/user/rate")
    Call<RetroResponse> rateRider(@Body RateModel rateModel);
}
