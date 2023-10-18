package com.example.documentmanagement.documents;

import com.example.documentmanagement.administrator.AdministratorService;
import com.example.documentmanagement.insolvencyprocess.InsolvencyProcessService;
import com.example.documentmanagement.otherExpenses.OtherExpensesService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class DownloadingController {

    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private InsolvencyProcessService insolvencyProcessService;

    @Autowired
    private TemplateService templateService;
    @Autowired
    private DownloadService downloadService;

    @Autowired
    private OtherExpensesService otherExpensesService;

  /*  @GetMapping("/download-blank")
    public void downloadBlank(Model model, HttpServletResponse response) throws IOException {

        DownloadService templateFirst = new DownloadService(administratorService, insolvencyProcessService, otherExpensesService, templateService);

        byte[] xwpfDocumentBytes = templateFirst.exportBlankDoc().toByteArray();


        response.setContentType("application/msword");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = " + "adminBlank.doc";
        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(xwpfDocumentBytes);
        outputStream.close();

    }*/

    @GetMapping("/download-filled")
    public void downloadFilledFile(Model model, HttpServletResponse response) throws IOException {

       // byte[] xwpfDocumentBytes = templateService.exportWordDoc().toByteArray();

        byte[] xwpfDocumentBytes = downloadService.exportWordDoc().toByteArray();

        response.setContentType("application/msword");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = " + "filledtemplate";
        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(xwpfDocumentBytes);
        outputStream.close();

    }

    @GetMapping("/download-tables/{id}")
    public void downloadTables(@PathVariable Long id, Model model, HttpServletResponse response) throws IOException {

       // DownloadService downloadService = new DownloadService(administratorService, insolvencyProcessService, otherExpensesService );


        byte[] xwpfDocumentBytes = downloadService.exportCostsOfInsolvencyProceedings(id).toByteArray();
        response.setContentType("application/msword");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = " + "SegsanasPlans.doc";
        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(xwpfDocumentBytes);
        outputStream.close();

    }
    //    --------------------------------------------------------------------------
    @GetMapping("/download-company-blank/{id}")
    public void downloadCompanyBlank(@PathVariable Long id, Model model, HttpServletResponse response) throws IOException {

        DownloadService companyBlank = new DownloadService(administratorService, insolvencyProcessService, otherExpensesService, templateService);

        byte[] xwpfDocumentBytes = companyBlank.exportCompanyBlank(id).toByteArray();

        response.setContentType("application/msword");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = " + "companyBlank.doc";
        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(xwpfDocumentBytes);
        outputStream.close();

    }

    @GetMapping("/download-authority-blank/{id}/{number}")
    public void downloadAuthorityBlank(@PathVariable Long id, @PathVariable int number,Model model, HttpServletResponse response) throws IOException {

        DownloadService authorityBlank = new DownloadService(administratorService, insolvencyProcessService, otherExpensesService, templateService);

        byte[] xwpfDocumentBytes = authorityBlank.exportAuthorityBlank(id, number).toByteArray();

        response.setContentType("application/msword");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = " + "authorityBlank.doc";
        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(xwpfDocumentBytes);
        outputStream.close();
    }
}
