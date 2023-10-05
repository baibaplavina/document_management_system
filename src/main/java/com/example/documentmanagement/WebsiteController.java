package com.example.documentmanagement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class WebsiteController {
    private AdministratorService administratorService;

    @GetMapping("/")
    public String displayHomePage(){
        return"index";
    }
    @GetMapping("/create-process")
    public String displayCreateNewProcessPage(){
        return "createProcess";
    }
    @GetMapping("/view-processes")
    public String displayAllProcessesPage(){
        return "viewProcessesList";
    }
    @GetMapping("/create-document")
    public String displayCreateDocumentPage(){
        return "processDocumentPage";
    }

    @GetMapping("/create-administrator")
    public String displayCreateAdministrator(){
        return "createAdministrator";
    }

    @PostMapping("/create-administrator")
    public String handleAdministratorRegistration(Administrator administrator){

        System.out.println(administrator);
        try {
           administratorService.createAdministrator(administrator);
            return "redirect:/login?status=ADMINISTRATOR_REGISTRATION_SUCCESS";
        } catch (Exception exception) {
            return "redirect://register?status=ADMINISTRATOR_REGISTRATION_FAILED&error=" + exception.getMessage();
        }
    }

}
