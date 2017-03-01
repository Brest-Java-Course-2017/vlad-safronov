package com.epam.test.dao;

import com.epam.test.model.User;

import java.util.List;

public interface UserDao {

     List<User> getAllUsers();

     User getUserById(Integer userId);

     Integer addUser(User user);

     int updateUser(User user) throws Exception;

     void deleteUser(Integer userId) throws Exception;

     User getUserByLogin(String login) throws Exception;

     boolean isUserExist(String login);

}
