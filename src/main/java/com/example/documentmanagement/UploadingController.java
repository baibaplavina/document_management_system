package com.example.documentmanagement;


import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.io.InputStream;


@Controller
public class UploadingController {


    @GetMapping("/files")
    public String homepage() {
        return "files";
    }

    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/";
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            InputStream stream = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(stream);

            Sheet datatypeSheet = workbook.getSheetAt(0);
            System.out.println(fileName);
            System.out.println(datatypeSheet.getSheetName());
            System.out.println(datatypeSheet.getRow(0).getCell(1));


        } catch (Exception e) {
            e.printStackTrace();
        }

        attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

        return "redirect:/";
    }

    @GetMapping("/download")
    public void downloadFile(Model model, HttpServletResponse response) throws IOException {

        Template_first templateFirst = new Template_first();

        byte[] xwpfDocumentBytes = templateFirst.exportWordDoc().toByteArray();

        response.setContentType("application/msword");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = " + "filledtemplate";
        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(xwpfDocumentBytes);
        outputStream.close();

    }
}