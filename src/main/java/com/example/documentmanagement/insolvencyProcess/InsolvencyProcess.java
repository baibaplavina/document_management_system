package com.example.documentmanagement.insolvencyProcess;

import com.example.documentmanagement.administrator.Administrator;
import com.example.documentmanagement.insolvencyProcess.otherExpenses.OtherExpenses;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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
    @Pattern(regexp = "([0-9]{11})", message = "registration number should contain 11 numbers, please fill in.")
    private String registrationNumber;
    @NotEmpty(message = "company name is required, please fill in.")
    private String companyName;
    @NotEmpty(message = "company address is required, please fill in.")
    private String companyAddress;
    @NotEmpty(message = "court name is required, please fill in.")
    private String courtName;
    @Pattern(regexp = "(C.*[0-9]{8})", message = "court case number contains  letter C and 8 numbers and is required, please fill in.")
    private String courtCaseNumber;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate courtDecisionDate;
    @NotEmpty(message = "e-address is required, please fill in.")
    private String e_address;
    @DateTimeFormat(pattern = "dd/mm/yyyy")
    private Timestamp createdAt;
    private Timestamp updatedAt;
    @ManyToOne
    @JoinColumn(name = "adminId", nullable = false)
    private Administrator admin;
        @OneToMany
    @Cascade(value={CascadeType.ALL})
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
    private String valstsIenemumuApmers;
    private String maksatnespejasKontrolesApmers;
    private Boolean securedAssets;

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
