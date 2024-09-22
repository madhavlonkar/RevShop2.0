package com.revshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.revshop.dto.CartItemDTO;
import com.revshop.master.CartMaster;
import com.revshop.master.LoginMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.CartService;
import com.revshop.service.LoginService;
import com.revshop.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private LoginService loginService;

    // Add a product to the cart with a fixed quantity of 1
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") int productId, Model model) {
        // Get the logged-in user details
        LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
        UserMaster user = loggedInUser.getUserId();
        // Fetch the product by its ID
        ProductMaster product = productService.getProductById(productId);

        if (product == null) {
            model.addAttribute("errorMessage", "Product not found.");
            return "error-page";
        }

        // Check if product is in stock
        if (product.getProductStock() < 1) {
            model.addAttribute("errorMessage", "Product is out of stock.");
            return "error-page";
        }

        // Add one quantity of the product to the cart
        CartMaster cartItem = cartService.addProductToCart(user, product, 1);

        if (cartItem == null) {
            model.addAttribute("errorMessage", "Could not add product to cart.");
            return "error-page";
        }

        // Redirect to the cart view or success page
        return "redirect:/cart/view";
    }

    // View cart items
    @GetMapping("/view")
    public String viewCart(HttpServletRequest request, Model model) {
        // Get the logged-in user details
        LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
        if (loggedInUser == null) {
            model.addAttribute("errorMessage", "User not logged in.");
            return "error-page";
        }

        // Get the JSESSIONID from the current session
        String sessionId = request.getSession().getId();

        // API endpoint to fetch the cart items
        String url = "http://localhost:8081/api/cart/items";
        RestTemplate restTemplate = new RestTemplate();

        // Set up headers, including the session ID
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Cookie", "JSESSIONID=" + sessionId);  // Include the session ID as a cookie

        // Create HttpEntity object with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Call the REST API
        try {
            ResponseEntity<List<CartItemDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<CartItemDTO>>() {}
            );

            List<CartItemDTO> cartItems = response.getBody();

            if (cartItems == null || cartItems.isEmpty()) {
                model.addAttribute("message", "No items in the cart.");
                return "cart-view";
            }

            // Calculate the total cart value
            double cartTotal = 0;
            for (CartItemDTO item : cartItems) {
                cartTotal += item.getQuantity() * item.getProductPrice();
            }

            // Add the cart items and total to the model
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("cartTotal", cartTotal);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle any HTTP errors (404, 500, etc.)
            model.addAttribute("errorMessage", "Error fetching cart items: " + e.getStatusCode());
            return "error-page";
        } catch (ResourceAccessException e) {
            // Handle connection errors
            model.addAttribute("errorMessage", "Could not connect to the API: " + e.getMessage());
            return "error-page";
        }

        return "cart-view"; // Return the view for displaying cart items
    }




    // Remove an item from the cart
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("cartId") int cartId, HttpServletRequest request, Model model) {
        String sessionId = request.getSession().getId();
        String url = "http://localhost:8081/api/cart/items/" + cartId;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Cookie", "JSESSIONID=" + sessionId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            model.addAttribute("errorMessage", "Error removing item: " + e.getStatusCode());
            return "error-page";
        } catch (ResourceAccessException e) {
            model.addAttribute("errorMessage", "Could not connect to the API: " + e.getMessage());
            return "error-page";
        }

        // Redirect to the cart view after successful removal
        return "redirect:/cart/view";
    }
    
 // Update product quantity in the cart
    @PostMapping("/update")
    public String updateCartQuantity(
        @RequestParam("productId") int productId, 
        @RequestParam("action") String action, 
        Model model) {

        // Get the logged-in user details
        LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
        UserMaster user = loggedInUser.getUserId();

        // Fetch the product by its ID
        ProductMaster product = productService.getProductById(productId);

        if (product == null) {
            model.addAttribute("errorMessage", "Product not found.");
            return "error-page";
        }

        // Determine whether to increase or decrease the quantity
        int quantityChange = "increase".equals(action) ? 1 : -1;

        try {
            CartMaster updatedCartItem = cartService.updateProductQuantity(user, product, quantityChange);

            if (updatedCartItem == null && quantityChange < 0) {
                model.addAttribute("message", "Product removed from the cart.");
            }
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }

        // Redirect to the cart view
        return "redirect:/cart/view";
    }

}
