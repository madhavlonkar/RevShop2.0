package com.revshop.master;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_order_details")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderDetailsId;

    // Many-to-One relationship with OrderMaster
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", referencedColumnName = "orderId", nullable = false)
    private OrderMaster order;

    // Many-to-One relationship with ProductMaster
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", referencedColumnName = "productId", nullable = false)
    private ProductMaster product;

    // Quantity of the product in the order
    @Column(name = "quantity", nullable = false)
    private int quantity;

    // Price of the product at the time of ordering (in case the price changes later)
    @Column(name = "priceAtOrder", nullable = false)
    private double priceAtOrder;
}
