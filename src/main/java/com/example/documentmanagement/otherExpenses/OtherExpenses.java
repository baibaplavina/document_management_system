package com.example.documentmanagement.otherExpenses;

import com.example.documentmanagement.insolvencyprocess.InsolvencyProcess;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class OtherExpenses {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long otherExpensesId;
    @ManyToOne
    private InsolvencyProcess insolvencyProcess;
    private String assetType;
    String name;
    private String sanemejs;
    private String creatingDate;
    private String otherDate;
    private double sum;
    private double unpaid;


}
