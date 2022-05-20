package com.example.comp4521.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

    @Test
    public void getName() {
        User user = new User();
        user.setName("Olivia");
        assert (user.getName().equals("Olivia"));
    }

    @Test
    public void getUserId() {
        User user = new User();
        user.setUserId("123");
        assert (user.getUserId().equals("123"));
    }

    @Test
    public void getUserEmail() {
        User user = new User();
        user.setUserEmail("123@gmail.com");
        assert (user.getUserEmail().equals("123@gmail.com"));
    }
}