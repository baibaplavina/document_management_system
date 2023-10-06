package com.example.documentmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsolvencyProcessService {
    @Autowired
    public InsolvencyProcessRepository insolvencyProcessRepository;

    List<InsolvencyProcess> findAll() {
        return insolvencyProcessRepository.findAll();

    }

    public InsolvencyProcess createInsolvencyProcess(InsolvencyProcess insolvencyProcess) throws Exception {
        if (insolvencyProcess.getCompanyName().isEmpty()||insolvencyProcess.getCourtName().isEmpty()
                ||insolvencyProcess.getCompanyAddress().isEmpty())

            throw new Exception("All lines should be filled in, please re-check");
        else {
            insolvencyProcessRepository.save(insolvencyProcess);

        }
        return insolvencyProcess;
    }

    public void deleteInsolvencyProcess(Long id) {

        insolvencyProcessRepository.deleteById(id);
    }

}
