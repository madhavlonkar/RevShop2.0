package com.revshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.revshop.master.CartMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.CartService;
import com.revshop.service.CheckoutService;
import com.revshop.service.LoginService;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private CartService cartService;
    
    @Autowired
    private LoginService loginService;

    @GetMapping("/view")
    public String showCheckoutPage(Model model) {
        // Get the logged-in user
        UserMaster user = loginService.getLoggedInUserDetails().getUserId();
        
        // Get the cart items for the user
        List<CartMaster> cartItems = cartService.getCartItemsForUser(user);

        // Calculate total amount
        double cartTotal = cartItems.stream().mapToDouble(item -> (item.getProduct().getProductPrice() - 
            (item.getProduct().getProductPrice() * item.getProduct().getProductDiscount() / 100.0)) * item.getQuantity()).sum();

        model.addAttribute("userDetails", user);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartTotal);

        return "checkout"; // JSP view name
    }

    @PostMapping("/confirm")
    public String processCheckout(String payment_id, String address, String city, String state, String zip, Model model) {
        // Get the logged-in user
        UserMaster user = loginService.getLoggedInUserDetails().getUserId();
        
        // Place the order, process transaction, and handle all related operations
        boolean isOrderPlaced = checkoutService.placeOrder(user, payment_id, address, city, state, zip);

        if (isOrderPlaced) {
            model.addAttribute("message", "Order placed successfully! Payment ID: " + payment_id);
            return "order-confirm";
        } else {
            model.addAttribute("errorMessage", "Failed to place the order.");
            return "checkout";
        }
    }
}
