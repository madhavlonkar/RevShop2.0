package com.revshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.revshop.master.LoginMaster;
import com.revshop.master.OrderMaster;
import com.revshop.service.LoginService;
import com.revshop.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private LoginService loginService;

    // Fetch and display orders for the logged-in user or seller
    @GetMapping("/myOrders")
    public String showUserOrders(Model model, @RequestParam(name = "view", required = false, defaultValue = "seller") String view) {
        // Get the logged-in user
        LoginMaster user = loginService.getLoggedInUserDetails();
        String role = user.getRole();

        List<OrderMaster> orders;
        // Determine which orders to show based on the view parameter
        if ("personal".equals(view)) {
            // Show orders placed by the seller as a buyer
            orders = orderService.getOrdersForUser(user.getUserId());
        } else if (role.equals("ROLE_seller")) {
            // Show orders for products related to the seller
            orders = orderService.getOrdersForSeller(user.getUserId());
        } else {
            // Default: Fetch orders for the logged-in user (buyer)
            orders = orderService.getOrdersForUser(user.getUserId());
        }

        // Add orders and user details to the model
        model.addAttribute("orders", orders);
        model.addAttribute("userDetails", user);
        model.addAttribute("view", view);  // To toggle between views

        return "my-orders"; // JSP view name (my-orders.jsp)
    }

    // Update order status by seller
    @PostMapping("/updateStatus")
    public String updateOrderStatus(@RequestParam("orderId") Long orderId, 
                                    @RequestParam("status") String status, Model model) {
        // Update the order status
        orderService.updateOrderStatus(orderId, status);

        // Redirect back to the order page
        return "redirect:/orders/myOrders";
    }
}
