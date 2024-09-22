package com.revshop.master;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "tbl_review")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "product")
public class ReviewMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", referencedColumnName = "productId", nullable = false)
    private ProductMaster product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false)
    private UserMaster user;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "reviewContent", columnDefinition = "TEXT")
    private String reviewContent;

    @Column(name = "reviewDate", nullable = false)
    private Date reviewDate;
}
