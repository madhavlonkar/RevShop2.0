package com.revshop.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.revshop.master.LoginMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.ReviewMaster;
import com.revshop.service.LoginService;
import com.revshop.service.OrderService; // Import OrderService or the equivalent service for purchase checking
import com.revshop.service.ProductService;
import com.revshop.service.ReviewService;

@Controller
public class ReviewController {

    @Autowired
    private ProductService productService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private OrderService orderService; // Inject OrderService to check purchases

    // Display Product Details and Reviews
    @GetMapping("/product/{productId}")
    public String showProductDetails(@PathVariable("productId") int productId, Model model) {
        // Fetch product details by productId
        ProductMaster product = productService.getProductById(productId);

        if (product == null) {
            model.addAttribute("errorMessage", "Product not found.");
            return "error-page"; // Return an error page if the product is not found
        }

        // Get the logged-in user's details using the loginService
        LoginMaster loggedInUser = loginService.getLoggedInUserDetails();

        // Fetch reviews for the product
        List<ReviewMaster> reviews = reviewService.getReviewsByProductId(productId);

        // Check if the user has purchased the product
        boolean hasPurchased = loggedInUser != null && orderService.hasUserPurchasedProduct(loggedInUser.getUserId(), productId);

        // Add product, user, reviews, and purchase status to the model
        model.addAttribute("product", product);
        model.addAttribute("user", loggedInUser);
        model.addAttribute("reviews", reviews);
        model.addAttribute("hasPurchased", hasPurchased);

        return "productDetails"; // Return the JSP page for product details
    }

    // Submit a Review
    @PostMapping("/product/{productId}/review")
    public String submitReview(
        @PathVariable("productId") int productId,
        @RequestParam("rating") int rating,
        @RequestParam("reviewContent") String reviewContent,
        Model model) {

        // Get the logged-in user's details using the loginService
        LoginMaster loggedInUser = loginService.getLoggedInUserDetails();

        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login page if the user is not logged in
        }

        // Check if the user has purchased the product
        boolean hasPurchased = orderService.hasUserPurchasedProduct(loggedInUser.getUserId(), productId);
        if (!hasPurchased) {
            model.addAttribute("errorMessage", "You need to purchase the product first before leaving a review.");
            return "productDetails"; // Redirect back to the product page with an error message
        }

        // Fetch the product by productId
        ProductMaster product = productService.getProductById(productId);

        if (product == null) {
            model.addAttribute("errorMessage", "Product not found.");
            return "error-page"; // Return an error page if the product is not found
        }

        // Create a new review
        ReviewMaster review = new ReviewMaster();
        review.setProduct(product);
        review.setUser(loggedInUser.getUserId()); // Set the entire user object, not just the userId
        review.setRating(rating);
        review.setReviewContent(reviewContent);
        review.setReviewDate(new Date()); // Set the current time for the review as java.util.Date

        // Save the review using the ReviewService
        reviewService.saveReview(review);

        return "redirect:/product/" + productId; // Redirect back to the product page after submission
    }
}
