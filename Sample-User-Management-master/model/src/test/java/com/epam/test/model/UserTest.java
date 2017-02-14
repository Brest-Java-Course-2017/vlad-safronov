package com.epam.test.model;

import org.junit.Assert;

/**
 * Created by Vlad on 14.2.17.
 */
public class UserTest {

    public static final int USER_ID = 11;

    @org.junit.Test
    public void getUserId() throws Exception {

        User user = new User();
        user.setUserId(11);
        Assert.assertEquals("User id: ", (Integer) USER_ID, user.getUserId());
    }

    @org.junit.Test
    public void getLogin() throws Exception {

    }

    @org.junit.Test
    public void getPassword() throws Exception {

    }

    @org.junit.Test
    public void getDescription() throws Exception {

    }

}