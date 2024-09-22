package com.revshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.revshop.master.LoginMaster;
import com.revshop.master.ProductMaster;
import com.revshop.service.LoginService;
import com.revshop.service.ProductService;
import com.revshop.utility.EmailService;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private LoginService loginService;
    
    @Autowired
    private EmailService emailService;

    @GetMapping("/home")
    public String showHomePage(
            @RequestParam(value = "category", required = false, defaultValue = "Electronics") String category,
            @RequestParam(value = "s", required = false) String searchQuery,
            Model model) {

        List<ProductMaster> products;

        // If there's a search query, search for products by name
        if (searchQuery != null && !searchQuery.isEmpty()) {
            products = productService.searchProductsQuery(searchQuery);
        } else {
            // Otherwise, filter by category
            products = productService.getProductsByCategory(category);
        }

        // Add products and the selected category to the model
        model.addAttribute("products", products);
        model.addAttribute("selectedCategory", category);

        // Get the logged-in user details and add to the model
        LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
        model.addAttribute("user", loggedInUser);

        return "index"; // returns the view name (index.jsp)
    }
    
    @GetMapping("/dashboard")
    public String showSellerDashboard(
            @RequestParam(value = "category", required = false, defaultValue = "Electronics") String category,
            @RequestParam(value = "s", required = false) String searchQuery,
            Model model) {

        // Get the logged-in seller details
        LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
        if (loggedInUser == null) {
            model.addAttribute("errorMessage", "User is not logged in.");
            return "error-page";
        }

        List<ProductMaster> products;

        // If there's a search query, search for products by name, but restrict to seller's products
        if (searchQuery != null && !searchQuery.isEmpty()) {
            products = productService.searchProductsBySellerAndQuery(loggedInUser.getUserId().getUserId(), searchQuery);
        } else {
            // Otherwise, filter by category, restricted to seller's products
            products = productService.getProductsBySellerAndCategory(loggedInUser.getUserId().getUserId(), category);
        }

        // Add products, the selected category, and logged-in user to the model
        model.addAttribute("products", products);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("user", loggedInUser);

        // Fetch low-stock products
        List<ProductMaster> lowStockProducts = productService.getLowStockProductsBySellerId(loggedInUser.getUserId().getUserId());
        model.addAttribute("lowStockProducts", lowStockProducts);
        
        // If there are low-stock products, send email alert
        if (!lowStockProducts.isEmpty()) {
            String sellerEmail = loggedInUser.getUserId().getEmail();  
            emailService.sendLowStockAlert(sellerEmail, lowStockProducts);
        }

        return "Seller/sellerDashboard"; // Return the JSP page for the seller dashboard
    }


}

