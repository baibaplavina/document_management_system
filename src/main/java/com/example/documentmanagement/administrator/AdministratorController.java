package com.example.documentmanagement.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller

public class AdministratorController {
    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private AdministratorRepository administratorRepository;
    @GetMapping("/create-administrator")
    public String displayCreateAdminPage(@RequestParam(required = false) String message,
                                         @RequestParam(required = false) String error,
                                         Model model){
            model.addAttribute("message", message);
            model.addAttribute("error", error);
            model.addAttribute("admin", new Administrator());
            model.addAttribute("adminList", administratorService.findAll());

        return "createAdministrator";
    }


    @PostMapping("/create-administrator")
    public String handleAdministratorRegistration(Administrator administrator){
        try {
            administratorRepository.saveAndFlush(administrator);
            return "redirect:/create-administrator?status=ADMINISTRATOR_REGISTRATION_SUCCESS";
        } catch (Exception exception) {
            return "redirect:/create-administrator?status=ADMINISTRATOR_REGISTRATION_FAILED&error=" + exception.getMessage();
        }
    }
    @GetMapping("/edit-administrator/{id}")
    public String showAdminEditPage(@PathVariable Long id, Model model){
        try{
            Administrator selectedAdmin = administratorService.findAdministratorById(id);
            model.addAttribute("admin", selectedAdmin);
            model.addAttribute("adminList", administratorService.findAll());
            return "editAdministratorProfile";
        }catch( Exception ex){
            return "redirect:/?message=ADMINISTRATOR_EDIT_FAILED&error="+ ex.getMessage();
        }

    }
    @PostMapping ("/edit-administrator/{id}")
    public String editAdmin(@PathVariable Long id, Model model, Administrator administrator){
        try{
            administrator.setAdminId(id);
            administratorService.editAdministrator(administrator);
            List<Administrator> adminList = administratorService.findAll();
            model.addAttribute("adminList", adminList);

            return "redirect:/create-administrator?message=ADMINISTRATOR_EDITED_SUCCESSFULLY";
        }catch( Exception ex){
            return "redirect:/create-administrator?message=ADMINISTRATOR_EDIT_FAILED&error="+ ex.getMessage();
        }

    }
    @GetMapping("/delete-administrator/{id}")
    public String deleteAdmin(@PathVariable Long id){
        try{
            administratorRepository.deleteById(id);
            return "redirect:/create-administrator?message=ADMINISTRATOR_DELETED_SUCCESSFULLY";
        } catch(Exception ex){
            return "redirect:/create-administrator?message=ADMINISTRATOR_DELETE_FAILED&error="+ ex.getMessage();
        }
    }
}
