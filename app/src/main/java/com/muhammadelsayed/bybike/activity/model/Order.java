package com.muhammadelsayed.bybike.activity.model;

import com.google.maps.model.LatLng;

public class Order {

    private String email;
    private String password;
    private String Sender_Lat;
    private String Sender_Lng;
    private String Receiver_lat;
    private String Receiver_lng;
    private String api_token;


    public Order(String email, String password, String sender_Lat, String sender_Lng, String receiver_lat, String receiver_lng, String api_token) {
        this.email = email;
        this.password = password;
        Sender_Lat = sender_Lat;
        Sender_Lng = sender_Lng;
        Receiver_lat = receiver_lat;
        Receiver_lng = receiver_lng;
        this.api_token = api_token;
    }

    public Order() {
    }


    @Override
    public String toString() {
        return "Order{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", Sender_Lat='" + Sender_Lat + '\'' +
                ", Sender_Lng='" + Sender_Lng + '\'' +
                ", Receiver_lat='" + Receiver_lat + '\'' +
                ", Receiver_lng='" + Receiver_lng + '\'' +
                ", api_token='" + api_token + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSender_Lat() {
        return Sender_Lat;
    }

    public void setSender_Lat(String sender_Lat) {
        Sender_Lat = sender_Lat;
    }

    public String getSender_Lng() {
        return Sender_Lng;
    }

    public void setSender_Lng(String sender_Lng) {
        Sender_Lng = sender_Lng;
    }

    public String getReceiver_lat() {
        return Receiver_lat;
    }

    public void setReceiver_lat(String receiver_lat) {
        Receiver_lat = receiver_lat;
    }

    public String getReceiver_lng() {
        return Receiver_lng;
    }

    public void setReceiver_lng(String receiver_lng) {
        Receiver_lng = receiver_lng;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }
}
