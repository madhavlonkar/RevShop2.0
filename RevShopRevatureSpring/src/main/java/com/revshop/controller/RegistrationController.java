package com.revshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.revshop.config.CustomUserDetailsService;
import com.revshop.exceptionHandler.EmailAlreadyExistsException;
import com.revshop.exceptionHandler.UsernameAlreadyExistsException;
import com.revshop.master.LoginMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.LoginService;
import com.revshop.service.UserService;

@Controller
public class RegistrationController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private UserService userService;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@GetMapping("/register")
	public String showRegistrationPage(Model model) {
		model.addAttribute("user", new LoginMaster());
		return "LoginAndRegistration/user-register";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user") LoginMaster user, Model model) {
		try {

			boolean isRegistered = loginService.registerUser(user);

			if (!isRegistered) {
				throw new Exception("Registration failed due to unknown reasons.");
			}

			return "redirect:/login";
		} catch (UsernameAlreadyExistsException e) {
			model.addAttribute("RegistererrorMessage", e.getMessage());
			return "LoginAndRegistration/user-register"; // Stay on registration page
		} catch (EmailAlreadyExistsException e) {
			model.addAttribute("RegistererrorMessage", e.getMessage());
			return "LoginAndRegistration/user-register"; // Stay on registration page
		} catch (Exception e) {
			model.addAttribute("RegistererrorMessage", "An error occurred during registration: " + e.getMessage());
			return "LoginAndRegistration/user-register"; // Stay on registration page
		}
	}

	@GetMapping("/detailregistrationpage")
	public String showDetailRegistrationPage(Model model) {
		LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
		model.addAttribute("user", loggedInUser);
		return "LoginAndRegistration/detail-registration";
	}

	@PostMapping("/completeDetailRegistration")
	public String completeDetailRegistration(@ModelAttribute("user") UserMaster user,@RequestParam("role") String role) {
		userService.updateDetails(user,role);
		return "redirect:/home";
	}
}
