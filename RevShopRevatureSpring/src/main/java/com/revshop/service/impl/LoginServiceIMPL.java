package com.revshop.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.revshop.dao.LoginDAO;
import com.revshop.exceptionHandler.EmailAlreadyExistsException;
import com.revshop.exceptionHandler.UsernameAlreadyExistsException;
import com.revshop.master.LoginMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.LoginService;
import com.revshop.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class LoginServiceIMPL implements LoginService {

    @Autowired
    private LoginDAO loginDAO;
    
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public boolean registerUser(LoginMaster user) {
        try {
            // Check if username or email already exists
            if (loginDAO.existsByUsername(user.getUserName())) {
                throw new UsernameAlreadyExistsException("Username already in use.");
            }

            if (loginDAO.existsByEmail(user.getEmail())) {
                throw new EmailAlreadyExistsException("Email already in use.");
            }

            // Encrypt the user's password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setFirstLogin(true);

            // Save the user in UserMaster and LoginMaster tables together
            UserMaster u = new UserMaster();
            u.setEmail(user.getEmail());
            UserMaster savedUser = userService.saveUser(u); // Save in UserMaster
            user.setUserId(savedUser); // Set the saved UserMaster object in LoginMaster
            user.setRole("NoYetDecided");

            return loginDAO.insert(user); // Save in LoginMaster
        } catch (EmailAlreadyExistsException | UsernameAlreadyExistsException e) {
            // Rethrow custom exceptions to be caught by the controller
            throw e;
        } catch (DataIntegrityViolationException e) {
            // Handle database integrity violation exceptions (like unique constraints)
            throw new RuntimeException("A database integrity violation occurred: " + e.getMessage(), e);
        } catch (Exception e) {
            // Handle general exceptions
            throw new RuntimeException("An error occurred during registration: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public List<LoginMaster> getAllUser() {
        try {
            return loginDAO.reterieveAll();
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while fetching users: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        try {
            loginDAO.delete(userId);  // Implement the delete method
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while deleting the user: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean existsByUsername(String username) {
        try {
            return loginDAO.existsByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while checking username existence: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean existsByEmail(String email) {
        try {
            return loginDAO.existsByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while checking email existence: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public LoginMaster getLoggedInUserDetails() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null) {
                throw new RuntimeException("No authentication details found.");
            }
            
            // Handle OAuth2 users
            if (authentication.getPrincipal() instanceof OAuth2User) {
                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                String email = getEmailFromOAuth2User(oAuth2User);
                if (email == null) {
                    throw new RuntimeException("Email not found for OAuth2 user.");
                }
                return loginDAO.findByEmail(email); // Find the user by email in the database
            }
            
            // Handle manually logged-in users (custom UserDetails implementation)
            String email = authentication.getName(); // For manually logged-in users
            return loginDAO.findByEmail(email); // Find the user by email in the database
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while fetching logged-in user details: " + e.getMessage(), e);
        }
    }

    private String getEmailFromOAuth2User(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        // Check for common attribute names used by different OAuth2 providers
        if (attributes.containsKey("email")) {
            return (String) attributes.get("email");
        } else if (attributes.containsKey("mail")) {
            return (String) attributes.get("mail");
        } else if (attributes.containsKey("userPrincipalName")) {
            return (String) attributes.get("userPrincipalName"); // For some Microsoft Azure setups
        } else if (attributes.containsKey("preferred_username")) {
            return (String) attributes.get("preferred_username"); // For some OpenID setups
        }
        // Add more cases if necessary, depending on OAuth providers you are using
        return null; // If no email is found
    }

    
    @Override
    public boolean isFirstLogin(String email) {
        LoginMaster user = loginDAO.findByEmail(email);
        return user != null && user.isFirstLogin();
    }

    @Override
    @Transactional
    public LoginMaster findByEmail(String email) {
        try {
            // Fetch the user by email from the loginDAO
            return loginDAO.findByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while fetching the user by email: " + e.getMessage(), e);
        }
    }

}
