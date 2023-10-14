package com.example.documentmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsolvencyProcessService {
    @Autowired
    private InsolvencyProcessRepository insolvencyProcessRepository;
    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    public InsolvencyProcessService(InsolvencyProcessRepository insolvencyProcessRepository, AdministratorRepository administratorRepository) {
        this.insolvencyProcessRepository=insolvencyProcessRepository;
        this.administratorRepository=administratorRepository;
    }

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

    public InsolvencyProcess editInsolvencyProcessAssets(InsolvencyProcess process) throws Exception {

        for (InsolvencyProcess currentProcess : insolvencyProcessRepository.findAll()) {

            if (currentProcess.getId().equals(process.getId())) {

                currentProcess.setAssetsList(process.getAssetsList());
                currentProcess.setAssetsListCosts(process.getAssetsListCosts());
                currentProcess.setAssetsTotalCosts(process.getAssetsTotalCosts());
                currentProcess.setSecuredAssets(process.getSecuredAssets());

                insolvencyProcessRepository.save(currentProcess);
                return currentProcess;
            }
        }

        throw new Exception("process not found by id!");

    }

    public InsolvencyProcess findInsolvencyProcessById(Long id) throws Exception {

        for (InsolvencyProcess process : insolvencyProcessRepository.findAll()) {
            if (process.getId().equals(id))
                return insolvencyProcessRepository.findById(id).get();
        }
        throw new Exception("Insolvency process not found");

    }

    public InsolvencyProcess editInsolvencyProcessCosts(InsolvencyProcess process) throws Exception {
        for (InsolvencyProcess currentProcess : insolvencyProcessRepository.findAll()) {

            if (currentProcess.getId().equals(process.getId())) {
                currentProcess.setNeikilataMantaSum(process.getNeikilataMantaSum());
                insolvencyProcessRepository.save(currentProcess);
                return currentProcess;
            }
        }

        throw new Exception("process not found by id!");
    }


    public InsolvencyProcess editInsolvencyProcessMoney(InsolvencyProcess process) throws Exception {
        for (InsolvencyProcess currentProcess : insolvencyProcessRepository.findAll()) {

            if (currentProcess.getId().equals(process.getId())) {
                currentProcess.setProcessMoney(process.getProcessMoney());
                insolvencyProcessRepository.save(currentProcess);
                return currentProcess;
            }
        }

        throw new Exception("process not found by id!");
    }

    public InsolvencyProcess editInsolvencyProcessExpenses(InsolvencyProcess process) throws Exception {
        for (InsolvencyProcess currentProcess : insolvencyProcessRepository.findAll()) {

            if (currentProcess.getId().equals(process.getId())) {
                currentProcess.setTotalExpenses(process.getTotalExpenses());
                currentProcess.setAdminSalary(process.getAdminSalary());
                insolvencyProcessRepository.save(currentProcess);
                return currentProcess;
            }
        }

        throw new Exception("process not found by id!");
    }

    public InsolvencyProcess editInsolvencyProcessCreditors(InsolvencyProcess process) throws Exception {
        for (InsolvencyProcess currentProcess : insolvencyProcessRepository.findAll()) {

            if (currentProcess.getId().equals(process.getId())) {
                currentProcess.setCreditorsList(process.getCreditorsList());

                insolvencyProcessRepository.save(currentProcess);
                return currentProcess;
            }
        }

        throw new Exception("process not found by id!");
    }

}
