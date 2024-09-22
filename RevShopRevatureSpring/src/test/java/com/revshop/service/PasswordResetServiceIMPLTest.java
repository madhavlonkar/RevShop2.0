package com.revshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.revshop.dao.LoginDAO;
import com.revshop.dao.PasswordResetTokenDAO;
import com.revshop.master.LoginMaster;
import com.revshop.master.PasswordResetTokenMaster;
import com.revshop.service.impl.PasswordResetServiceIMPL;
import com.revshop.utility.EmailService;

public class PasswordResetServiceIMPLTest {

    @Mock
    private PasswordResetTokenDAO passwordResetTokenDAO;

    @Mock
    private LoginDAO loginDAO;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetServiceIMPL passwordResetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePasswordResetToken() {
        LoginMaster loginMaster = new LoginMaster();
        loginMaster.setEmail("test@example.com");
        String token = UUID.randomUUID().toString();

        // Call the method
        passwordResetService.createPasswordResetToken(loginMaster, token);

        // Verify that the token was saved
        verify(passwordResetTokenDAO, times(1)).saveToken(any(PasswordResetTokenMaster.class));
    }

    @Test
    void testGetToken_Success() {
        String token = "testToken";
        PasswordResetTokenMaster resetToken = new PasswordResetTokenMaster();
        resetToken.setToken(token);

        when(passwordResetTokenDAO.findByToken(token)).thenReturn(resetToken);

        PasswordResetTokenMaster result = passwordResetService.getToken(token);

        assertEquals(token, result.getToken());
    }

    @Test
    void testSendPasswordResetToken_Success() {
        String email = "test@example.com";
        LoginMaster loginMaster = new LoginMaster();
        loginMaster.setEmail(email);

        when(loginDAO.findByEmail(email)).thenReturn(loginMaster);

        // Call the method
        passwordResetService.sendPasswordResetToken(email);

        // Verify that the email was sent and the token was created
        verify(emailService, times(1)).sendEmail(eq(email), eq("Password Reset"), anyString());
        verify(passwordResetTokenDAO, times(1)).saveToken(any(PasswordResetTokenMaster.class));
    }

    @Test
    void testSendPasswordResetToken_UserNotFound() {
        String email = "notfound@example.com";
        when(loginDAO.findByEmail(email)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            passwordResetService.sendPasswordResetToken(email);
        });

        assertEquals("No user found with this email address.", exception.getMessage());
    }

    @Test
    void testGetByResetToken_ValidToken() {
        String token = "validToken";
        PasswordResetTokenMaster resetToken = new PasswordResetTokenMaster();
        resetToken.setToken(token);
        resetToken.setExpirationTime(LocalDateTime.now().plusMinutes(10));
        LoginMaster loginMaster = new LoginMaster();
        resetToken.setLoginMaster(loginMaster);

        when(passwordResetTokenDAO.findByToken(token)).thenReturn(resetToken);

        LoginMaster result = passwordResetService.getByResetToken(token);

        assertNotNull(result);
        assertEquals(loginMaster, result);
    }

    @Test
    void testGetByResetToken_InvalidToken() {
        String token = "invalidToken";
        when(passwordResetTokenDAO.findByToken(token)).thenReturn(null);

        LoginMaster result = passwordResetService.getByResetToken(token);

        assertNull(result);
    }

    @Test
    void testGetByResetToken_ExpiredToken() {
        String token = "expiredToken";
        PasswordResetTokenMaster resetToken = new PasswordResetTokenMaster();
        resetToken.setToken(token);
        resetToken.setExpirationTime(LocalDateTime.now().minusMinutes(1));

        when(passwordResetTokenDAO.findByToken(token)).thenReturn(resetToken);

        LoginMaster result = passwordResetService.getByResetToken(token);

        assertNull(result);
    }

    @Test
    void testResetPassword_Success() {
        String token = "validToken";
        String newPassword = "newPassword";
        PasswordResetTokenMaster resetToken = new PasswordResetTokenMaster();
        resetToken.setToken(token);
        resetToken.setExpirationTime(LocalDateTime.now().plusMinutes(10));
        LoginMaster loginMaster = new LoginMaster();
        loginMaster.setPassword("oldPassword");
        resetToken.setLoginMaster(loginMaster);

        when(passwordResetTokenDAO.findByToken(token)).thenReturn(resetToken);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        // Call the method
        passwordResetService.resetPassword(token, newPassword);

        // Verify password update and token deletion
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(loginDAO, times(1)).insert(loginMaster);
        verify(passwordResetTokenDAO, times(1)).deleteToken(resetToken);
    }

    @Test
    void testResetPassword_InvalidToken() {
        String token = "invalidToken";
        when(passwordResetTokenDAO.findByToken(token)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            passwordResetService.resetPassword(token, "newPassword");
        });

        assertEquals("Invalid or expired token.", exception.getMessage());
    }

    @Test
    void testCleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();

        // Call the method
        passwordResetService.cleanExpiredTokens();

        // Verify that expired tokens were deleted
        verify(passwordResetTokenDAO, times(1)).deleteExpiredTokens(now);
    }
}
