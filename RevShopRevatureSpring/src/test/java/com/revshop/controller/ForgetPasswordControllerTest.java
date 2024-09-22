package com.revshop.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import com.revshop.master.LoginMaster;
import com.revshop.service.PasswordResetService;

public class ForgetPasswordControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PasswordResetService passwordResetService;

    @InjectMocks
    private ForgetPasswordController forgetPasswordController;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(forgetPasswordController).build();
    }

    @Test
    public void testShowForgotPasswordPage() throws Exception {
        mockMvc.perform(get("/forgot-password"))
               .andExpect(status().isOk())
               .andExpect(view().name("LoginAndRegistration/forgot-password"));
    }

    @Test
    public void testProcessForgotPassword_Success() throws Exception {
        String email = "test@example.com";

        mockMvc.perform(post("/forgot-password")
                .param("email", email))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/forgot-password?success"));

        verify(passwordResetService, times(1)).sendPasswordResetToken(email);
    }

    @Test
    public void testShowResetPasswordPage_ValidToken() throws Exception {
        String token = "validToken";
        LoginMaster mockUser = new LoginMaster();
        when(passwordResetService.getByResetToken(token)).thenReturn(mockUser);

        mockMvc.perform(get("/reset-password")
                .param("token", token))
               .andExpect(status().isOk())
               .andExpect(view().name("LoginAndRegistration/reset-password"))
               .andExpect(model().attributeExists("token"));

        verify(passwordResetService, times(1)).getByResetToken(token);
    }

    @Test
    public void testShowResetPasswordPage_InvalidToken() throws Exception {
        String token = "invalidToken";
        when(passwordResetService.getByResetToken(token)).thenReturn(null);

        mockMvc.perform(get("/reset-password")
                .param("token", token))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/forgot-password?error"));

        verify(passwordResetService, times(1)).getByResetToken(token);
    }

    @Test
    public void testProcessResetPassword_Success() throws Exception {
        String token = "validToken";
        String newPassword = "newPassword";

        mockMvc.perform(post("/reset-password")
                .param("token", token)
                .param("password", newPassword))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/login?resetSuccess"));

        verify(passwordResetService, times(1)).resetPassword(token, newPassword);
    }
}
