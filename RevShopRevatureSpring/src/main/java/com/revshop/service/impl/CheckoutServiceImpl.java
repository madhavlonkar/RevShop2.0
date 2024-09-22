package com.revshop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revshop.dao.CartDAO;
import com.revshop.dao.OrderDAO;
import com.revshop.dao.ProductDAO;
import com.revshop.master.CartMaster;
import com.revshop.master.OrderDetails;
import com.revshop.master.OrderMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.TransactionMaster;
import com.revshop.master.UserMaster;
import com.revshop.repo.TransactionDAO;
import com.revshop.service.CheckoutService;
import com.revshop.utility.EmailService;

@Service
@Transactional
public class CheckoutServiceImpl implements CheckoutService {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private EmailService emailService;

    @Override
    public boolean placeOrder(UserMaster user, String paymentId, String address, String city, String state, String zip) {
        List<CartMaster> cartItems = cartDAO.findByUser(user);

        if (cartItems == null || cartItems.isEmpty()) {
            return false;  // No items in the cart
        }

        // Calculate total order amount
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> (item.getProduct().getProductPrice() - 
                    (item.getProduct().getProductPrice() * item.getProduct().getProductDiscount() / 100.0)) 
                    * item.getQuantity()).sum();

        // 1. Create and save OrderMaster
        OrderMaster order = new OrderMaster();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setOrderStatus("Pending");
        order.setShippingAddress(address + ", " + city + ", " + state + ", " + zip);
        order.setOrderDate(java.time.LocalDateTime.now());

        // Save OrderMaster and get order id
        OrderMaster savedOrder = orderDAO.saveOrder(order);

        // 2. Save OrderDetails and update product stock
        for (CartMaster cartItem : cartItems) {
            // Save each product in OrderDetails
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(savedOrder);
            orderDetails.setProduct(cartItem.getProduct());
            orderDetails.setQuantity(cartItem.getQuantity());
            orderDetails.setPriceAtOrder(cartItem.getProduct().getProductPrice());
            orderDAO.saveOrderDetails(orderDetails);

            // Update product stock
            ProductMaster product = cartItem.getProduct();
            int newStock = product.getProductStock() - cartItem.getQuantity();
            product.setProductStock(newStock);
            productDAO.updateProduct(product);

            // Fetch seller's email and send email notification
            String sellerEmail = product.getSeller().getEmail();
            emailService.sendEmailOnOrderReceived(sellerEmail, product, cartItem.getQuantity(), user);
        }

        // 3. Record the transaction
        TransactionMaster transaction = new TransactionMaster();
        transaction.setUser(user);
        transaction.setOrder(savedOrder);
        transaction.setPaymentId(paymentId);
        transaction.setAmount(totalAmount);
        transaction.setTransactionStatus("Success");
        transaction.setTransactionDate(java.time.LocalDateTime.now());
        transactionDAO.save(transaction);

        // 4. Clear cart
        cartDAO.clearCartByUser(user);

        // Send email to the buyer
        emailService.sendEmailOnOrderPlaced(user.getEmail(), savedOrder, totalAmount);

        return true;  // Order successfully placed
    }
}
