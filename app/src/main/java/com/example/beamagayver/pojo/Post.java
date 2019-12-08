package com.example.beamagayver.pojo;

public class Post {

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
