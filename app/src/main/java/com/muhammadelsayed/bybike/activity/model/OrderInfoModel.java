package com.muhammadelsayed.bybike.activity.model;

import java.io.Serializable;

public class OrderInfoModel implements Serializable{
    private Order order;
    private Rider transporter;


    public OrderInfoModel(Order order, Rider transporter) {
        this.order = order;
        this.transporter = transporter;
    }

    public OrderInfoModel() {
    }


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
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
