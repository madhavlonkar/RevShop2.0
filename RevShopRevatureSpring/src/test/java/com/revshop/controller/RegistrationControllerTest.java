package com.revshop.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.revshop.exceptionHandler.EmailAlreadyExistsException;
import com.revshop.exceptionHandler.UsernameAlreadyExistsException;
import com.revshop.master.LoginMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.LoginService;
import com.revshop.service.UserService;

public class RegistrationControllerTest {

    @Mock
    private LoginService loginService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RegistrationController registrationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
    }

    @Test
    void testShowRegistrationPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("LoginAndRegistration/user-register"));
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        LoginMaster user = new LoginMaster();
        when(loginService.registerUser(user)).thenReturn(true);

        mockMvc.perform(post("/register")
                .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(loginService, times(1)).registerUser(user);
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists() throws Exception {
        LoginMaster user = new LoginMaster();
        when(loginService.registerUser(user)).thenThrow(new UsernameAlreadyExistsException("Username already exists"));

        mockMvc.perform(post("/register")
                .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(model().attribute("RegistererrorMessage", "Username already exists"))
                .andExpect(view().name("LoginAndRegistration/user-register"));

        verify(loginService, times(1)).registerUser(user);
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() throws Exception {
        LoginMaster user = new LoginMaster();
        when(loginService.registerUser(user)).thenThrow(new EmailAlreadyExistsException("Email already exists"));

        mockMvc.perform(post("/register")
                .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(model().attribute("RegistererrorMessage", "Email already exists"))
                .andExpect(view().name("LoginAndRegistration/user-register"));

        verify(loginService, times(1)).registerUser(user);
    }

    @Test
    void testRegisterUser_UnknownError() throws Exception {
        LoginMaster user = new LoginMaster();
        when(loginService.registerUser(user)).thenReturn(false);

        mockMvc.perform(post("/register")
                .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(model().attribute("RegistererrorMessage", "Registration failed due to unknown reasons."))
                .andExpect(view().name("LoginAndRegistration/user-register"));

        verify(loginService, times(1)).registerUser(user);
    }

    @Test
    void testShowDetailRegistrationPage_Success() throws Exception {
        LoginMaster loggedInUser = new LoginMaster();
        when(loginService.getLoggedInUserDetails()).thenReturn(loggedInUser);

        mockMvc.perform(get("/detailregistrationpage"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("LoginAndRegistration/detail-registration"));

        verify(loginService, times(1)).getLoggedInUserDetails();
    }

    @Test
    void testCompleteDetailRegistration_Success() throws Exception {
        UserMaster user = new UserMaster();
        String role = "ADMIN";

        mockMvc.perform(post("/completeDetailRegistration")
                .flashAttr("user", user)
                .param("role", role))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(userService, times(1)).updateDetails(user, role);
    }
}
