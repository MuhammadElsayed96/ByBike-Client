package com.muhammadelsayed.bybike.activity.model;

public class RetroResponse {
    private String message;
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public RetroResponse() {
    }

    public RetroResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String toString() {
        return "RetroResponse{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
