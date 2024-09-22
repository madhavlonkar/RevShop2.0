package com.revshop.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revshop.dao.CartDAO;
import com.revshop.dto.CartItemDTO;
import com.revshop.master.CartMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.CartService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDAO cartDAO;

    @Override
    public CartMaster addProductToCart(UserMaster user, ProductMaster product, int quantity) {
        CartMaster existingCartItem = cartDAO.findByUserAndProduct(user, product);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            return cartDAO.saveOrUpdateCart(existingCartItem);
        } else {
            CartMaster newCartItem = new CartMaster();
            newCartItem.setUser(user);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            return cartDAO.saveOrUpdateCart(newCartItem);
        }
    }

    @Override
    public List<CartMaster> getCartItemsForUser(UserMaster user) {
        return cartDAO.findByUser(user);
    }

    @Override
    public boolean removeCartItem(int cartId) {
        CartMaster cartItem = cartDAO.findById(cartId);
        if (cartItem != null) {
            cartDAO.delete(cartItem);
            return true;
        }
        return false;
    }

    @Override
    public void updateCart(CartMaster cartItem) {
        // Update the existing cart item
        cartDAO.saveOrUpdateCart(cartItem);
    }

    @Override
    public CartMaster getCartItemForUserAndProduct(UserMaster user, ProductMaster product) {
        // Retrieve the cart item for a specific user and product
        return cartDAO.findByUserAndProduct(user, product);
    }
    
    @Override
    public CartMaster updateProductQuantity(UserMaster user, ProductMaster product, int quantityChange) {
        CartMaster existingCartItem = cartDAO.findByUserAndProduct(user, product);

        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + quantityChange;

            // If new quantity is less than or equal to 0, remove the product from the cart
            if (newQuantity <= 0) {
                cartDAO.delete(existingCartItem);
                return null; // Indicate that the item was removed
            }

            // If new quantity exceeds the available stock, log it and return null
            if (newQuantity > product.getProductStock()) {
                // Log the error
                System.out.println("Insufficient stock for product: " + product.getProductName());
                return null; // No update made
            }

            // Otherwise, update the quantity
            existingCartItem.setQuantity(newQuantity);
            return cartDAO.saveOrUpdateCart(existingCartItem);
        }

        return null;
    }
    
    public List<CartItemDTO> convertToDTO(List<CartMaster> cartItems) {
        return cartItems.stream().map(item -> new CartItemDTO(
            item.getCartId(),
            item.getProduct().getProductId(),      // Product ID
            item.getProduct().getProductName(),    // Product Name
            item.getProduct().getProductPrice(),   // Product Price
            item.getProduct().getProductImage(),   // Product Image
            item.getProduct().getProductDiscount(),// Product Discount
            item.getQuantity(),                    // Quantity
            item.getStatus()                       // Status
        )).collect(Collectors.toList());
    }


}
