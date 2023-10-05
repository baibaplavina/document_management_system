package com.example.documentmanagement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class WebsiteController {
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

}
