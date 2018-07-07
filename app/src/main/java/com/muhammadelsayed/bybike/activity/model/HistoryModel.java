package com.muhammadelsayed.bybike.activity.model;

import java.util.Arrays;
import java.util.List;

public class HistoryModel {
    private List<Trip> trips;

    public HistoryModel(List<Trip> trips) {
        this.trips = trips;
    }

    public HistoryModel() {
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public String toString() {
        return "HistoryModel{" +
                "trips=" + trips +
                '}';
    }
}
