package com.muhammadelsayed.bybike.activity.model;

import java.io.Serializable;

public class OrderInfoModel implements Serializable{
    private OrderInfo order;
    private Rider transporter;


    public OrderInfoModel(OrderInfo order, Rider transporter) {
        this.order = order;
        this.transporter = transporter;
    }

    public OrderInfoModel() {
    }


    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }

    public Rider getTransporter() {
        return transporter;
    }

    public void setTransporter(Rider transporter) {
        this.transporter = transporter;
    }

    @Override
    public String toString() {
        return "OrderInfoModel{" +
                "order=" + order +
                ", transporter=" + transporter +
                '}';
    }
}
