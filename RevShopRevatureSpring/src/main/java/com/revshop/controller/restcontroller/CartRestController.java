package com.revshop.controller.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revshop.dto.CartItemDTO;
import com.revshop.master.CartMaster;
import com.revshop.master.LoginMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.CartService;
import com.revshop.service.LoginService;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    private CartService cartService;

    @Autowired
    private LoginService loginService;

    @GetMapping(value = "/items", produces = "application/json")
    public ResponseEntity<List<CartItemDTO>> getCartItems() {
        LoginMaster loggedInUser = loginService.getLoggedInUserDetails();
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        UserMaster user = loggedInUser.getUserId();
        List<CartMaster> cartItems = cartService.getCartItemsForUser(user);

        if (cartItems.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Convert CartMaster entities to CartItemDTOs
        List<CartItemDTO> cartItemDTOs = cartService.convertToDTO(cartItems);

        return ResponseEntity.ok(cartItemDTOs); // Return the DTOs in the response
    }
    
    @DeleteMapping("/items/{cartId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable int cartId) {
        boolean isRemoved = cartService.removeCartItem(cartId);

        if (!isRemoved) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Item not found in the cart
        }

        return ResponseEntity.noContent().build(); // Item successfully removed
    }
}
