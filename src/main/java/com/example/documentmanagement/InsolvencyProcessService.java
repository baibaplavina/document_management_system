package com.example.documentmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsolvencyProcessService {
    @Autowired
    public InsolvencyProcessRepository insolvencyProcessRepository;
    @Autowired
    public AdministratorRepository administratorRepository;

    List<InsolvencyProcess> findAll() {
        return insolvencyProcessRepository.findAll();

    }
    public InsolvencyProcess createInsolvencyProcess(InsolvencyProcess insolvencyProcess) throws Exception {
        if (insolvencyProcess.getRegistrationNumber().isEmpty() ||
                insolvencyProcess.getCompanyName().isEmpty()
                || insolvencyProcess.getCompanyAddress().isEmpty()
                || insolvencyProcess.getCourtName().isEmpty()
                || insolvencyProcess.getCourtCaseNumber().isEmpty()
                || insolvencyProcess.getE_address().isEmpty())
            throw new Exception("Some information is missing, please re-fill the form");
            System.out.println(insolvencyProcess);
            insolvencyProcessRepository.saveAndFlush(insolvencyProcess);

        return insolvencyProcess;
    }


    public void deleteInsolvencyProcess(Long id) {

        insolvencyProcessRepository.deleteById(id);
    }

}
