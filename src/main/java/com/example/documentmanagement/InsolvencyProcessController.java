package com.example.documentmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class InsolvencyProcessController {
    @Autowired
    private InsolvencyProcessService insolvencyProcessService;
    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private InsolvencyProcessRepository insolvencyProcessRepository;


    @GetMapping("/create-process")
    public String displayCreateProcessPage(@RequestParam(required = false) String message,
                                           @RequestParam(required = false) String error,
                                           Model model) {
        List<Administrator> adminList = administratorRepository.findAll();
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        model.addAttribute("process", new InsolvencyProcess());
        model.addAttribute("processList", insolvencyProcessService.findAll());
        model.addAttribute("adminList", adminList);

        return "createProcess";
    }

    @PostMapping("/create-process")
    public String createProcessPage(InsolvencyProcess insolvencyProcess) {
        try {
            insolvencyProcessRepository.save(insolvencyProcess);

            return "redirect:/create-process?message=INSOLVENCY_PROCESS_CREATED_SUCCESSFULLY";
        } catch (Exception exception) {
            return "redirect:/create-process?message=INSOLVENCY_PROCESS_CREATION_FAILED&error=" + exception.getMessage();
        }
    }

    @GetMapping("/edit-process/{id}")
    public String showEditProcessPage(@PathVariable Long id, Model model) {
        try {
            List<Administrator> adminList = administratorRepository.findAll();
            InsolvencyProcess selectedProcess = insolvencyProcessService.findInsolvencyProcessById(id);
            model.addAttribute("process", selectedProcess);
            model.addAttribute("admin", adminList);
            return "editProcess";
        } catch (Exception exception) {
            return "redirect:/?message=PROCESS_EDIT_FAILED&error=" + exception.getMessage();
        }
    }

    @PostMapping("/edit-process/{id}")
    public String editProcess(@PathVariable Long id, Model model, InsolvencyProcess insolvencyProcess) {
        try {
            insolvencyProcess.setId(id);
            System.out.println(insolvencyProcess);
            insolvencyProcessService.editInsolvencyProcess(insolvencyProcess);
            List<Administrator> adminList = administratorRepository.findAll();
            model.addAttribute("admin", adminList);
            return "redirect:/view-processes?message=INSOLVENCY_PROCESS_EDITED_SUCCESSFULLY";
        } catch (Exception exception) {
            return "redirect:/?message=INSOLVENCY_PROCESS_EDIT_FAILED&error=" + exception.getMessage();
        }
    }

    @GetMapping("/view-processes/active")
    public String displayFilteredProcesses(@RequestParam(required = false) String message,
                                           @RequestParam(required = false) String error,
                                           LocalDate closingDate,
                                           Model model) {
        List<InsolvencyProcess> activeProcessList = insolvencyProcessService.findByCaseClosingDate(closingDate);
        List<InsolvencyProcess> inactiveProcessList = insolvencyProcessService.findByCaseClosingDate(closingDate);

        for (InsolvencyProcess currentProcess : insolvencyProcessService.findByCaseClosingDate(closingDate)) {
            if (currentProcess.getCaseClosingDate() == null) {
                activeProcessList.add(currentProcess);
                System.out.println("Active cases" + activeProcessList + "\n");
            } else {
                inactiveProcessList.add(currentProcess);
                System.out.println("Inactive cases" + inactiveProcessList);
            }
            model.addAttribute("active", activeProcessList);
            model.addAttribute("inactive", activeProcessList);
            model.addAttribute("message", message);
            model.addAttribute("error", error);

        }
        return "viewProcessesActive";
    }
//    @GetMapping( "/view-processes/inactive")
//    public String displayFilteredProcessesInactive(@RequestParam(required = false) String message,
//                                           @RequestParam(required = false) String error,
//                                           LocalDate caseClosingDate,
//                                           Model model) {
//        List<InsolvencyProcess> inactiveCases = insolvencyProcessService.sortProcesses(caseClosingDate);
//
//        model.addAttribute("processList", insolvencyProcessService.findAll());
//        model.addAttribute("message", message);
//        model.addAttribute("error", error);
//        return "viewProcessesInactive";
//


    @GetMapping("/view-processes")
    public String displayProcessesList(@RequestParam(required = false) String message,
                                       @RequestParam(required = false) String error,
                                       Model model) {
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        model.addAttribute("processList", insolvencyProcessService.findAll());
        return "viewProcessesList";
    }

    @GetMapping("/process-documents")
    public String displayProcessDocumentPage(@RequestParam(required = false) String message,
                                             @RequestParam(required = false) String error,
                                             Model model) {
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        model.addAttribute("processList", insolvencyProcessService.findAll());
        return "processDocumentPage";
    }

    @GetMapping("/process-documents/{id}")
    public String displayProcessDocumentPage(@PathVariable() Long id, Model model) {

        try {
            InsolvencyProcess process = insolvencyProcessService.findInsolvencyProcessById(id);
            model.addAttribute("process", process);
            return "processDocumentPage";
        } catch (Exception exception) {

            return "redirect:/?message=VIEW_PROCESS_FAILED&error=" + exception.getMessage();
        }


    }
    @GetMapping("/delete-process/{id}")
    public String deleteProcess(@PathVariable Long id){
        try{
            insolvencyProcessRepository.deleteById(id);
            return "redirect:/view-processes?message=PROCESS_DELETED_SUCCESSFULLY";
        } catch(Exception ex){
            return "redirect:/view-processes?message=PROCESS_DELETE_FAILED&error="+ ex.getMessage();
        }
    }
}
