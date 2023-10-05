package com.example.documentmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsolvencyCompanyService {
    @Autowired
    public InsolvencyCompanyRepository insolvencyCompanyRepository;

    List<InsolvencyCompany> findAll() {
        return insolvencyCompanyRepository.findAll();

    }
}
