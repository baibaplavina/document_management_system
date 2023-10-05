package com.example.documentmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministratorService {

    @Autowired
    public AdministratorRepository administratorRepository;

    List<Administrator> findAll() {
        return administratorRepository.findAll();

    }
}
