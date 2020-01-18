package com.example.beamagayver.pojo;

public class User {

    private String mName;
    private String mEmail;
    private String mUserType;

    public User(String mFName, String mLName, String mUserType) {
        this.mName = mFName;
        this.mEmail = mLName;
        this.mUserType = mUserType;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmUserType() {
        return mUserType;
    }

    public void setmUserType(String mUserType) {
        this.mUserType = mUserType;
    }
}
