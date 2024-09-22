package com.revshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.revshop.master.LoginMaster;
import com.revshop.service.PasswordResetService;

@Controller
public class ForgetPasswordController {
	
	@Autowired
	private PasswordResetService passwordResetService;
	
	@GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "LoginAndRegistration/forgot-password";  // Displays the forgot password form
    }

    // Handle Forgot Password submission
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email) {
    	passwordResetService.sendPasswordResetToken(email);
        return "redirect:/forgot-password?success";  // Redirect with success message
    }

    // Reset Password form
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        LoginMaster user = passwordResetService.getByResetToken(token);
        if (user == null) {
            return "redirect:/forgot-password?error";  // Invalid or expired token
        }
        model.addAttribute("token", token);
        return "LoginAndRegistration/reset-password";  // Show password reset form
    }

    // Handle password reset form submission
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token, 
                                       @RequestParam("password") String password) {
    	passwordResetService.resetPassword(token, password);
        return "redirect:/login?resetSuccess";  // Redirect to login with success message
    }

}
