package com.example.comp4521;

import android.app.Application;

public class GlobalVariable extends Application {
    private String UserID;    //User id in db
    private String UserName;     //User name
    private String UserEmail;         //User email

    //private static GlobalVariable singleton;
    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserEmail(String UserEmail) {
        this.UserEmail = UserEmail;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    /*public static GlobalVariable getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }*/
}
