package com.github.overpass.gather.map;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Meeting {

    private String name;
    private double latitude;
    private double longitude;
    private Date date;
    private int type;
    private List<String> photos;
    private int maxPeople;
    private boolean isPrivate;

    public Meeting() {
    }

    public Meeting(String name,
                   double latitude,
                   double longitude,
                   Date date,
                   int type,
                   int maxPeople, boolean isPrivate) {
        this(name, latitude, longitude, date, type, new ArrayList<>(), maxPeople, isPrivate);
    }

    public Meeting(String name,
                   double latitude,
                   double longitude,
                   Date date,
                   int type,
                   List<String> photos,
                   int maxPeople, boolean isPrivate) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.type = type;
        this.photos = photos;
        this.maxPeople = maxPeople;
        this.isPrivate = isPrivate;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
}
