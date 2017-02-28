package com.epam.test.client.rest.api;

import com.epam.test.client.exception.ServerDataAccessException;
import com.epam.test.model.User;

import java.util.List;

/**
 * Users Consumer Api.
 */
public interface UsersConsumer {

    /**
     * Get all users list.
     *
     * @return all users list
     */
    List<User> getAllUsers() throws ServerDataAccessException;

    /**
     * Get user by Id.
     *
     * @param userId user identifier.
     * @return user.
     */
    User getUserById(Integer userId) throws ServerDataAccessException;

    /**
     * Get user by login.
     *
     * @param login user login.
     * @return user
     * @throws ServerDataAccessException
     */
    User getUserByLogin(String login) throws ServerDataAccessException;

    /**
     * Create new user.
     *
     * @param user user.
     * @return new user Id.
     */
    Integer addUser(User user) throws ServerDataAccessException;

    /**
     * Update user.
     *
     * @param user user.
     * @return new user Id.
     */
    int updateUser(User user) throws ServerDataAccessException;

    int deleteUser(Integer userId) throws ServerDataAccessException;
}
