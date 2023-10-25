package com.example.documentmanagement.insolvencyProcess;

import com.example.documentmanagement.administrator.Administrator;
import com.example.documentmanagement.administrator.AdministratorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class InsolvencyProcessController {
    @Autowired
    private InsolvencyProcessService insolvencyProcessService;
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
        model.addAttribute("page", findPaginated(1, "id", "asc", model));
        return "createProcess";
    }

    @PostMapping("/create-process")
    public String createProcessPage(@Valid @ModelAttribute("process") InsolvencyProcess insolvencyProcess,
                                    BindingResult result, Model model) {
        try {
            if(result.hasErrors()){
//                session.setAttribute("process", insolvencyProcess);
                model.addAttribute("process", insolvencyProcess);
                return "createProcess";
            }
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
    public String editProcess(@PathVariable Long id, @Valid @ModelAttribute("process")  InsolvencyProcess insolvencyProcess,
                              BindingResult result, Model model) {
        try {
            List<Administrator> adminList = administratorRepository.findAll();
            if(result.hasErrors()){
                model.addAttribute("admin", adminList);
                return "editProcess";
            }
            insolvencyProcess.setId(id);
            insolvencyProcessService.editInsolvencyProcess(insolvencyProcess);
            System.out.println(insolvencyProcess);
            model.addAttribute("admin", adminList);
            return "redirect:/view-processes/all?message=INSOLVENCY_PROCESS_EDITED_SUCCESSFULLY";
        } catch (Exception exception) {
            return "redirect:/?message=INSOLVENCY_PROCESS_EDIT_FAILED&error=";
        }
    }

    @GetMapping("/view-processes/{status}")
    public String searchActive(@RequestParam(required = false) String message,
                               @RequestParam(required = false) String error,
                               @PathVariable String status,
                               Model model) {

        model.addAttribute("status",status);
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        //return "viewProcessesList";
        return findPaginated(1, "id", "asc", model);


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
    public String deleteProcess(@PathVariable Long id) {
        try {
            insolvencyProcessRepository.deleteById(id);
            return "redirect:/view-processes/all?message=PROCESS_DELETED_SUCCESSFULLY";
        } catch (Exception ex) {
            return "redirect:/view-processes/all?message=PROCESS_DELETE_FAILED&error=";
        }
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 5;
        Page<InsolvencyProcess> page = insolvencyProcessService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<InsolvencyProcess> processList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDirection", sortDir.equals("asc") ? "desc" : "asc");
        String status = model.getAttribute("status") == null ?
                "all" :
                (String) model.getAttribute("status");

        if(status.equals("completed")){
            List<InsolvencyProcess>processes=insolvencyProcessService.findAll().stream()
                    .filter(process -> process.getCaseClosingDate() != null)
                    .collect(Collectors.toList());
            System.out.println(processes);

            model.addAttribute("processList", processes);
        } else if (status.equals("active")) {

            model.addAttribute("processList", processList.stream()
                    .filter(process -> process.getCaseClosingDate() == null)
                    .collect(Collectors.toList()));
        } else{
            model.addAttribute("processList", processList);
        }
        return "viewProcessesList";
    }

}
