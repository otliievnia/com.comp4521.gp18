package com.example.comp4521.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PostTest {

    @Test
    public void getPostID() {
        Post post = new Post();
        post.setPostID("123");
        assert(post.getPostID().equals("123"));
    }

    @Test
    public void getAccountID() {
        Post post = new Post();
        post.setAccountID("123");
        assert(post.getAccountID().equals("123"));
    }

    @Test
    public void getAnimalType() {
        Post post = new Post();
        post.setAnimalType("Dog");
        assert(post.getAnimalType().equals("Dog"));
    }

    @Test
    public void getLocation() {
        Post post = new Post();
        post.setLocation("HKUST");
        assert(post.getLocation().equals("HKUST"));
    }

    @Test
    public void getGpsLatitude() {
        Post post = new Post();
        post.setGpsLatitude(12.123124);
        assert(post.getGpsLatitude() == 12.123124);
    }

    @Test
    public void getGpsLongitude() {
        Post post = new Post();
        post.setGpsLongitude(12.123124);
        assert(post.getGpsLongitude() == 12.123124);
    }

    @Test
    public void getBreed() {
        Post post = new Post();
        post.setBreed("bulldog");
        assert(post.getBreed().equals("bulldog"));
    }

    @Test
    public void getMobile() {
        Post post = new Post();
        post.setMobile("65812882");
        assert(post.getMobile().equals("65812882"));
    }

    @Test
    public void getSex() {
        Post post = new Post();
        post.setSex("Female");
        assert(post.getSex().equals("Female"));
    }

    @Test
    public void getName() {
        Post post = new Post();
        post.setName("Amber");
        assert(post.getName().equals("Amber"));
    }

    @Test
    public void getDescriptions() {
        Post post = new Post();
        post.setDescriptions("description");
        assert(post.getDescriptions().equals("description"));
    }


    @Test
    public void getMissingOrStray() {
        Post post = new Post();
        post.setMissingOrStray("missing");
        assert(post.getMissingOrStray().equals("missing"));
    }
}