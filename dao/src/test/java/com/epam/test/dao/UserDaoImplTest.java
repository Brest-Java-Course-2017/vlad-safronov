package com.epam.test.dao;


import com.epam.test.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

        User user = userDao.getUserById(1);
        assertNotNull(user);
        assertEquals("userLogin1",user.getLogin());
    }
    @Test
    public void addUserTest() throws Exception{

    }
    @Test
    public void updateUser(User user) throws Exception{

    }
    @Test
    public void deleteUser(Integer userId) throws Exception{

    }

}