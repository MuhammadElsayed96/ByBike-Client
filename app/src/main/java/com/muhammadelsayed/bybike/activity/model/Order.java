package com.muhammadelsayed.bybike.activity.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Order implements Serializable {

    private int id;
    private String uuid;
    private int status;
    private String email;
    private String password;
    private String Sender_Lat;
    private String Sender_Lng;
    private String Receiver_lat;
    private String Receiver_lng;
    private String api_token;
    private String created_at;
    private String updated_at;
    private String user;

    public Order(int id, String uuid, int status, String email, String password, String sender_Lat, String sender_Lng, String receiver_lat, String receiver_lng, String api_token, String created_at, String updated_at, String user) {
        this.id = id;
        this.uuid = uuid;
        this.status = status;
        this.email = email;
        this.password = password;
        Sender_Lat = sender_Lat;
        Sender_Lng = sender_Lng;
        Receiver_lat = receiver_lat;
        Receiver_lng = receiver_lng;
        this.api_token = api_token;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user = user;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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


    public void setSenderLatLng(LatLng sender) {
        Sender_Lat = String.valueOf(sender.latitude);
        Sender_Lng = String.valueOf(sender.longitude);
    }

    public void setReceiverLatLng(LatLng receiver) {
        Receiver_lat = String.valueOf(receiver.latitude);
        Receiver_lng = String.valueOf(receiver.longitude);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", status=" + status +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", Sender_Lat='" + Sender_Lat + '\'' +
                ", Sender_Lng='" + Sender_Lng + '\'' +
                ", Receiver_lat='" + Receiver_lat + '\'' +
                ", Receiver_lng='" + Receiver_lng + '\'' +
                ", api_token='" + api_token + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
