package com.revshop.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.revshop.dao.LoginDAO;
import com.revshop.dao.PasswordResetTokenDAO;
import com.revshop.master.LoginMaster;
import com.revshop.master.PasswordResetTokenMaster;
import com.revshop.service.PasswordResetService;
import com.revshop.utility.EmailService;

import jakarta.transaction.Transactional;

@Service
public class PasswordResetServiceIMPL implements PasswordResetService {

    @Autowired
    private PasswordResetTokenDAO passwordResetTokenDAO;
    
    @Autowired
    private LoginDAO loginDAO;
    
    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createPasswordResetToken(LoginMaster loginMaster, String token) {
        PasswordResetTokenMaster resetToken = new PasswordResetTokenMaster(
            token, 
            loginMaster, 
            LocalDateTime.now().plusMinutes(5) // Token expires in 30 minutes
        );
        passwordResetTokenDAO.saveToken(resetToken);
    }

    @Override
    public PasswordResetTokenMaster getToken(String token) {
        return passwordResetTokenDAO.findByToken(token);
    }

    @Override
    public void deleteToken(PasswordResetTokenMaster token) {
        passwordResetTokenDAO.deleteToken(token);
    }
    
    @Override
    @Transactional
    public void sendPasswordResetToken(String email) {
        LoginMaster user = loginDAO.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("No user found with this email address.");
        }

        // Generate a random token
        String token = UUID.randomUUID().toString();
        createPasswordResetToken(user, token); // Save the token

        // Send email with reset link
        String resetUrl = "http://localhost:8081/reset-password?token=" + token;
        String emailContent = "Click the following link to reset your password: " + resetUrl;
        emailService.sendEmail(user.getEmail(), "Password Reset", emailContent);
    }

    @Override
    @Transactional
    public LoginMaster getByResetToken(String token) {
        PasswordResetTokenMaster resetToken = getToken(token);
        if (resetToken == null || resetToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            return null;  // Token is invalid or expired
        }
        return resetToken.getLoginMaster();
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        LoginMaster user = getByResetToken(token);
        if (user == null) {
            throw new RuntimeException("Invalid or expired token.");
        }

        // Reset the user's password
        user.setPassword(passwordEncoder.encode(newPassword));
        loginDAO.insert(user);

        // Optionally delete the token after successful password reset
        PasswordResetTokenMaster resetToken = getToken(token);
        deleteToken(resetToken);
    }
    
    @Scheduled(fixedRate = 300000)  // 300000 milliseconds = 5 minutes
    @Transactional
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        passwordResetTokenDAO.deleteExpiredTokens(now);
        System.out.println("Cleaned up expired password reset tokens at " + now);
    }
}
