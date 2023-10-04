package com.example.documentmanagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Administrator {
    private int certificateNumber;
    private String adminName;
    private String adminSurname;
    private String adminAddress;
    private String adminEmail;
    private String adminE_address;
    private String adminPhoneNumber;
    private Gender adminGender;

}
