package com.example.documentmanagement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class InsolvencyCompany {
    @Id
    @GeneratedValue()
    private Long id;
    private int registrationNumber;
    private String companyName;
    private String companyAddress;
    private String courtName;
    private String courtCaseNumber;
    private Date courtDecisionDate;
    private String e_address;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
