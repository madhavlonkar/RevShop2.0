package com.revshop.master;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tbl_login")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"userId"})
public class LoginMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int loginId;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false)
    @JsonBackReference
    private UserMaster userId;

    private String email;
    private String password;
    private boolean isFirstLogin;
    private String userName;
    private String role;
    
}
