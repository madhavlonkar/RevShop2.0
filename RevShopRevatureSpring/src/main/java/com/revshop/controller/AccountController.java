package com.revshop.controller;

import com.revshop.master.UserMaster;
import com.revshop.service.UserService;
import com.revshop.service.LoginService;
import com.revshop.master.LoginMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @GetMapping("/account")
    public String showAccountPage(Model model) {
        try {
            // Get logged-in user details
            LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
            if (loggedInUser != null) {
                // Fetch user details using email (or userId) from UserService
                UserMaster userDetails = userService.getUserById(loggedInUser.getUserId().getUserId());
                
                // Add userDetails to model to pass to JSP
                model.addAttribute("userDetails", userDetails);
            } else {
                model.addAttribute("errorMessage", "User not logged in");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while fetching user details: " + e.getMessage());
        }

        return "LoginAndRegistration/userAccount"; // Return the view name (account.jsp)
    }
}
