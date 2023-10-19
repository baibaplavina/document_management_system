package com.example.documentmanagement.insolvencyprocess;

import com.example.documentmanagement.administrator.Administrator;
import com.example.documentmanagement.otherExpenses.OtherExpenses;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class InsolvencyProcess {
    @Id
    @GeneratedValue()
    private Long id;
    @Column(length=11)

    private String registrationNumber;
    private String companyName;
    private String companyAddress;
    private String courtName;
    @Column(length = 9)
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
    @OneToMany
    List<OtherExpenses> otherExpenses;
    private double CreditorsRequest;
    private String assetsList_iekilata;
    private String assetsListCosts_iekilata;
    private String assetsTotalCosts_iekilata;

    private String assetsList_neiekilata;
    private String assetsListCosts_neiekilata;
    private String assetsTotalCosts_neiekilata;

    private double neikilataMantaSum;
    private String processMoney;
    private double totalExpenses;
    private double adminSalary;
    private String creditorsList;
    private Boolean securedAssets;
    private String IzmaksuRasanasDatumsType1;
    private String SegtaSummaType1;
    private String SegsanasDatumsType1;
    private String NavApmaksatasType1;
    private String IzmaksuRasanasDatumsType2;
    private String SegtaSummaType2;
    private String SegsanasDatumsType2;
    private String NavApmaksatasType2;
    private String IzmaksuRasanasDatumsType3;
    private String SegtaSummaType3;
    private String SegsanasDatumsType3;
    private String NavApmaksatasType3;
    private String IzmaksuRasanasDatumsType4;
    private String SegtaSummaType4;
    private String SegsanasDatumsType4;
    private String NavApmaksatasType4;

    private LocalDate caseClosingDate;

    @PrePersist
    public void beforeSave() {

        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
    @PostUpdate
    public void updating(){
        this.updatedAt= new Timestamp(System.currentTimeMillis());
    }


}
