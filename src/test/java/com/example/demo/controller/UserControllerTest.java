package com.example.demo.controller;

import com.example.demo.Utils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private static UserController userController;
    private static UserRepository userRepository = mock(UserRepository.class);
    private static CartRepository cartRepository = mock(CartRepository.class);
    private static BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    private static String DESCRIPTION = "test description";
    private static String ITEM_NAME = "bread";
    private static String USER_NAME = "testuser";
    private static String PASSWORD = "password";

    @BeforeClass
    public static void setup() {
        userController = new UserController();
        Utils.injectObject(userController, "userRepository", userRepository);
        Utils.injectObject(userController, "cartRepository", cartRepository);
        Utils.injectObject(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void findById() {
        User user = Utils.createUser(1L, PASSWORD, USER_NAME);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User returnedUser = response.getBody();
        assertEquals(USER_NAME, returnedUser.getUsername());
        assertEquals(PASSWORD, returnedUser.getPassword());
    }

    @Test
    public void findByUsername() {
        User user = Utils.createUser(1L, PASSWORD, USER_NAME);

        when(userRepository.findByUsername(USER_NAME)).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName(USER_NAME);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User returnedUser = response.getBody();
        assertEquals(USER_NAME, returnedUser.getUsername());
        assertEquals(PASSWORD, returnedUser.getPassword());
    }

    @Test
    public void createUser() {
        when(bCryptPasswordEncoder.encode(PASSWORD)).thenReturn("hashed");
        CreateUserRequest createUserRequest = Utils.createCreateUserRequest(PASSWORD, USER_NAME);

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals(USER_NAME, user.getUsername());
        assertEquals("hashed", user.getPassword());
    }

}
