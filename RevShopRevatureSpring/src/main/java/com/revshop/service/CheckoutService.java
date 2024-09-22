package com.revshop.service;

import java.util.Map;

import com.revshop.master.UserMaster;

public interface CheckoutService {
    // Get details for the checkout page
    boolean placeOrder(UserMaster user, String paymentId, String address, String city, String state, String zip);
}
