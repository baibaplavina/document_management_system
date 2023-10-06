package com.example.documentmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministratorService {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    public AdministratorService(AdministratorRepository administratorRepository){
        this.administratorRepository = administratorRepository;
    }

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

    public Administrator editAdministrator(Administrator admin) throws Exception {

        for (Administrator currentAdmin : administratorRepository.findAll()) {

            if (currentAdmin.getAdminId().equals(admin.getAdminId())) {

                currentAdmin.setCertificateNumber(admin.getCertificateNumber());
                currentAdmin.setAdminName(admin.getAdminName());
                currentAdmin.setAdminSurname(admin.getAdminSurname());
                currentAdmin.setAdminAddress(admin.getAdminAddress());
                currentAdmin.setAdminEmail(admin.getAdminEmail());
                currentAdmin.setAdminE_address(admin.getAdminE_address());
                currentAdmin.setAdminPhoneNumber(admin.getAdminPhoneNumber());
                currentAdmin.setAdminGender(admin.getAdminGender());

                administratorRepository.save(currentAdmin);
                return currentAdmin;
            }
        }

        throw new Exception("administrator not found!");

    }


    public void deleteAdministrator(Long id) {

        administratorRepository.deleteById(id);
    }




}