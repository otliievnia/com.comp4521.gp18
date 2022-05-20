package com.example.comp4521.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class FavPostTest {

    @Test
    public void getFavPostID() {
        FavPost favpost = new FavPost();
        favpost.setFavPostID("123");
        assert(favpost.getFavPostID().equals("123"));
    }


    @Test
    public void getPostID() {
        FavPost favpost = new FavPost();
        favpost.setPostID("123");
        assert(favpost.getPostID().equals("123"));
    }


    @Test
    public void getUserID() {
        FavPost favpost = new FavPost();
        favpost.setUserID("123");
        assert(favpost.getUserID().equals("123"));
    }
}