package com.revshop.utility;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.revshop.master.OrderMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.UserMaster;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    public void sendEmailOnOrderPlaced(String buyerEmail, OrderMaster order, double totalAmount) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("java88pro@gmail.com");
            helper.setTo(buyerEmail);
            helper.setSubject("Thank You for Your Purchase!");

            String emailContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                    + "<h2 style='color: #4CAF50;'>Thank You for Your Purchase!</h2>"
                    + "<p>Dear <strong>" + order.getUser().getFirstName() + " " + order.getUser().getLastName() + "</strong>,</p>"
                    + "<p>Your order has been successfully placed on <strong>" + order.getOrderDate() + "</strong>. Here are your order details:</p>"
                    + "<table style='width: 100%; border-collapse: collapse;'>"
                    + "<thead><tr style='background-color: #f2f2f2;'>"
                    + "<th style='padding: 10px; border: 1px solid #ddd;'>Order ID</th>"
                    + "<th style='padding: 10px; border: 1px solid #ddd;'>Total Amount</th>"
                    + "<th style='padding: 10px; border: 1px solid #ddd;'>Shipping Address</th>"
                    + "</tr></thead>"
                    + "<tbody><tr>"
                    + "<td style='padding: 10px; border: 1px solid #ddd;'>" + order.getOrderId() + "</td>"
                    + "<td style='padding: 10px; border: 1px solid #ddd;'>$" + totalAmount + "</td>"
                    + "<td style='padding: 10px; border: 1px solid #ddd;'>" + order.getShippingAddress() + "</td>"
                    + "</tr></tbody>"
                    + "</table>"
                    + "<p style='margin-top: 20px;'>We are processing your order and will notify you when it's shipped. If you have any questions, feel free to contact our customer service at <strong>madhavlonkar2@gmail.com</strong>.</p>"
                    + "<p>Thank you for shopping with us!</p>"
                    + "<p style='font-size: 0.9em; color: #888;'>RevShop Team</p>"
                    + "</div>";

            helper.setText(emailContent, true); // Enable HTML content

            mailSender.send(message);
            log.info("Order confirmation email sent to buyer.");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to send email to buyer.");
        }
    }

    public void sendEmailOnOrderReceived(String sellerEmail, ProductMaster product, int quantity, UserMaster buyer) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("java88pro@gmail.com");
            helper.setTo(sellerEmail);
            helper.setSubject("New Order Received for " + product.getProductName());

            String emailContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                    + "<h2 style='color: #FF5733;'>New Order Received</h2>"
                    + "<p>Dear <strong>" + product.getSeller().getFirstName() + "</strong>,</p>"
                    + "<p>We are excited to inform you that a new order has been placed for one of your products. Here are the order details:</p>"
                    + "<table style='width: 100%; border-collapse: collapse;'>"
                    + "<thead><tr style='background-color: #f2f2f2;'>"
                    + "<th style='padding: 10px; border: 1px solid #ddd;'>Product Name</th>"
                    + "<th style='padding: 10px; border: 1px solid #ddd;'>Quantity</th>"
                    + "<th style='padding: 10px; border: 1px solid #ddd;'>Buyer Name</th>"
                    + "</tr></thead>"
                    + "<tbody><tr>"
                    + "<td style='padding: 10px; border: 1px solid #ddd;'>" + product.getProductName() + "</td>"
                    + "<td style='padding: 10px; border: 1px solid #ddd;'>" + quantity + "</td>"
                    + "<td style='padding: 10px; border: 1px solid #ddd;'>" + buyer.getFirstName() + " " + buyer.getLastName() + "</td>"
                    + "</tr></tbody>"
                    + "</table>"
                    + "<p style='margin-top: 20px;'>Please ensure prompt delivery and update the order status accordingly in the system.</p>"
                    + "<p>Thank you for being a valued seller on RevShop.</p>"
                    + "<p style='font-size: 0.9em; color: #888;'>RevShop Seller Support Team</p>"
                    + "</div>";

            helper.setText(emailContent, true); // Enable HTML content

            mailSender.send(message);
            log.info("Order notification email sent to seller.");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to send email to seller.");
        }
    }
    
    public void sendLowStockAlert(String sellerEmail, List<ProductMaster> lowStockProducts) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("java88pro@gmail.com");
            helper.setTo(sellerEmail);
            helper.setSubject("Low Stock Alert for Your Products");

            StringBuilder emailContent = new StringBuilder();
            emailContent.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>")
                    .append("<h2 style='color: #FF5733;'>Low Stock Alert</h2>")
                    .append("<p>Dear Seller,</p>")
                    .append("<p>The following products are below the stock threshold:</p>")
                    .append("<ul>");

            for (ProductMaster product : lowStockProducts) {
                emailContent.append("<li>")
                        .append(product.getProductName())
                        .append(" - Current Stock: ")
                        .append(product.getProductStock())
                        .append(" (Threshold: ")
                        .append(product.getThreshold())
                        .append(")</li>");
            }

            emailContent.append("</ul>")
                    .append("<p>Please restock your products to avoid selling out.</p>")
                    .append("<p style='font-size: 0.9em; color: #888;'>RevShop Team</p>")
                    .append("</div>");

            helper.setText(emailContent.toString(), true); // Enable HTML content

            mailSender.send(message);
            log.info("Low stock alert email sent to seller.");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to send low stock email to seller.");
        }
    }
    
    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("java88pro@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            log.info("Email sent to " + to);

        } catch (Exception e) {
            log.error("Failed to send email", e);
        }
    }
}
