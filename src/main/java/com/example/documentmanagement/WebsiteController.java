package com.example.documentmanagement;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class WebsiteController {


    @Autowired
    private AdministratorService administratorService;

    @GetMapping("/")
    public String displayHomePage(){
        return"index";
    }


}
