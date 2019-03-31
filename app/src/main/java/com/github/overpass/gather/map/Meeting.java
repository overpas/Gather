package com.github.overpass.gather.map;

import java.util.Date;

public class Meeting {

    private String name;
    private double latitude;
    private double longitude;
    private Date date;

    public Meeting(String name, double latitude, double longitude, Date date) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
