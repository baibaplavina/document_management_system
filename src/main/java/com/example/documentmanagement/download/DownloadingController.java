package com.example.documentmanagement.download;

import com.example.documentmanagement.insolvencyProcess.InsolvencyProcessService;
import com.example.documentmanagement.insolvencyProcess.otherExpenses.OtherExpensesService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class DownloadingController {

    @Autowired
    private InsolvencyProcessService insolvencyProcessService;
    @Autowired
    private DownloadService downloadService;
    @Autowired
    private OtherExpensesService otherExpensesService;

    @GetMapping("/download-blank/{id}")
    public void downloadAdminBlank(@PathVariable Long id, HttpServletResponse response) throws IOException {

        DownloadService adminBlank = new DownloadService(insolvencyProcessService, otherExpensesService);

        byte[] xwpfDocumentBytes = adminBlank.exportAdminBlank(id).toByteArray();

        response.setContentType("application/msword");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = " + "adminBlank.doc";
        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(xwpfDocumentBytes);
        outputStream.close();

    }

    @GetMapping("/download-tables/{id}")
    public void downloadTables(@PathVariable Long id, HttpServletResponse response) {

        try {
            byte[] xwpfDocumentBytes = downloadService.exportCostsOfInsolvencyProceedings(id).toByteArray();
            response.setContentType("application/msword");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename = " + "SegsanasPlans.doc";
            response.setHeader(headerKey, headerValue);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(xwpfDocumentBytes);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Error writing file to output stream");
        }

    }

    @GetMapping("/download-company-blank/{id}")
    public void downloadCompanyBlank(@PathVariable Long id, HttpServletResponse response) throws IOException {

        DownloadService companyBlank = new DownloadService(insolvencyProcessService, otherExpensesService);

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
    public void downloadAuthorityBlank(@PathVariable Long id, @PathVariable int number, HttpServletResponse response) throws IOException {

        DownloadService authorityBlank = new DownloadService(insolvencyProcessService, otherExpensesService);

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
