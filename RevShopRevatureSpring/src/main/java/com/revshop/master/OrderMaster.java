package com.revshop.master;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_order")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    // Many-to-One relationship with UserMaster (buyer)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false)
    private UserMaster user;

    // Total price of the order
    @Column(name = "totalAmount", nullable = false)
    private double totalAmount;

    // Status of the order, e.g., "Pending", "Shipped", "Delivered"
    @Column(name = "orderStatus", length = 45, nullable = false)
    private String orderStatus = "Pending";  // Default status

    // Timestamp of the order creation
    @Column(name = "orderDate", nullable = false)
    private LocalDateTime orderDate;

    // Shipping address for the order
    @Column(name = "shippingAddress", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String shippingAddress;

    // One-to-Many relationship with OrderDetails (product and quantity)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetails> orderDetails;

    // One-to-One relationship with TransactionMaster
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private TransactionMaster transaction;
}
