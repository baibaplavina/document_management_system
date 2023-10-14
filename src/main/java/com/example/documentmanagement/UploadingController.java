package com.example.documentmanagement;


import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.io.InputStream;



@Controller
public class UploadingController {

    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private InsolvencyProcessService insolvencyProcessService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private UploadService uploadService;

    @GetMapping("/files")
    public String homepage() {
        return "files";
    }

    @PostMapping("/process-documents/{id}/documents/{uploadTypeId}")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes, @PathVariable Long id, @PathVariable Long uploadTypeId) {
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/?message=UPLOADING_FAILED";
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(fileName + " " + id);
        try {
            InputStream stream = file.getInputStream();
            if(uploadTypeId == 1) {
            uploadService.handleAssetsUpload(stream, id);}
            else if(uploadTypeId == 2) {
                uploadService.handleCostsUpload(stream, id);
            }
            else if(uploadTypeId == 3) {
                uploadService.handleMoneyUpload(stream, id);
            }
            else if(uploadTypeId == 4) {
                uploadService.handleExpensesUpload(stream, id);
            }
            else if(uploadTypeId == 5) {
                uploadService.handleCreditorsUpload(stream, id);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

        return "redirect:/process-documents/{id}";
    }

    @GetMapping("/download-blank")
    public void downloadBlank(Model model, HttpServletResponse response) throws IOException {

        TemplateService templateFirst = new TemplateService(administratorService, insolvencyProcessService);

        byte[] xwpfDocumentBytes = templateFirst.exportBlankDoc().toByteArray();

        response.setContentType("application/msword");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = " + "adminBlank.doc";
        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(xwpfDocumentBytes);
        outputStream.close();

    }

    @GetMapping("/download-filled")
    public void downloadFilledFile(Model model, HttpServletResponse response) throws IOException {



        byte[] xwpfDocumentBytes = templateService.exportWordDoc().toByteArray();

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

        TemplateService templateService = new TemplateService(administratorService, insolvencyProcessService);

      //  byte[] xwpfDocumentBytes = templateService.exportTableDoc(id).toByteArray();
        byte[] xwpfDocumentBytes = templateService.exportCostsOfInsolvencyProceedings(id).toByteArray();
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

        TemplateService companyBlank = new TemplateService(administratorService, insolvencyProcessService);

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

        TemplateService authorityBlank = new TemplateService(administratorService, insolvencyProcessService);

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