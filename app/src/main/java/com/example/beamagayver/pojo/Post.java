package com.example.beamagayver.pojo;


public class Post implements Comparable<Post> {
    private String mPostID;
    private String mOwnerID;
    private String mOwnerName;
    private String mOwnerImage;
    private String mPostTime;
    private String mPostCaption;
    private String mCarDetails;
    private String mDuration;
    private String mStartDate;
    private String mStartTime;
    private JoinedModel mJoined;
    private LikesModel mLikes;
    private String mPrice;
    private String mPhoneNumber;
    private LocationModel mLocation;


    public Post() {
    }

    public Post(String name, String OwnerID, String ownerImage, String postTime
            , String caption, String carDetails, String duration, String startDate
            , String startTime, JoinedModel joined, LikesModel likes, String phoneNumber
            , LocationModel location , String price) {

        this.mOwnerName = name;
        this.mOwnerImage = ownerImage;
        this.mOwnerID = OwnerID;
        this.mPostTime = postTime;
        this.mPostCaption = caption;
        this.mCarDetails = carDetails;
        this.mDuration = duration;
        this.mStartDate = startDate;
        this.mStartTime = startTime;
        this.mJoined = joined;
        this.mLikes = likes;
        this.mPhoneNumber = phoneNumber;
        this.mLocation = location;
        this.mPrice = price;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public LocationModel getmLocation() {
        return mLocation;
    }

    public void setmLocation(LocationModel mLocation) {
        this.mLocation = mLocation;
    }

    public JoinedModel getmJoined() {
        return mJoined;
    }

    public void setmJoined(JoinedModel mJoined) {
        this.mJoined = mJoined;
    }

    public LikesModel getmLikes() {
        return mLikes;
    }

    public void setmLikes(LikesModel mLikes) {
        this.mLikes = mLikes;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmPostID() {
        return mPostID;
    }

    public void setmPostID(String mPostID) {
        this.mPostID = mPostID;
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

    public String getmCarDetails() {
        return mCarDetails;
    }

    public void setmCarDetails(String mCarDetails) {
        this.mCarDetails = mCarDetails;
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

    @Override
    public int compareTo(Post o) {

        return mPostTime.compareTo(o.mPostTime);
    }
}
