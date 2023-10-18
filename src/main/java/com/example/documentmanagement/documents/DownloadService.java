package com.example.documentmanagement.documents;

import com.example.documentmanagement.administrator.AdministratorService;
import com.example.documentmanagement.insolvencyprocess.InsolvencyProcessService;
import com.example.documentmanagement.otherExpenses.OtherExpensesService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class DownloadService {

    private AdministratorService administratorService;
    private InsolvencyProcessService insolvencyProcessService;
    private OtherExpensesService otherExpensesService;
    private TemplateService templateService;

    @Autowired
    public DownloadService(AdministratorService administratorService, InsolvencyProcessService insolvencyProcessService, OtherExpensesService otherExpensesService, TemplateService templateService) {
        this.administratorService = administratorService;
        this.insolvencyProcessService = insolvencyProcessService;
        this.otherExpensesService = otherExpensesService;
        this.templateService = templateService;
    }


    public ByteArrayOutputStream exportBlankDoc() throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/adminBlank.docx");
        XWPFDocument doc = new XWPFDocument(inputStream);

        try {
            templateService.replaceHeaderText(doc);
            templateService.replacePlaceDateText(doc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        out.close();
        doc.close();

        return out;
    }

    public ByteArrayOutputStream exportWordDoc() throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/template1.docx");
        XWPFDocument doc = new XWPFDocument(inputStream);

        try {

            templateService.replaceHeaderText(doc);
            templateService.replacePlaceDateText(doc);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        out.close();
        doc.close();

        return out;
    }

    public ByteArrayOutputStream exportCostsOfInsolvencyProceedings(Long processId) {

        try {
            CostsOfInsolvencyProceedingsDoc document = new CostsOfInsolvencyProceedingsDoc(insolvencyProcessService.findInsolvencyProcessById(processId), otherExpensesService);
            XWPFDocument doc  = document.createFilledDocument();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.write(out);
            out.close();
            doc.close();
            return out;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*------------------------------------------------*////

    public ByteArrayOutputStream exportAdminBlank(Long id) throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/adminBlank.docx");
        XWPFDocument adminBlank = new XWPFDocument(inputStream);

        try {

            templateService.replaceHeaderText(adminBlank);
            templateService.replaceText(adminBlank, "Place", administratorService.findAdministratorById(2L).getAdminAddress());
            templateService.replaceText(adminBlank, "Maksātnespējīgā companyName", "Maksātnespējīgā " +  insolvencyProcessService.findInsolvencyProcessById(id).getCompanyName());
            templateService.replaceText(adminBlank, "vienotais reģistrācijas Nr. registrationNumber", "vienotais reģistrācijas Nr. " +  insolvencyProcessService.findInsolvencyProcessById(id).getRegistrationNumber());
            templateService.replaceText(adminBlank, "Place", administratorService.findAdministratorById(2L).getAdminAddress());



        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        adminBlank.write(out);
        out.close();
        adminBlank.close();

        return out;
    }

    public ByteArrayOutputStream exportCompanyBlank(Long id) throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/companyBlank.docx");
        XWPFDocument doc = new XWPFDocument(inputStream);

        try {

            templateService.replaceHeaderText(doc);
            templateService.replaceCompanyParagraphText(doc, id);
            templateService.replaceIeceltaIeceltsParagraphText(doc,id);
            templateService.replacePlaceDateText(doc);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        out.close();
        doc.close();

        return out;
    }


    public ByteArrayOutputStream exportAuthorityBlank(Long id, int number) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/companyBlank.docx");
        XWPFDocument doc = new XWPFDocument(inputStream);

        try {
            templateService.replaceHeaderText(doc);
            templateService.replaceCompanyParagraphText(doc, id);
            templateService.replacePlaceDateText(doc);
            templateService.replaceIeceltaIeceltsParagraphText(doc,id);
            templateService.replaceAuthorityDocNameText(doc);

            switch (number){
                case 1:
                    templateService.replaceAuthorityMainText1(doc);
                    break;
                case 2:
                    templateService.replaceAuthorityMainText2(doc);
                    break;
                case 3:
                    templateService.replaceAuthorityMainText3(doc, id);
                    break;
                case 4:
                    templateService.replaceAuthorityMainText4(doc, id);
                    break;
                case 5:
                    templateService.replaceAuthorityMainText5(doc, id);
                    break;
                case 6:
                    templateService.replaceAuthorityMainText6(doc, id);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        out.close();
        doc.close();

        return out;
    }
}
