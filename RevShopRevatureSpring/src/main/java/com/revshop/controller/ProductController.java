package com.revshop.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.revshop.master.LoginMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.ReviewMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.LoginService;
import com.revshop.service.ProductService;
import com.revshop.service.ReviewService;
import com.revshop.service.UserService;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private LoginService loginService;

	@Autowired
	private ReviewService reviewService;

	private final String imageUploadDirectory = System.getProperty("user.home")
			+ "\\git\\RevShopSpring\\RevShopRevatureSpring\\src\\main\\resources\\static\\img\\home\\";

	

	@GetMapping("/products/add")
	public String showAddProductPage(Model model) {
		// Get the logged-in seller details
		LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
		if (loggedInUser == null) {
			model.addAttribute("errorMessage", "User is not logged in.");
			return "error-page";
		}

		// Fetch the UserMaster details of the logged-in user
		UserMaster seller = loggedInUser.getUserId(); // Assuming LoginMaster has a OneToOne mapping to UserMaster

		if (seller == null) {
			model.addAttribute("errorMessage", "Seller not found.");
			return "error-page";
		}

		// Prepare the model attributes for the JSP page
		ProductMaster product = new ProductMaster();
		model.addAttribute("product", product);
		model.addAttribute("seller", seller);

		return "Seller/addProduct"; // Return the JSP page for adding a product
	}

	@PostMapping("/products/add")
	public String addProduct(@ModelAttribute("product") ProductMaster product, @RequestParam("sellerId") int sellerId,
			@RequestParam("productImages") MultipartFile productImage, Model model) {

		// Get the logged-in seller
		UserMaster seller = userService.getUserById(sellerId);
		if (seller == null) {
			model.addAttribute("errorMessage", "Seller not found.");
			return "error-page";
		}

		// Handle product image upload
		String imagePath = null;
		if (!productImage.isEmpty()) {
			try {
				String fileName = productImage.getOriginalFilename();
				String fullPath = imageUploadDirectory + fileName;
				File file = new File(fullPath);
				productImage.transferTo(file);
				imagePath = "/img/home/" + fileName;
			} catch (IOException e) {
				model.addAttribute("errorMessage", "Failed to upload image.");
				return "error-page";
			}
		}

		// Set the seller and image path to the product
		product.setSeller(seller);
		product.setProductImage(imagePath);

		// Save the product
		productService.saveProduct(product);

		return "redirect:/dashboard";
	}

	@GetMapping("/products/edit/{id}")
	public String showEditProductPage(@PathVariable("id") int productId, Model model) {
		// Get the logged-in seller details
		LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
		if (loggedInUser == null) {
			model.addAttribute("errorMessage", "User is not logged in.");
			return "error-page";
		}

		// Fetch the product by productId
		ProductMaster product = productService.getProductById(productId);
		if (product == null || product.getSeller().getUserId() != loggedInUser.getUserId().getUserId()) {
			model.addAttribute("errorMessage", "Product not found or you are not authorized to edit this product.");
			return "error-page";
		}

		// Prepare the model attributes for the JSP page
		model.addAttribute("product", product);
		model.addAttribute("seller", loggedInUser.getUserId());

		return "Seller/editProduct"; // Return the JSP page for editing the product
	}

	@PostMapping("/products/update")
	public String updateProduct(@ModelAttribute("product") ProductMaster product,
			@RequestParam("sellerId") int sellerId, @RequestParam("productImages") MultipartFile productImage,
			@RequestParam("currentImageUrl") String currentImageUrl, Model model) {

		// Get the logged-in seller
		UserMaster seller = userService.getUserById(sellerId);
		if (seller == null) {
			model.addAttribute("errorMessage", "Seller not found.");
			return "error-page";
		}

		// Handle image upload or retain the existing image
		String imagePath = currentImageUrl; // Keep the current image by default
		if (!productImage.isEmpty()) {
			try {
				String fileName = productImage.getOriginalFilename();
				String fullPath = imageUploadDirectory + fileName;
				File file = new File(fullPath);
				productImage.transferTo(file);
				imagePath = "/img/home/" + fileName; // New image path
			} catch (IOException e) {
				model.addAttribute("errorMessage", "Failed to upload image.");
				return "error-page";
			}
		}

		// Set the seller and image path to the product
		product.setSeller(seller);
		product.setProductImage(imagePath); // Use the new or existing image

		productService.updateProduct(product);

		return "redirect:/dashboard";
	}

	@PostMapping("/products/delete/{productId}")
	public String deleteProduct(@PathVariable("productId") int productId, Model model) {
		// Get the logged-in seller details
		LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
		if (loggedInUser == null) {
			model.addAttribute("errorMessage", "User is not logged in.");
			return "error-page";
		}

		// Fetch the product by productId
		ProductMaster product = productService.getProductById(productId);
		if (product == null || product.getSeller().getUserId() != loggedInUser.getUserId().getUserId()) {
			model.addAttribute("errorMessage", "Product not found or you are not authorized to delete this product.");
			return "error-page";
		}

		productService.deleteProductById(productId);

		return "redirect:/dashboard"; 
	}
	
	
}
