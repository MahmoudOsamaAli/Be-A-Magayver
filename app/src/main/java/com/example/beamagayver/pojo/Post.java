package com.example.beamagayver.pojo;

public class Post {
    private String mOwnerID;
    private String mOwnerName;
    private String mOwnerImage;
    private String mPostTime;
    private String mPostCaption;
    private String mCarName;
    private String mCarModel;
    private String mCarColor;
    private String mDuration;
    private String mStartDate;
    private String mStartTime;
    private int ImageLink;
    private String mName;
    private String mTime;
    private String mPostContent;

    public Post(int imageLink, String mName, String mTime, String mPostContent) {
        ImageLink = imageLink;
        this.mName = mName;
        this.mTime = mTime;
        this.mPostContent = mPostContent;
    }

    public Post(String name,String OwnerID, String ownerImage, String postTime, String caption, String carDetails, String duration, String startDate, String startTime) {
        this.mOwnerName = name;
        this.mOwnerImage = ownerImage;
        this.mOwnerID = OwnerID;
        this.mPostTime = postTime;
        this.mPostCaption = caption;
        this.mCarModel = carDetails;
        this.mDuration = duration;
        this.mStartDate = startDate;
        this.mStartTime = startTime;
    }

    public String getmOwnerID() {
        return mOwnerID;
    }

    public void setmOwnerID(String mOwnerID) {
        this.mOwnerID = mOwnerID;
    }

    public String getmOwnerName() {
        return mOwnerName;
    }

    public String getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public void setmOwnerName(String mOwnerName) {
        this.mOwnerName = mOwnerName;
    }

    public String getmOwnerImage() {
        return mOwnerImage;
    }

    public void setmOwnerImage(String mOwnerImage) {
        this.mOwnerImage = mOwnerImage;
    }

    public String getmPostTime() {
        return mPostTime;
    }

    public void setmPostTime(String mPostTime) {
        this.mPostTime = mPostTime;
    }

    public String getmPostCaption() {
        return mPostCaption;
    }

    public void setmPostCaption(String mPostCaption) {
        this.mPostCaption = mPostCaption;
    }

    public String getmCarName() {
        return mCarName;
    }

    public void setmCarName(String mCarName) {
        this.mCarName = mCarName;
    }

    public String getmCarModel() {
        return mCarModel;
    }

    public void setmCarModel(String mCarModel) {
        this.mCarModel = mCarModel;
    }

    public String getmCarColor() {
        return mCarColor;
    }

    public void setmCarColor(String mCarColor) {
        this.mCarColor = mCarColor;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getmStartDate() {
        return mStartDate;
    }

    public void setmStartDate(String mStartDate) {
        this.mStartDate = mStartDate;
    }

    public int getImageLink() {
        return ImageLink;
    }

    public void setImageLink(int imageLink) {
        ImageLink = imageLink;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmPostContent() {
        return mPostContent;
    }

    public void setmPostContent(String mPostContent) {
        this.mPostContent = mPostContent;
    }
}
