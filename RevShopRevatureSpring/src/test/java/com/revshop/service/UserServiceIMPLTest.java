package com.revshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import com.revshop.dao.LoginDAO;
import com.revshop.dao.UserDAO;
import com.revshop.master.LoginMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.impl.UserServiceIMPL;

public class UserServiceIMPLTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private LoginDAO loginDAO;

    @InjectMocks
    private UserServiceIMPL userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser_Success() {
        UserMaster user = new UserMaster();
        user.setEmail("test@example.com");

        when(userDAO.saveUser(user)).thenReturn(user);

        // Call the method
        UserMaster result = userService.saveUser(user);

        // Assert that the returned user is the one saved
        assertEquals(user.getEmail(), result.getEmail());
        verify(userDAO, times(1)).saveUser(user);
    }

    @Test
    void testSaveUser_EmailAlreadyExists() {
        UserMaster user = new UserMaster();
        user.setEmail("existing@example.com");

        when(userDAO.saveUser(user)).thenThrow(new DataIntegrityViolationException("Email already exists"));

        // Assert that an exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.saveUser(user);
        });

        assertEquals("Email already exists. Please use a different email.", exception.getMessage());
        verify(userDAO, times(1)).saveUser(user);
    }

    @Test
    void testUpdateDetails_Success() {
        UserMaster user = new UserMaster();
        user.setEmail("test@example.com");
        LoginMaster loginMaster = new LoginMaster();
        loginMaster.setEmail(user.getEmail());
        loginMaster.setFirstLogin(true);

        when(loginDAO.findByEmail(user.getEmail())).thenReturn(loginMaster);
        when(userDAO.saveUser(user)).thenReturn(user);

        // Call the method
        boolean result = userService.updateDetails(user, "ADMIN");

        // Assert the method call was successful
        assertTrue(result);
        assertEquals("ROLE_ADMIN", loginMaster.getRole());
        assertFalse(loginMaster.isFirstLogin());
        verify(loginDAO, times(1)).insert(loginMaster);
        verify(userDAO, times(1)).saveUser(user);
    }

    @Test
    void testGetUserById_Success() {
        int userId = 1;
        UserMaster user = new UserMaster();
        user.setUserId(userId);

        when(userDAO.getUserById(userId)).thenReturn(user);

        // Call the method
        UserMaster result = userService.getUserById(userId);

        // Assert the returned user matches the expected one
        assertEquals(userId, result.getUserId());
        verify(userDAO, times(1)).getUserById(userId);
    }
}
