package com.muhammadelsayed.bybike.activity.model;

public class RateModel {


    private String api_token;
    private String rider;
    private String order;
    private int rate;

    public RateModel(String api_token, String rider, String order, int rate) {
        this.api_token = api_token;
        this.rider = rider;
        this.order = order;
        this.rate = rate;
    }

    public RateModel() {
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "RateModel{" +
                "api_token='" + api_token + '\'' +
                ", rider='" + rider + '\'' +
                ", order='" + order + '\'' +
                ", rate=" + rate +
                '}';
    }
}
