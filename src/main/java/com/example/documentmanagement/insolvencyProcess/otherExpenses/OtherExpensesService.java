package com.example.documentmanagement.insolvencyProcess.otherExpenses;

import com.example.documentmanagement.insolvencyProcess.InsolvencyProcess;
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
    public void deleteAllByProcess(InsolvencyProcess insolvencyProcess) {
        if(!otherExpensesRepository.findByInsolvencyProcess(insolvencyProcess).isEmpty()) {
       List<OtherExpenses> otherExpenses = otherExpensesRepository.findByInsolvencyProcess(insolvencyProcess);
       for (OtherExpenses exp : otherExpenses) {
           if(exp.getInsolvencyProcess() == insolvencyProcess) {
               otherExpensesRepository.delete(exp);
           }
       }
       }
    }
}
