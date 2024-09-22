package com.revshop.master;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

@Entity
@Table(name = "tbl_favorite")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FavoriteMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int favoriteId;

    @ManyToOne(fetch = FetchType.EAGER)  // Temporarily use EAGER for debugging
    @JoinColumn(name = "userid", referencedColumnName = "userId", nullable = false)
    @JsonBackReference
    private UserMaster user;

    @ManyToOne(fetch = FetchType.EAGER)  // Temporarily use EAGER for debugging
    @JoinColumn(name = "productid", referencedColumnName = "productId", nullable = false)
    private ProductMaster product;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "status", length = 45, nullable = true)
    private String status = "Not Shipped Yet";
}
