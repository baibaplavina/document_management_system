package com.example.documentmanagement;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class WebsiteController {


    @Autowired
    private AdministratorService administratorService;


    @GetMapping("/")
    public String displayHomePage(){
        return"index";
    }

    @GetMapping("/create-document")
    public String displayProcessDocumentPage(){

        return "processDocumentPage";
    }
    @GetMapping("/create-administrator")
    public String displayCreateAdminPage(){
        return "administratorProfile";
    }


    @PostMapping("/create-administrator")
    public String handleAdministratorRegistration(Administrator administrator){
        System.out.println(administrator);
        try {
           administratorService.createAdministrator(administrator);
            return "redirect:/register?status=ADMINISTRATOR_REGISTRATION_SUCCESS";
        } catch (Exception exception) {
            return "redirect:/register?status=ADMINISTRATOR_REGISTRATION_FAILED&error=" + exception.getMessage();
        }
    }

}
