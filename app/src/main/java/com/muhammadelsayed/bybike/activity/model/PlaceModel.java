package com.muhammadelsayed.bybike.activity.model;

public class PlaceModel {

    private String fullText;
    private String primaryText;
    private String secondaryText;
    private String placeID;

    public PlaceModel() {
    }

    public PlaceModel(String fullText, String primaryText, String secondaryText, String placeID) {
        this.fullText = fullText;
        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
        this.placeID = placeID;
    }

    @Override
    public String toString() {
        return "PlaceModel{" +
                "fullText='" + fullText + '\'' +
                ", primaryText='" + primaryText + '\'' +
                ", secondaryText='" + secondaryText + '\'' +
                ", placeID='" + placeID + '\'' +
                '}';
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }
}
