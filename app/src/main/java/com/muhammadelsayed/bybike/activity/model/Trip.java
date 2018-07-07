package com.muhammadelsayed.bybike.activity.model;

public class Trip {

    private int id;
    private String uuid;
    private int status;
    private String Sender_Lat;
    private String Sender_Lng;
    private String Receiver_lat;
    private String Receiver_lng;
    private String user;
    private String created_at;
    private String updated_at;
    private OrderReceive order_receive;
    private Rate rate;

    public Trip(int id, String uuid, int status, String sender_Lat, String sender_Lng, String receiver_lat, String receiver_lng, String user, String created_at, String updated_at, OrderReceive order_receive, com.muhammadelsayed.bybike.activity.model.Rate rate) {
        this.id = id;
        this.uuid = uuid;
        this.status = status;
        Sender_Lat = sender_Lat;
        Sender_Lng = sender_Lng;
        Receiver_lat = receiver_lat;
        Receiver_lng = receiver_lng;
        this.user = user;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.order_receive = order_receive;
        rate = rate;
    }

    public Trip() {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public OrderReceive getOrder_receive() {
        return order_receive;
    }

    public void setOrder_receive(OrderReceive order_receive) {
        this.order_receive = order_receive;
    }

    public com.muhammadelsayed.bybike.activity.model.Rate getRate() {
        return rate;
    }

    public void setRate(com.muhammadelsayed.bybike.activity.model.Rate rate) {
        rate = rate;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", status=" + status +
                ", Sender_Lat='" + Sender_Lat + '\'' +
                ", Sender_Lng='" + Sender_Lng + '\'' +
                ", Receiver_lat='" + Receiver_lat + '\'' +
                ", Receiver_lng='" + Receiver_lng + '\'' +
                ", user='" + user + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", order_receive=" + order_receive +
                ", Rate='" + rate + '\'' +
                '}';
    }
}


