package com.example.weatherapp.models;

import com.google.gson.annotations.SerializedName;

public class Coordinate {
    @SerializedName("lon")
    private String lon;
    @SerializedName("lat")
    private String lat;

    public Coordinate(String lon, String lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }
}
