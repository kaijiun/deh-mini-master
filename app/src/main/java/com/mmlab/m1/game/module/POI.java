package com.mmlab.m1.game.module;

public class POI {


    public POI(int POI_id, String POI_title, double latitude, double longitude) {
        this.POI_id = POI_id;
        this.POI_title = POI_title;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getPOI_id() {
        return POI_id;
    }

    public void setPOI_id(int POI_id) {
        this.POI_id = POI_id;
    }

    public String getPOI_title() {
        return POI_title;
    }

    public void setPOI_title(String POI_title) {
        this.POI_title = POI_title;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    int POI_id;
    String POI_title;
    double latitude;
    double longitude;
}
