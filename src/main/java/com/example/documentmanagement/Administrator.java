package com.example.documentmanagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "certificateNumber")
    private String certificateNumber;
    @Column(name = "adminName")
    private String adminName;
    @Column(name = "adminSurname")
    private String adminSurname;
    @Column(name = "adminAddress")
    private String adminAddress;
    @Column(name = "adminEmail")
    private String adminEmail;
    @Column(name = "adminE_address")
    private String adminE_address;
    @Column(name = "adminPhoneNumber")
    private String adminPhoneNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "adminGender")
    private Gender adminGender;
    private Timestamp lastUpdated;
    private Timestamp createdAt;

}
