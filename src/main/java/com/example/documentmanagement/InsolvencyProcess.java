package com.example.documentmanagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class InsolvencyProcess {
    @Id
    @GeneratedValue()
    private Long id;

    private String registrationNumber;
    private String companyName;
    private String companyAddress;
    private String courtName;
    private String courtCaseNumber;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate courtDecisionDate;
    private String e_address;
    @DateTimeFormat(pattern = "mm/dd/yyyy")
    private Timestamp createdAt;
    private Timestamp updatedAt;
    @ManyToOne
    @JoinColumn(name = "adminId", nullable = false)
    private Administrator admin;
    private String assetsList;
    private String assetsListCosts;
    private String assetsTotalCosts;
    private double neikilataMantaSum;
    private String processMoney;
    private double totalExpenses;
    private double adminSalary;

}
