package com.example.beamagayver.pojo;

public class UserActivity implements Comparable<UserActivity>{

    private String uid;
    private boolean joined;
    private boolean created;
    private String postID;
    private String time;
    private String accountName;

    public UserActivity(){}
    public UserActivity( boolean created, String postID, String time) {
        this.created = created;
        this.postID = postID;
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int compareTo(UserActivity activity) {
        return time.compareTo(activity.time);
    }
}
