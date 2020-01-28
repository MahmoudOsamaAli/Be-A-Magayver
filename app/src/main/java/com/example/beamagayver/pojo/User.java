package com.example.beamagayver.pojo;

public class User {

    private String Uid;
    private String name;
    private String email;
    private String photoUri;
    private String userType;
    private String country;
    private String phoneNumber;


    public User(String id , String mFName, String mEmail, String photo , String mUserType) {
        this.Uid = id;
        this.name = mFName;
        this.email = mEmail;
        this.photoUri = photo;
        this.userType = mUserType;
    }
    public User(){}

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
