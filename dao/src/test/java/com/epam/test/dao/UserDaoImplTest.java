package com.epam.test.dao;


import com.epam.test.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-spring-dao.xml"})
public class UserDaoImplTest{

    @Autowired
    UserDao userDao;

    @Test
    public void getAllUsersTest() throws Exception {
        List<User> users = userDao.getAllUsers();
        assertTrue(users.size() == 2);
    }

    @Test
    public void getUserByIdTest() throws Exception {

        User user = userDao.getUserById(2);
        assertNotNull(user);
        assertEquals("userLogin2",user.getLogin());
        user = userDao.getUserById(10);
        assertEquals(null,user);
    }
    @Test
    public void addUserTest() throws Exception{
        User user = new User(
                3,
                "TestUser3",
                "userPassword",
                "third test user"
        );
        Integer id = userDao.addUser(user);
        User userFromDb = userDao.getUserById(user.getUserId());
        assertEquals(user,userFromDb);
        assertEquals(new Integer(3),id);
        userDao.deleteUser(3);
    }

    @Test
    public void updateUser() throws Exception{
        User user = new User(
                1,
                "UpdatedUser",
                "UpdatedPassword",
                "UpdatedDescription");
        User oldUser = userDao.getUserById(1);
        assertNotEquals(user,oldUser);
        userDao.updateUser(user);
        User newUser = userDao.getUserById(1);
        assertEquals(user,newUser);
    }
    @Test
    public void deleteUser() throws Exception{
        List<User> users = userDao.getAllUsers();
        Integer sizeBeforeDelete = users.size();
        User user = userDao.getUserById(1);
        userDao.deleteUser(1);
        users = userDao.getAllUsers();
        Integer sizeAfterDelete = users.size();
        assertEquals(1,sizeBeforeDelete-sizeAfterDelete);
        assertEquals(null,userDao.getUserById(1));

    }

}