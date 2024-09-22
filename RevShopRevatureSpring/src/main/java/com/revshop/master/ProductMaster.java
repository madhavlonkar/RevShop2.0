package com.revshop.master;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
import lombok.ToString;

@Entity
@Table(name = "tbl_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"seller"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProductMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @Column(name = "productName", nullable = false)
    private String productName;

    @Column(name = "productDescription", columnDefinition = "TEXT", nullable = false)
    private String productDescription;

    @Column(name = "productPrice", nullable = false)
    private double productPrice;

    @Column(name = "productDiscount")
    private double productDiscount;

    @Column(name = "productStock", nullable = false)
    private int productStock;

    @Column(name = "productBrand", nullable = false)
    private String productBrand;

    @Column(name = "productCategory", nullable = false)
    private String productCategory;

    @Column(name = "productStatus", nullable = false)
    private String productStatus;

    @Column(name = "productImage", columnDefinition = "TEXT")
    private String productImage;  // Path to the image

    @Column(name = "threshold", nullable = false)
    private int threshold;
    
    @Column(name = "productTags", length = 255)
    private String productTags;

    // Many-to-One relationship with UserMaster (seller)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerId", referencedColumnName = "userId", nullable = false)
    private UserMaster seller;
}
