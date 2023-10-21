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


   /*public ByteArrayOutputStream exportBlankDoc(Long id) throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/adminBlank.docx");
        XWPFDocument doc = new XWPFDocument(inputStream);

        try {
            templateService.replaceCompanyAdminHeaderText(doc,id);
            templateService.replacePlaceDateCompanyBlankText(doc,id);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        out.close();
        doc.close();

        return out;
    }*/

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
            XWPFDocument doc = document.createFilledDocument();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.write(out);
            out.close();
            doc.close();
            return out;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ByteArrayOutputStream exportAdminBlank(Long id) throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/adminBlank.docx");
        XWPFDocument doc = new XWPFDocument(inputStream);

        try {

            templateService.replaceCompanyAdminHeaderText(doc, id);
            templateService.replaceCompanyParagraphText(doc, id);
            //  templateService.replaceIeceltaIeceltsParagraphText(doc,id);
            templateService.replacePlaceDateCompanyBlankText(doc, id);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        out.close();
        doc.close();

        return out;
    }

    public ByteArrayOutputStream exportCompanyBlank(Long id) throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/companyBlank.docx");
        XWPFDocument doc = new XWPFDocument(inputStream);

        try {

            templateService.replaceCompanyAdminHeaderText(doc, id);
            templateService.replaceCompanyParagraphText(doc, id);
            templateService.replaceIeceltaIeceltsParagraphText(doc, id);
            templateService.replacePlaceDateCompanyBlankText(doc, id);

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

        XWPFDocument doc = new XWPFDocument();
        try {
            switch (number) {
                case 1:
                    AuthorityBlank_1 authorityBlank1 = new AuthorityBlank_1(insolvencyProcessService.findInsolvencyProcessById(id));
                    doc = authorityBlank1.createFilledDocument();
                    break;
                case 2:

                    AuthorityBlank_2 authorityBlank2 = new AuthorityBlank_2(insolvencyProcessService.findInsolvencyProcessById(id));
                    doc = authorityBlank2.createFilledDocument();
                    break;
                case 3:

                    AuthorityBlank_3 authorityBlank3 = new AuthorityBlank_3(insolvencyProcessService.findInsolvencyProcessById(id));
                    doc = authorityBlank3.createFilledDocument();
                    break;
                case 4:

                    AuthorityBlank_4 authorityBlank4 = new AuthorityBlank_4(insolvencyProcessService.findInsolvencyProcessById(id));
                    doc = authorityBlank4.createFilledDocument();
                    break;
                case 5:
                    AuthorityBlank_5 authorityBlank5 = new AuthorityBlank_5(insolvencyProcessService.findInsolvencyProcessById(id));
                    doc = authorityBlank5.createFilledDocument();

                    break;
                case 6:
                    AuthorityBlank_6 authorityBlank6 = new AuthorityBlank_6(insolvencyProcessService.findInsolvencyProcessById(id));
                    doc = authorityBlank6.createFilledDocument();

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
