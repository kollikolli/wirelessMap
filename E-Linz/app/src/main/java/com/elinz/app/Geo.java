package com.elinz.app;

/**
 * Created by hari on 13.06.14.
 */
public class Geo {

    public Geo(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    private double longitude;
    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
