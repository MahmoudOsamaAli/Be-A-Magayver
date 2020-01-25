package com.example.beamagayver.pojo;
public class LocationModel {

    private String country;
    private String city;
    private String street;
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


}
