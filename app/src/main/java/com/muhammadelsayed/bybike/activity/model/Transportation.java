package com.muhammadelsayed.bybike.activity.model;

import com.google.gson.annotations.SerializedName;

public class Transportation {

    private int id;
    private String created_at;
    private String updated_at;
    @SerializedName("type")
    private String transType;
    @SerializedName("price")
    private int transCost;
    private int transImg;
    private String transDistance;




    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public int getTransImg() {
        return transImg;
    }

    public void setTransImg(int transImg) {
        this.transImg = transImg;
    }

    public int getTransCost() {
        return transCost;
    }

    public void setTransCost(int transCost) {
        this.transCost = transCost;
    }

    public String getTransDistance() {
        return transDistance;
    }

    public void setTransDistance(String transDistance) {
        this.transDistance = transDistance;
    }

    public Transportation() {
    }

    public Transportation(String transType, int transImg, int transCost, String transDistance) {
        this.transType = transType;
        this.transImg = transImg;
        this.transCost = transCost;
        this.transDistance = transDistance;
    }
}
