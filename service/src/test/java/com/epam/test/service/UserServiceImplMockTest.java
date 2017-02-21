package com.epam.test.service;

import com.epam.test.model.User;
import com.epam.test.dao.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import static org.easymock.EasyMock.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:service-test-mock.xml"})
public class UserServiceImplMockTest {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final User user = new User("userLogin3", "userPassword3");

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao mockUserDao;

    @After
    public void clean() {
        verify(mockUserDao);
        reset(mockUserDao);
    }

    @Test
    public void testAddUser() throws Exception {
        expect(mockUserDao.addUser(new User("userLogin3", "userPassword3"))).andReturn(5);
        replay(mockUserDao);
        Integer id = userService.addUser(user);
        Assert.isTrue(id == 5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetUserByLoginException() {
        expect(mockUserDao.getUserByLogin(user.getLogin())).andThrow(new UnsupportedOperationException());
        replay(mockUserDao);
        userService.getUserByLogin(user.getLogin());
    }

}