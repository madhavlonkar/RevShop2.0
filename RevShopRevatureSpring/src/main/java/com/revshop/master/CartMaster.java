package com.revshop.master;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;

    // Many-to-One relationship with UserMaster (buyer)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false)
    private UserMaster user;

    // Many-to-One relationship with ProductMaster
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", referencedColumnName = "productId", nullable = false)
    private ProductMaster product;

    // Quantity of the product in the cart
    @Column(name = "quantity", nullable = false)
    private int quantity;

    // Status of the cart item, for example, "pending" or "ordered"
    @Column(name = "status", length = 45, nullable = false)
    private String status = "Not Shipped Yet";  // Default status
}
