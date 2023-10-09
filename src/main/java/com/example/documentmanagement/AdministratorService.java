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

    public Administrator createAdministrator(Administrator administrator) throws Exception {
        if (administrator.getAdminName().isEmpty() || administrator.getAdminSurname().isEmpty() ||
                administrator.getCertificateNumber().isEmpty() || administrator.getAdminEmail().isEmpty() ||
                administrator.getAdminAddress().isEmpty() || administrator.getAdminGender().toString() == null)
            throw new Exception("All lines should be filled in, please check");
        administratorRepository.saveAndFlush(administrator);
        return administrator;
    }


    public Administrator findAdministratorByCertificateNumber(int number) throws Exception {

        for (Administrator admin : administratorRepository.findAll()) {
            if (admin.getCertificateNumber().equals(number))
                return administratorRepository.findByCertificateNumber(number);
        }
        throw new Exception("Administrator not found");

    }

    public Administrator findAdministratorById(Long id) throws Exception {

        for (Administrator admin : administratorRepository.findAll()) {
            if (admin.getAdminId().equals(id))
                return administratorRepository.findById(id).get();
        }
        throw new Exception("Administrator not found");

    }


}
