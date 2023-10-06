package com.example.documentmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsolvencyCompanyService {

    public InsolvencyCompanyRepository insolvencyCompanyRepository;
    @Autowired
    public InsolvencyCompanyService(InsolvencyCompanyRepository insolvencyCompanyRepository){
        this.insolvencyCompanyRepository=insolvencyCompanyRepository;
    }

    List<InsolvencyCompany> findAll() {
        return insolvencyCompanyRepository.findAll();

    }

    public InsolvencyCompany createInsolvencyCompany(InsolvencyCompany insolvencyCompany) throws Exception {
        if (insolvencyCompany.getRegistrationNumber()==0 ||
                insolvencyCompany.getCompanyName().isEmpty()
                || insolvencyCompany.getCompanyAddress().isEmpty()
                || insolvencyCompany.getCourtName().isEmpty()
                || insolvencyCompany.getCourtCaseNumber().isEmpty()
                || insolvencyCompany.getE_address().isEmpty())
            throw new Exception("Some information is missing, please re-fill the form");
        else {
            insolvencyCompanyRepository.saveAndFlush(insolvencyCompany);
            System.out.println(insolvencyCompany);

        }
        return insolvencyCompany;
    }
}
