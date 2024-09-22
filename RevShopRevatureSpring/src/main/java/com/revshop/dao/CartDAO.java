package com.revshop.dao;

import java.util.List;

import com.revshop.master.CartMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.UserMaster;

public interface CartDAO {
    CartMaster findByUserAndProduct(UserMaster user, ProductMaster product);
    List<CartMaster> findByUser(UserMaster user);
    CartMaster findById(int cartId);  // Find cart item by its ID
    void delete(CartMaster cartItem); // Delete cart item
    CartMaster saveOrUpdateCart(CartMaster cartMaster);
    void clearCartByUser(UserMaster user);
}
