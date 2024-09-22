package com.revshop.master;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tbl_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"loginMaster"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name = "firstName", length = 45)
    private String firstName;

    @Column(name = "lastName", length = 45)
    private String lastName;

    @Column(name = "gender", length = 45)
    private String gender;

    @Column(name = "mobile", length = 45)
    private String mobile;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    @Column(name = "email", length = 45, unique = true)  // Set unique constraint here
    private String email;

    @Column(name = "pincode", length = 45)
    private String pincode;

    @Column(name = "billingAddress", columnDefinition = "MEDIUMTEXT")
    private String billingAddress;

    @Column(name = "shippingAddress", columnDefinition = "MEDIUMTEXT")
    private String shippingAddress;

    @Column(name = "bankAccountNo", length = 45)
    private String bankAccountNo;

    @Column(name = "ifsc", length = 45)
    private String ifsc;

    @Column(name = "companyName", length = 45)
    private String companyName;

    @Column(name = "gstNumber", length = 45)
    private String gstNumber;

    @Column(name = "websiteUrl", columnDefinition = "MEDIUMTEXT")
    private String websiteUrl;

    @Column(name = "panNumber", length = 45)
    private String panNumber;

    // Define OneToOne relationship with LoginMaster and cascade on delete
    @OneToOne(mappedBy = "userId", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private LoginMaster loginMaster;  // This ensures LoginMaster entry is deleted when UserMaster is deleted
}
