package com.example.documentmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        List<Administrator> adminList=administratorRepository.findAll();
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        model.addAttribute("process", new InsolvencyProcess());
        model.addAttribute("adminList", adminList);

        return "createProcess";
    }

    @PostMapping("/create-process")
    public String createProcessPage(InsolvencyProcess insolvencyProcess) {
        try {
            insolvencyProcessRepository.save(insolvencyProcess);

            return "redirect:/create-process?message=INSOLVENCY_COMPANY_CREATED_SUCCESSFULLY";
        } catch (Exception exception) {
            return "redirect:/create-process?message=INSOLVENCY_COMPANY_CREATION_FAILED&error=" + exception.getMessage();
        }

    }

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
}
