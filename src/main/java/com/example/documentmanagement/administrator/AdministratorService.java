package com.example.documentmanagement.administrator;

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


    public Administrator findAdministratorById(Long id) throws Exception {

        for (Administrator admin : administratorRepository.findAll()) {
            if (admin.getAdminId().equals(id))
                return administratorRepository.findById(id).get();
        }
        throw new Exception("Administrator not found");

    }

    public Administrator editAdministrator(Administrator administrator) throws Exception {
        for (Administrator currentAdmin : administratorRepository.findAll()) {
            if (currentAdmin.getAdminId().equals(administrator.getAdminId())) {
                currentAdmin.setCertificateNumber(administrator.getCertificateNumber());
                currentAdmin.setAdminName(administrator.getAdminName());
                currentAdmin.setAdminSurname(administrator.getAdminSurname());
                currentAdmin.setAdminAddress(administrator.getAdminAddress());
                currentAdmin.setAdminEmail(administrator.getAdminEmail());
                currentAdmin.setAdminE_address(administrator.getAdminE_address());
                currentAdmin.setAdminPhoneNumber(administrator.getAdminPhoneNumber());
                currentAdmin.setAdminGender(administrator.getAdminGender());
                currentAdmin.setPlace(administrator.getPlace());
                administratorRepository.saveAndFlush(currentAdmin);
                return currentAdmin;
            }
        }
        throw new Exception("Administrator not found, please check Id");


    }
    public void deleteAdministrator(Long id){
        administratorRepository.deleteById(id);
    }
}
