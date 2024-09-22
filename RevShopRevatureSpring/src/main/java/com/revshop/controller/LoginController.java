package com.revshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.revshop.master.LoginMaster;
import com.revshop.service.LoginService;
import com.revshop.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@Autowired
	private LoginService loginService;

	@GetMapping("/login")
	public String showLoginPage() {
		return "LoginAndRegistration/user-login";
	}

	@GetMapping("/loginSuccess")
	public String loginSuccessHandler(Model model) {
		// Get the logged-in user's email
		LoginMaster loggedInUser = loginService.getLoggedInUserDetails();

		if (loginService.isFirstLogin(loggedInUser.getEmail())) {
//			model.addAttribute("email", loggedInUser.getEmail());
			return "redirect:/detailregistrationpage";
		} else {
			model.addAttribute("email", loggedInUser.getEmail());
			return "redirect:/home";
		}
	}

//	@GetMapping("/home")
//	public String homePage(Model model) {
//		LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
//		model.addAttribute("email", loggedInUser.getEmail());
//		return "home";
//	}

//	@GetMapping("/getUsers")
//	public String getAllUser(Model model) {
//		List<LoginMaster> users = loginService.getAllUser();
//		for (LoginMaster user : users) {
//			System.out.print(user);
//		}
//		return "redirect:/register";
//	}
//
//	@GetMapping("/deleteUser/{userId}")
//	public String deleteUser(@PathVariable("userId") int userId) {
//		loginService.deleteUser(userId); // Call service to delete the user
//		return "redirect:/getUsers"; // Redirect to the user list page after deletion
//	}

}
