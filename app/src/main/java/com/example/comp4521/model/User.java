package com.example.comp4521.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private String userId;
    private String email; // firebase assigned ID
    private String displayName;
    private Long createdDateTime;
    private Long updatedDateTime;

    public User() {
    }

    public String getName() {
        return displayName;
    }

    public void setName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return email;
    }

    public void setUserEmail(String email) {
        this.email = email;
    }

    @Exclude
    public Long getCreatedDateTimeLong() {
        return createdDateTime;
    }

    public Map<String, String> getCreatedDateTime() {
        return ServerValue.TIMESTAMP;
    }

    public Map<String, String> getUpdatedDateTime() {
        return ServerValue.TIMESTAMP;
    }

    public Long getUpdatedDateTimeLong() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Long updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public void setCreatedDateTime(Long createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

}
