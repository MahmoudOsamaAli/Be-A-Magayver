package com.example.beamagayver.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationModel implements Parcelable {

    private String id;

    private String placeID;

    private String country;

    private String city;

    private String street;

    private double latitude;

    private double longitude;

    public LocationModel() {
    }

    private LocationModel(Parcel in) {
        id = in.readString();
        placeID = in.readString();
        country = in.readString();
        city = in.readString();
        street = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<LocationModel> CREATOR = new Creator<LocationModel>() {
        @Override
        public LocationModel createFromParcel(Parcel in) {
            return new LocationModel(in);
        }

        @Override
        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(placeID);
        parcel.writeString(country);
        parcel.writeString(city);
        parcel.writeString(street);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }

    public LocationModel(String id, String placeID, String country, String city, String street, double latitude, double longitude) {
        this.id = id;
        this.placeID = placeID;
        this.country = country;
        this.city = city;
        this.street = street;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
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
    public int describeContents() {
        return 0;
    }


}
