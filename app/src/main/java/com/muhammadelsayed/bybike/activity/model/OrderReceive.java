


package com.muhammadelsayed.bybike.activity.model;

public class OrderReceive {

    private int id;
    private int Total_cost;
    private String  Order_ID;
    private String Transporter_ID;
    private String created_at;
    private String updated_at;
    private Rider rider;

    public OrderReceive(int id, int total_cost, String order_ID, String transporter_ID, String created_at, String updated_at, Rider rider) {
        this.id = id;
        Total_cost = total_cost;
        Order_ID = order_ID;
        Transporter_ID = transporter_ID;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.rider = rider;
    }

    public OrderReceive() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal_cost() {
        return Total_cost;
    }

    public void setTotal_cost(int total_cost) {
        Total_cost = total_cost;
    }

    public String getOrder_ID() {
        return Order_ID;
    }

    public void setOrder_ID(String order_ID) {
        Order_ID = order_ID;
    }

    public String getTransporter_ID() {
        return Transporter_ID;
    }

    public void setTransporter_ID(String transporter_ID) {
        Transporter_ID = transporter_ID;
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

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    @Override
    public String toString() {
        return "OrderReceive{" +
                "id=" + id +
                ", Total_cost=" + Total_cost +
                ", Order_ID='" + Order_ID + '\'' +
                ", Transporter_ID='" + Transporter_ID + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", rider=" + rider +
                '}';
    }
}
