package com.muhammadelsayed.bybike.activity.model;

public class Transportation {

    private String transType;
    private int transImg;
    private String transCost;
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

    public String getTransCost() {
        return transCost;
    }

    public void setTransCost(String transCost) {
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

    public Transportation(String transType, int transImg, String transCost, String transDistance) {
        this.transType = transType;
        this.transImg = transImg;
        this.transCost = transCost;
        this.transDistance = transDistance;
    }
}
