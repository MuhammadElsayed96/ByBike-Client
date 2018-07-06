package com.muhammadelsayed.bybike.activity.model;

import java.util.Arrays;

public class HistoryModel {
    private Trip[] trips;

    public HistoryModel(Trip[] trips) {
        this.trips = trips;
    }

    public HistoryModel() {
    }

    public Trip[] getTrips() {
        return trips;
    }

    public void setTrips(Trip[] trips) {
        this.trips = trips;
    }

    @Override
    public String toString() {
        return "HistoryModel{" +
                "trips=" + Arrays.toString(trips) +
                '}';
    }
}
