package com.revshop.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.revshop.master.LoginMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.LoginService;
import com.revshop.service.UserService;

public class AccountControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private LoginService loginService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void testShowAccountPage_LoggedInUser_Success() throws Exception {
        // Mock logged-in user
        LoginMaster loginMaster = new LoginMaster();
        UserMaster userMaster = new UserMaster();
        loginMaster.setUserId(userMaster);
        userMaster.setUserId(1);
        
        when(loginService.getLoggedInUserDetails()).thenReturn(loginMaster);
        when(userService.getUserById(1)).thenReturn(userMaster);

        // Perform GET request and verify the result
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userDetails"))
                .andExpect(view().name("LoginAndRegistration/userAccount"));

        // Verify interactions
        verify(loginService, times(1)).getLoggedInUserDetails();
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void testShowAccountPage_UserNotLoggedIn() throws Exception {
        // Mock no logged-in user
        when(loginService.getLoggedInUserDetails()).thenReturn(null);

        // Perform GET request and verify the result
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", "User not logged in"))
                .andExpect(view().name("LoginAndRegistration/userAccount"));

        // Verify interaction
        verify(loginService, times(1)).getLoggedInUserDetails();
        verify(userService, never()).getUserById(anyInt());
    }

    @Test
    void testShowAccountPage_ErrorFetchingUserDetails() throws Exception {
        // Mock logged-in user
        LoginMaster loginMaster = new LoginMaster();
        UserMaster userMaster = new UserMaster();
        loginMaster.setUserId(userMaster);
        userMaster.setUserId(1);

        when(loginService.getLoggedInUserDetails()).thenReturn(loginMaster);
        when(userService.getUserById(1)).thenThrow(new RuntimeException("Database error"));

        // Perform GET request and verify the result
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", "An error occurred while fetching user details: Database error"))
                .andExpect(view().name("LoginAndRegistration/userAccount"));

        // Verify interactions
        verify(loginService, times(1)).getLoggedInUserDetails();
        verify(userService, times(1)).getUserById(1);
    }
}
