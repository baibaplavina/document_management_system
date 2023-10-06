package com.example.documentmanagement;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class WebsiteController {


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



}
