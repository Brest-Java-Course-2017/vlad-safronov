package com.epam.test.service;



import com.epam.test.model.User;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface UserService {

    /**
     * Get all users list.
     *
     * @return all users list
     */
    List<User> getAllUsers() throws DataAccessException;

    /**
     * Get user by Id.
     *
     * @param userId user identifier.
     * @return user.
     */
    User getUserById(Integer userId) throws DataAccessException;

    /**
     * Get user by login.
     *
     * @param login user login.
     * @return user
     * @throws DataAccessException
     */
    User getUserByLogin(String login) throws Exception;

    /**
     * Create new user.
     *
     * @param user user.
     * @return new user Id.
     */
    Integer addUser(User user) throws DataAccessException;

    /**
     * Update user.
     *
     * @param user user.
     * @return new user Id.
     */
    int updateUser(User user) throws Exception;

    int deleteUser(Integer userId) throws Exception;

}