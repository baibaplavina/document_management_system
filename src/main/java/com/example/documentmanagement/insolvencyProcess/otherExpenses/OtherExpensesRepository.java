package com.example.documentmanagement.insolvencyProcess.otherExpenses;

import com.example.documentmanagement.insolvencyProcess.InsolvencyProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtherExpensesRepository extends JpaRepository<OtherExpenses, Long> {

    List<OtherExpenses> findByInsolvencyProcess(InsolvencyProcess insolvencyProcess);

    List<OtherExpenses> findByInsolvencyProcessAndAssetType(InsolvencyProcess insolvencyProcess, String assetType);


}
