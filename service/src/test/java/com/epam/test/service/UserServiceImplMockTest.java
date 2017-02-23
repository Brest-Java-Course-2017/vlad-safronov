package com.epam.test.service;

import com.epam.test.model.User;
import com.epam.test.dao.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

    private static final User USER = new User("userLogin3", "userPassword3");

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao mockUserDao;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @After
    public void clean() {
        verify(mockUserDao);
        reset(mockUserDao);
    }


    @Test
    public void testAddUser() throws Exception {
        expect(mockUserDao.addUser(new User("userLogin3", "userPassword3"))).andReturn(5);
        replay(mockUserDao);
        Integer id = userService.addUser(USER);
        Assert.isTrue(id == 5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetUserByLoginException() throws Exception {
        expect(mockUserDao.getUserByLogin(USER.getLogin())).andThrow(new UnsupportedOperationException());
        replay(mockUserDao);
        userService.getUserByLogin(USER.getLogin());
    }

//    @Test
//    public void getAllUsersTest(){
//       expect(mockUserDao.getAllUsers()).andReturn().times(1);
//        replay(mockUserDao);
//
//        userService.getAllUsers();
//    }

    @Test
    public void updateUserTest() throws Exception {
        User user = new User
                (1,"login","password","description");
        expect(mockUserDao.updateUser(user)).andReturn(1).times(1);
        replay(mockUserDao);

        userService.updateUser(user);
    }
    @Test
    public void updateUserNullTest() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(UserServiceImpl.ERR_USER_IS_NULL);

        replay(mockUserDao);
        User user = null;
        userService.updateUser(user);
    }
    @Test
    public void updateUserWithNullFieldsTest() throws Exception {
        thrown.expect(IllegalArgumentException.class);

        replay(mockUserDao);
        User user = new User();
        userService.updateUser(user);
    }
    @Test
    public void updateUserWithNullUserIdTest() throws Exception{
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(UserServiceImpl.ERR_USER_HAVE_NULL_ID);
        replay(mockUserDao);
        User user = new User
                (null,"login","password","description");
        userService.updateUser(user);
    }
    @Test
    public void updateUserWithNullLoginTest() throws Exception{
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(UserServiceImpl.ERR_USER_HAVE_NULL_LOGIN);
        replay(mockUserDao);
        User user = new User
                (1,null,"password","description");
        userService.updateUser(user);
    }
    @Test
    public void updateUserWithNullPasswordTest() throws Exception{
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(UserServiceImpl.ERR_USER_HAVE_NULL_PASSWORD);
        replay(mockUserDao);
        User user = new User
                (1,"login",null,"description");
        userService.updateUser(user);
    }
    @Test
    public void updateUserWithEmptyLogin() throws Exception{
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(UserServiceImpl.ERR_USER_HAVE_EMPTY_LOGIN);
        replay(mockUserDao);
        User user = new User
                (1,"","password","description");
        userService.updateUser(user);
    }
    @Test
    public void updateUserWithEmptyPassword() throws Exception{
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(UserServiceImpl.ERR_USER_HAVE_EMPTY_PASSWORD);
        replay(mockUserDao);
        User user = new User
                (1,"login","","description");
        userService.updateUser(user);
    }
    @Test
    public void updateUserWithNegativeUserId() throws Exception{
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(UserServiceImpl.ERR_USER_HAVE_NEGATIVE_ID);
        replay(mockUserDao);
        User user = new User
                (-1,"login","password","description");
        userService.updateUser(user);
    }

    @Test
    public void deleteUserWithNegativeParamTest() throws Exception {
        replay(mockUserDao);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(UserServiceImpl.ERR_USER_HAVE_NEGATIVE_ID);


        userService.deleteUser(-1);
    }
    @Test
    public void deleteUserWithNullParamTest() throws Exception {
        replay(mockUserDao);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(UserServiceImpl.ERR_USER_HAVE_NULL_ID);

        userService.deleteUser(null);
    }
    @Test
    public void deleteUserTest() throws Exception{
        Integer id = 1;
        mockUserDao.deleteUser(id);
        expectLastCall().times(1);
        replay(mockUserDao);

        userService.deleteUser(id);
    }

    @Test
    public void getUserByIdTest(){
        expect(mockUserDao.getUserById(1)).andReturn(new User());
        replay(mockUserDao);

        userService.getUserById(1);
    }
    @Test
    public void getUserWithNegativeIdTest(){
        replay(mockUserDao);
        Integer id = -1;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(UserServiceImpl.ERR_USER_HAVE_NEGATIVE_ID);
        userService.getUserById(-1);
    }
    @Test
    public void getUserWithNullIdTest(){
        replay(mockUserDao);
        Integer id  = null;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(UserServiceImpl.ERR_USER_HAVE_NULL_ID);
        userService.getUserById(id);
    }

}