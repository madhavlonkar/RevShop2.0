package com.revshop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.revshop.master.LoginMaster;
import com.revshop.service.LoginService;

@Controller
public class OAuth2LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/oauth2LoginSuccess")
    public String oauth2LoginSuccessHandler(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Extract email from OAuth2 attributes, different providers may use different keys
        String email = getEmailFromOAuth2User(oAuth2User);

        if (email == null) {
            throw new IllegalStateException("Email not found from OAuth2 provider");
        }

        LoginMaster loggedInUser = loginService.findByEmail(email);

        if (loginService.isFirstLogin(loggedInUser.getEmail())) {
            model.addAttribute("user", loggedInUser);
            return "redirect:/detailregistrationpage";
        } else {
            model.addAttribute("email", loggedInUser.getEmail());
            return "redirect:/home";
        }
    }

    private String getEmailFromOAuth2User(OAuth2User oAuth2User) {
        // Check for common attribute names used by different OAuth2 providers
        Map<String, Object> attributes = oAuth2User.getAttributes();
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
        return null; // If no email found
    }
}
