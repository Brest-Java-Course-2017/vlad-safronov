package com.epam.test.rest;

import com.epam.test.model.User;
import com.epam.test.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserRestController {

    private static final Logger LOGGER = LogManager.getLogger();


    @Autowired
    private UserService userService;

    //curl -v localhost:8088/users
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public @ResponseBody List<User> getUsers() {
        LOGGER.debug("getUsers()");
        return userService.getAllUsers();
    }

    //curl -H "Content-Type: application/json" -X POST -d '{"login":"xyz","password":"xyz"}' -v localhost:8088/user
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Integer addUser(@RequestBody User user) {
        LOGGER.debug("addUser: user = {}", user);
        return userService.addUser(user);
    }

    //curl -X PUT -v localhost:8088/user/2/l1/p1/d1
    @RequestMapping(value = "/user/{id}/{login}/{password}/{desc}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void updateUser(@PathVariable(value = "id") int id, @PathVariable(value = "desc") String desc,
            @PathVariable(value = "login") String login, @PathVariable(value = "password") String password) throws Exception {
        LOGGER.debug("updateUser: id = {}", id);
        userService.updateUser(new User(id, login, password, desc));
    }

    //curl -v localhost:8088/user/userLogin1
    @RequestMapping(value = "/user/{login}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.FOUND)
    public @ResponseBody User getUser(@PathVariable(value = "login") String login) throws Exception {
        LOGGER.debug("getUser: login = {}", login);
        return userService.getUserByLogin(login);
    }
}
