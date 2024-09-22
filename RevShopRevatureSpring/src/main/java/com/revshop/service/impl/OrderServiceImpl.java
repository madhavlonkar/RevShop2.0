package com.revshop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revshop.dao.OrderDAO;
import com.revshop.master.OrderMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Override
    public List<OrderMaster> getOrdersForUser(UserMaster user) {
        return orderDAO.findByUser(user);
    }

    @Override
    public boolean hasUserPurchasedProduct(UserMaster user, int productId) {
        // Delegate the check to the DAO method
        return orderDAO.hasUserPurchasedProduct(user, productId);
    }
    
    @Override
    public void updateOrderStatus(Long orderId, String status) {
        // Find the order by ID
        OrderMaster order = (OrderMaster) orderDAO.findById(orderId);
        
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        // Update the status
        order.setOrderStatus(status);

        // Save the updated order
        orderDAO.saveOrder(order);
    }

    // New method to get orders related to the seller's products
    @Override
    public List<OrderMaster> getOrdersForSeller(UserMaster user) {
        return orderDAO.findOrdersBySeller(user);
    }
}
