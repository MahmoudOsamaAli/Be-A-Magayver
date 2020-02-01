package com.example.beamagayver.pojo;

import android.location.Location;

import com.example.beamagayver.Utilities.NumberUtils;
import com.example.beamagayver.view.activities.HomeActivity;

import java.io.Serializable;

public class LocationModel implements Serializable ,Comparable<LocationModel>{

    private String country;
    private String city;
    private String street;
    private String distance;
    private double latitude;
    private double longitude;

    public LocationModel() {
        //no argument constructor needed
    }

    public LocationModel(String mCountry, String mCity, String mStreet, double log, double lat) {
        this.latitude = lat;
        this.longitude = log;
        this.country = mCountry;
        this.city = mCity;
        this.street = mStreet;
        distance = String.valueOf(NumberUtils.distance(latitude , log , new Location(HomeActivity.getLocation())));
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    @Override
    public int compareTo(LocationModel model) {
        return distance.compareTo(model.distance);
    }
}
