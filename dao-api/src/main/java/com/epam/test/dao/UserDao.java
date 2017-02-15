package com.epam.test.dao;

import com.epam.test.model.User;

import java.util.List;

/**
 * Created by vlad on 15.2.17.
 */

public interface UserDao {
   List<User> getAllUsers();
   User getUserById();
}
