package com.example.documentmanagement.otherExpenses;

import com.example.documentmanagement.insolvencyprocess.InsolvencyProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OtherExpensesService {
    @Autowired
    public OtherExpensesRepository otherExpensesRepository;

    public OtherExpenses createOtherExpenses(OtherExpenses otherExpenses) {
        otherExpensesRepository.saveAndFlush(otherExpenses);
        return otherExpenses;
    }
    public List<OtherExpenses> findSecuredAssetsByProcess(InsolvencyProcess insolvencyProcess) {
        return otherExpensesRepository.findByInsolvencyProcessAndAssetType(insolvencyProcess, "Ieķīlātā manta");
    }
    public List<OtherExpenses> findUnsecuredAssetsByProcess(InsolvencyProcess insolvencyProcess) {
        return otherExpensesRepository.findByInsolvencyProcessAndAssetType(insolvencyProcess, "Neieķīlātā manta");
    }
}
