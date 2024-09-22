package com.revshop.service;

import java.util.List;

import com.revshop.master.OrderMaster;
import com.revshop.master.UserMaster;

public interface OrderService {
    // Get orders for a specific user
    List<OrderMaster> getOrdersForUser(UserMaster user);

	boolean hasUserPurchasedProduct(UserMaster userId, int productId);

	void updateOrderStatus(Long orderId, String status);

	List<OrderMaster> getOrdersForSeller(UserMaster user);
}
