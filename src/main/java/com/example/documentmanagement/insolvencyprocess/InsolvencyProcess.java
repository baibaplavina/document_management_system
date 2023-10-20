package com.example.documentmanagement.insolvencyprocess;

import com.example.documentmanagement.administrator.Administrator;
import com.example.documentmanagement.otherExpenses.OtherExpenses;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
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
    @NotEmpty(message = "registration number should contain 11 numbers, please fill in.")
    private String registrationNumber;
    @NotEmpty(message = "company name is required, please fill in.")
    private String companyName;
    @NotEmpty(message = "Company address is required, please fill in.")
    private String companyAddress;
    @NotEmpty(message = "court name is required, please fill in.")
    private String courtName;
    @NotEmpty(message = "court case number is required, please fill in.")
    @Length(min = 9, max=9)
    private String courtCaseNumber;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate courtDecisionDate;
    @NotEmpty(message = "E-address should contain 12 symbols, please fill in.")
    private String e_address;
    @DateTimeFormat(pattern = "dd/mm/yyyy")
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
