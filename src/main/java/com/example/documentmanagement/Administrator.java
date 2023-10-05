package com.example.documentmanagement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Administrator {
    @Id
    @GeneratedValue()
    private Long id;
    private int certificateNumber;
    private String adminName;
    private String adminSurname;
    private String adminAddress;
    private String adminEmail;
    private String adminE_address;
    private String adminPhoneNumber;
    private Gender adminGender;



}
