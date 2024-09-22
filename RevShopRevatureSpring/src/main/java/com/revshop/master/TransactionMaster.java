package com.revshop.master;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_transaction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;

    // Many-to-One relationship with UserMaster
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false)
    private UserMaster user;

    // One-to-One relationship with OrderMaster
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", referencedColumnName = "orderId", nullable = false)
    private OrderMaster order;

    // Razorpay payment ID
    @Column(name = "paymentId", length = 100, nullable = false)
    private String paymentId;

    // Total amount paid
    @Column(name = "amount", nullable = false)
    private double amount;

    // Status of the transaction, e.g., "Success", "Failed"
    @Column(name = "transactionStatus", length = 45, nullable = false)
    private String transactionStatus;

    // Timestamp of the transaction
    @Column(name = "transactionDate", nullable = false)
    private LocalDateTime transactionDate;
}
