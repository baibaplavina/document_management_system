package com.example.documentmanagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Date courtDecisionDate;
    private String e_address;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    @ManyToOne
    @JoinColumn(name="adminId", nullable=false)
    private Administrator admin;

}
