package com.example.comp4521.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.Map;

public class FavPost implements Serializable {

    private String postID; // Focus on stray posts
    private String favPostID;
    private String userID; // firebase assigned ID
    private Long createdDateTime;


    public FavPost() {
    }

    public String getFavPostID() {
        return favPostID;
    }

    public void setFavPostID(String favPostID) {
        this.favPostID = favPostID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Exclude
    public Long getCreatedDateTimeLong() {
        return createdDateTime;
    }

    public Map<String, String> getCreatedDateTime() {
        return ServerValue.TIMESTAMP;
    }

    public void setCreatedDateTime(Long createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

}
