package com.revshop.dao;

import java.util.List;

import com.revshop.master.LoginMaster;
import com.revshop.master.OrderDetails;
import com.revshop.master.OrderMaster;
import com.revshop.master.UserMaster;

public interface OrderDAO {
    OrderMaster saveOrder(OrderMaster order);
    void saveOrderDetails(OrderDetails orderDetails);
	List<OrderMaster> findByUser(UserMaster user);
	boolean hasUserPurchasedProduct(UserMaster user, int productId);
	Object findById(Long orderId);
	List<OrderMaster> findOrdersBySeller(UserMaster user);
    
}
