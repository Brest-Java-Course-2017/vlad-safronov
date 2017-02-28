package com.epam.test.rest;

import com.epam.test.model.User;
import com.epam.test.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;
import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-spring-rest-mock.xml"})
public class UserControllerMockTest {

    public static final User user = new User( "userLogin1", "userPassword1");

    @Resource
    private UserRestController userController;

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(userController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @After
    public void tearDown() {
        verify(userService);
        reset(userService);
    }

    @Test
    public void addUserTest() throws Exception {
        expect(userService.addUser(anyObject(User.class))).andReturn(3);
        replay(userService);

        String user = new ObjectMapper().writeValueAsString(new User("login2", "password2"));

        mockMvc.perform(
                post("/user")
                        .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(user)
        ).andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().string("3"));
    }

    @Test
    public void getUsersTest() throws Exception {
        expect(userService.getAllUsers()).andReturn(Arrays.<User>asList(new User("l", "p")));
        replay(userService);

        mockMvc.perform(
                get("/users")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUserByIdTest() throws Exception {
        expect(userService.getUserById(1)).andReturn(user);
        replay(userService);

        mockMvc.perform(
                get("/user/id/1")
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.password").value(user.getPassword()));
    }

    @Test
    public void updateUserTest() throws Exception {
        expect(userService.updateUser(anyObject(User.class))).andReturn(0);
        replay(userService);

        mockMvc.perform(
                put("/user/2/login5/password5/desc5")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isAccepted())
        .andExpect(content().string(""));
    }

    @Test
    public void deleteUserTest() throws Exception {
        expect(userService.deleteUser(1)).andReturn(1);
        replay(userService);

        mockMvc.perform(
                delete("/user/1")).andDo(print())
                .andExpect(status().isOk());

    }
}
