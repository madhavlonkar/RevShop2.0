package com.revshop.master;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_forgetpassword")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetTokenMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true,columnDefinition = "TEXT")
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "login_id", nullable = false)
    private LoginMaster loginMaster;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    public PasswordResetTokenMaster(String token, LoginMaster loginMaster, LocalDateTime expirationTime) {
        this.token = token;
        this.loginMaster = loginMaster;
        this.expirationTime = expirationTime;
    }
    
}
