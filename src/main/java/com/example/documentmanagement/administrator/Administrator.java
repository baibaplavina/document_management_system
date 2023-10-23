package com.example.documentmanagement.administrator;

import com.example.documentmanagement.insolvencyProcess.InsolvencyProcess;
import jakarta.persistence.*;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity

public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long adminId;

    @Column(name = "certificateNumber")
    @Pattern(regexp = "([0-9]{5})", message = "certificate number should contain 5 numbers, please fill in")
    private String certificateNumber;
    @Column(name = "adminName")
    @NotEmpty(message = "To fill name is mandatory")
    private String adminName;
    @Column(name = "adminSurname")
    @NotEmpty(message = "To fill surname is mandatory")
    private String adminSurname;
    @Column(name = "adminAddress")
    @NotEmpty(message = "To fill address is mandatory")
    private String adminAddress;
    @Column(name = "adminEmail")
    @NotEmpty(message = "To fill e-mail is mandatory")
    private String adminEmail;
    @Column(name = "adminE_address")
    @NotEmpty(message = "To fill E-address is mandatory")
    private String adminE_address;
    @Column(name = "adminPhoneNumber")
    @NotEmpty(message = "To fill phone number is mandatory")
    private String adminPhoneNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "adminGender")
    @NotNull
    private Gender adminGender;
    private Timestamp lastUpdated;
    private Timestamp createdAt;
    @OneToMany
    private List<InsolvencyProcess> listOfProcesses;
    @Column(name = "place")
    @NotEmpty(message = "To fill place is mandatory")
    private String place;
    @PrePersist
    public void beforeSave() {
        this.createdAt = new Timestamp(System.currentTimeMillis());


    }
    @PostPersist
    public void upDating(){
        this.lastUpdated= new Timestamp(System.currentTimeMillis());
    }

}
