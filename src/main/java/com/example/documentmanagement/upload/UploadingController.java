package com.example.documentmanagement.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.InputStream;


@Controller
public class UploadingController {

    @Autowired
    private UploadService uploadService;


    @PostMapping("/process-documents/{id}/documents/{uploadTypeId}")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes, @PathVariable Long id, @PathVariable int uploadTypeId) {
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/?message=UPLOADING_FAILED";
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(fileName + " " + id);
        try {
            InputStream stream = file.getInputStream();
            switch (uploadTypeId) {
                case 1 -> uploadService.handleAssetsUpload(stream, id);
                case 2 -> uploadService.handleCostsUpload(stream, id);
                case 3 -> uploadService.handleMoneyUpload(stream, id);
                case 4 -> uploadService.handleExpensesUpload(stream, id);
                case 5 -> uploadService.handleCreditorsUpload(stream, id);
                default -> {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

        return "redirect:/process-documents/{id}#listHeader";
    }

}