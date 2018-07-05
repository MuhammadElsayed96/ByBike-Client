package com.muhammadelsayed.bybike.activity.model;

public class TripModel {
    private String api_token;
    private String order;

    public TripModel(String api_token, String order) {
        this.api_token = api_token;
        this.order = order;
    }

    public TripModel() {
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "TripModel{" +
                "api_token='" + api_token + '\'' +
                ", order=" + order +
                '}';
    }
}
