package com.epam.test.service;

import com.epam.test.model.User;
import com.epam.test.dao.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers() throws DataAccessException {
        LOGGER.debug("getAllUser(): get all users");
        List<User> allUsers = userDao.getAllUsers();
        for (User user:allUsers) {
            LOGGER.debug(user);
        }
        return allUsers;
    }

    @Override
    public User getUserById(Integer userId) throws DataAccessException {
        LOGGER.debug("getUserById: user id = {}",userId);
        Assert.notNull(userId,ERR_USER_HAVE_NULL_ID);
        Assert.isTrue(userId.intValue()>0,ERR_USER_HAVE_NEGATIVE_ID);

        return userDao.getUserById(userId);
    }

    @Override
    public User getUserByLogin(String login) throws Exception {
        LOGGER.debug("getUserByLogin(): user login = {} ", login);
        Assert.hasText(login, "User login should not be null.");
        return userDao.getUserByLogin(login);
    }

    @Override
    public Integer addUser(User user) throws DataAccessException {
        Assert.notNull(user, "User should not be null.");
        LOGGER.debug("addUser(): user login = {} ", user.getLogin());
        Assert.isNull(user.getUserId(), "User Id should be null.");
        Assert.hasText(user.getLogin(), "User login should not be null.");
        Assert.hasText(user.getPassword(), "User password should not be null.");
        if(userDao.isUserExist(user.getLogin())) throw new IllegalArgumentException("User with login "
                + user.getLogin()+"already exist");
        return userDao.addUser(user);
    }

    @Override
    public int updateUser(User user) throws Exception {
        Assert.notNull(user,ERR_USER_IS_NULL);

        LOGGER.debug(" updateUser(): update user = {} ",user);

        Assert.notNull(user.getUserId(),ERR_USER_HAVE_NULL_ID);
        Assert.notNull(user.getLogin(),ERR_USER_HAVE_NULL_LOGIN);
        Assert.notNull(user.getPassword(),ERR_USER_HAVE_NULL_PASSWORD);

        Assert.hasText(user.getLogin(),ERR_USER_HAVE_EMPTY_LOGIN);
        Assert.hasText(user.getPassword(),ERR_USER_HAVE_EMPTY_PASSWORD);

        Assert.isTrue(user.getUserId().intValue()>0,ERR_USER_HAVE_NEGATIVE_ID);

        return  userDao.updateUser(user);
    }

    @Override
    public int deleteUser(Integer userId) throws Exception {
        LOGGER.debug("deleteUser(): id = {}",userId);
        Assert.notNull(userId,ERR_USER_HAVE_NULL_ID);
        Assert.isTrue(userId.intValue()>0,ERR_USER_HAVE_NEGATIVE_ID);

        userDao.deleteUser(userId);
        return 0;
    }

    public static final String  ERR_USER_HAVE_NULL_ID = "User Id should not be null";
    public static final String ERR_USER_HAVE_NULL_LOGIN = "User login should not be null";
    public static final String ERR_USER_HAVE_EMPTY_LOGIN = "User login should has text";
    public static final String ERR_USER_HAVE_EMPTY_PASSWORD = "User password should has text";
    public static final String ERR_USER_IS_NULL = "User should not be null";
    public static final String ERR_USER_HAVE_NULL_PASSWORD ="User password should not be null";
    public static final String ERR_USER_HAVE_NEGATIVE_ID = "User id should not be negative";

}