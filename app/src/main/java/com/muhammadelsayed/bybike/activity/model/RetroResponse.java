package com.muhammadelsayed.bybike.activity.model;

public class RetroResponse {
    private String message;
    private Order order;

    public RetroResponse(String message, Order order) {
        this.message = message;
        this.order = order;
    }

    public RetroResponse() {
    }


    @Override
    public String toString() {
        return "RetroResponse{" +
                "message='" + message + '\'' +
                ", order=" + order +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


}
