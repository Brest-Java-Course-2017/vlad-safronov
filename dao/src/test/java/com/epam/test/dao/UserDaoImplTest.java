package com.epam.test.dao;


import com.epam.test.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-spring-dao.xml"})
@Transactional
public class UserDaoImplTest{

    public static final User userAddTest = new User
            (3,"TestUser3","userPassword","third test user");
    public static final Integer COUNT_OF_ALL_USERS = 2;

    @Autowired
    UserDao userDao;

    @Test
    public void getAllUsersTest() throws Exception {
        List<User> users = userDao.getAllUsers();
        assertTrue(users.size() == COUNT_OF_ALL_USERS);
    }

    @Test
    public void getUserByIdTest() throws Exception {

        User user = userDao.getUserById(COUNT_OF_ALL_USERS-1);
        assertNotNull(user);
        assertEquals("userLogin1",user.getLogin());
        user = userDao.getUserById(COUNT_OF_ALL_USERS+1);
        assertEquals(null,user);
    }
    @Test
    public void addUserTest() throws Exception{
        Integer id = userDao.addUser(userAddTest);
        User userFromDb = userDao.getUserById(userAddTest.getUserId());
        assertEquals(userAddTest,userFromDb);
        assertEquals(new Integer(COUNT_OF_ALL_USERS+1),id);
        userDao.deleteUser(COUNT_OF_ALL_USERS+1);
    }

    @Test
    public void getUserByLoginTest() throws Exception {
        User user = userDao.getUserById(1);
        User testUser = userDao.getUserByLogin(user.getLogin());
        Assert.assertEquals(user,testUser);
    }
    @Test
    public void getNotExistUserByLoginTest() throws Exception{
        thrown.expect(Exception.class);
        thrown.expectMessage(UserDaoImpl.ERR_USER_IS_NOT_EXIST);
        User testUser = userDao.getUserByLogin("Blablabla");
    }


    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Test
    public void updateUser() throws Exception{

        User user = new User(
                COUNT_OF_ALL_USERS+1,
                "UpdatedUser",
                "UpdatedPassword",
                "UpdatedDescription");
        thrown.expect(Exception.class);
        thrown.expectMessage(UserDaoImpl.ERR_USER_IS_NOT_EXIST);
        userDao.updateUser(user);

        user.setUserId(COUNT_OF_ALL_USERS-1);
        User oldUser = userDao.getUserById(COUNT_OF_ALL_USERS-1);
        assertNotEquals(oldUser,user);

        userDao.updateUser(user);
        User newUser = userDao.getUserById(COUNT_OF_ALL_USERS-1);
        assertEquals(user,newUser);
    }
    @Test
    public void deleteUser() throws Exception{
        List<User> users = userDao.getAllUsers();
        Integer sizeBeforeDelete = users.size();

        User user = userDao.getUserById(COUNT_OF_ALL_USERS);
        assertNotNull(user);

        userDao.deleteUser(COUNT_OF_ALL_USERS);

        users = userDao.getAllUsers();
        Integer sizeAfterDelete = users.size();
        assertEquals(1,sizeBeforeDelete-sizeAfterDelete);
        assertEquals(null,userDao.getUserById(COUNT_OF_ALL_USERS));

        //TODO вынести негативные сценарии в отдельные тесты

        thrown.expect(Exception.class);
        thrown.expectMessage(UserDaoImpl.ERR_USER_IS_NOT_EXIST);
        userDao.deleteUser(COUNT_OF_ALL_USERS+1);
    }

}