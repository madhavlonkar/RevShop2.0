package com.revshop.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.revshop.dao.LoginDAO;
import com.revshop.master.LoginMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.LoginService;
import com.revshop.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final LoginDAO loginDAO;
    private final UserService userService;
    
    public CustomOAuth2UserService(LoginDAO loginDAO, UserService userService) {
        this.loginDAO = loginDAO;
        this.userService = userService;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Extract user information from OAuth2 provider
        String email = getEmailFromOAuth2User(oAuth2User);
        
        if (email == null) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }

        LoginMaster user = loginDAO.findByEmail(email);
        
        // If user does not exist, create and register a new one
        if (user == null) {
            // Create a new LoginMaster object
            user = new LoginMaster();
            user.setEmail(email);
            user.setFirstLogin(true); // Mark as first login
            
            // Save the user details in UserMaster first
            UserMaster userMaster = new UserMaster();
            userMaster.setEmail(email);
            UserMaster savedUser = userService.saveUser(userMaster); // Save to UserMaster
            
            // Set the saved UserMaster object in LoginMaster
            user.setUserId(savedUser);
            user.setRole("buyer"); // Set default role for OAuth2 login

            // Save the user in LoginMaster
            loginDAO.insert(user);
        }

        // Return the OAuth2User with authorities (roles)
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole())); // Add user role to authorities
        
        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "email");
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

}
