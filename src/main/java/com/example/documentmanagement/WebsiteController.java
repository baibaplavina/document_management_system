package com.example.documentmanagement;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;


@Controller
public class WebsiteController {

    @GetMapping("/")
    public String displayHomePage(Model model){
        model.addAttribute("date", new Date());


        return"index";
    }


}
