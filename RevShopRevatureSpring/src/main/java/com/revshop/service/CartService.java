package com.revshop.service;

import java.util.List;

import com.revshop.dto.CartItemDTO;
import com.revshop.master.CartMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.UserMaster;

public interface CartService {
    CartMaster addProductToCart(UserMaster user, ProductMaster product, int quantity);
    List<CartMaster> getCartItemsForUser(UserMaster user);
    boolean removeCartItem(int cartId); // New method to remove item from the cart
	void updateCart(CartMaster cartItem);
	CartMaster getCartItemForUserAndProduct(UserMaster user, ProductMaster product);
	CartMaster updateProductQuantity(UserMaster user, ProductMaster product, int quantityChange);
	List<CartItemDTO> convertToDTO(List<CartMaster> cartItems);
}
