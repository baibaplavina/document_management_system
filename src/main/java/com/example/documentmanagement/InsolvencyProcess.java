package com.example.documentmanagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class InsolvencyProcess {
    @Id
    @GeneratedValue()
    private Long id;
    private int registrationNumber;
    private String companyName;
    private String companyAddress;
    private String courtName;
    private String courtCaseNumber;
    @DateTimeFormat(pattern = "mm/dd/yyyy")
    private Date courtDecisionDate;
    private String e_address;
    @DateTimeFormat(pattern = "mm/dd/yyyy")
    private Timestamp createdAt;
    private Timestamp updatedAt;
    @ManyToOne
    @JoinColumn(name="adminId", nullable=false)
    private Administrator admin;

}
