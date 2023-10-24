package com.example.documentmanagement.documents;

import com.example.documentmanagement.administrator.Gender;
import com.example.documentmanagement.insolvencyProcess.InsolvencyProcess;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

public class CompanyBlank extends TemplateService {
    private InsolvencyProcess insolvencyProcess;

    public CompanyBlank(InsolvencyProcess insolvencyProcess) {

        this.insolvencyProcess = insolvencyProcess;
    }

    public XWPFDocument createFilledDocument() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/companyBlank.docx");
        assert inputStream != null;
        XWPFDocument doc = new XWPFDocument(inputStream);

        try {

            replaceCompanyAdminHeaderText(doc, insolvencyProcess.getId());
            replaceCompanyParagraphText(doc, insolvencyProcess.getId());
            replaceIeceltaIeceltsParagraphText(doc, insolvencyProcess.getId());
            replacePlaceDateCompanyBlankText(doc, insolvencyProcess.getId());

            return doc;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void replaceCompanyAdminHeaderText(XWPFDocument doc, Long id) {

        replaceText(doc, "administratorName",
                insolvencyProcess.getAdmin().getAdminName());
        replaceText(doc, "administratorSurname",
                insolvencyProcess.getAdmin().getAdminSurname());
        replaceText(doc, "certificateNumber",
                insolvencyProcess.getAdmin().getCertificateNumber());
        replaceText(doc, "administratorAddress",
                insolvencyProcess.getAdmin().getAdminAddress());
        replaceText(doc, "administratorPhoneNumber",
                insolvencyProcess.getAdmin().getAdminPhoneNumber());
        replaceText(doc, "adminisratorEmail",
                insolvencyProcess.getAdmin().getAdminEmail());
        replaceText(doc, "administratorEAddress",
                " " + insolvencyProcess.getAdmin().getAdminE_address());
        replaceText(doc, "companyName",
                insolvencyProcess.getCompanyName());
        replaceText(doc, "registrationNumber",
                insolvencyProcess.getRegistrationNumber());
    }

    public void replaceCompanyParagraphText(XWPFDocument doc, Long id) {

       /* replaceText(doc,"companyName",
        ""+insolvencyProcessService.findInsolvencyProcessById(id).getCompanyName());
        replaceText(doc,"registrationNumber",
        ""+insolvencyProcessService.findInsolvencyProcessById(id).getRegistrationNumber());*/
        replaceText(doc, "courtName",
                "" + insolvencyProcess.getCourtName());
        replaceText(doc, "courtDesitionDate",
                "" + insolvencyProcess.getCourtDecisionDate());
        replaceText(doc, "courtCaseNumber",
                "" + insolvencyProcess.getCourtCaseNumber() + " ");
    }

    public void replaceIeceltaIeceltsParagraphText(XWPFDocument doc, Long id) {

        boolean isFemale = insolvencyProcess.getAdmin().getAdminGender().equals(Gender.FEMALE);
        if (isFemale) {
            replaceText(doc, "iecelta/iecelts",
                    "iecelta");
        } else if (insolvencyProcess.getAdmin().getAdminGender().equals(Gender.MALE)) {
            replaceText(doc, "iecelta/iecelts",
                    "iecelts");
        }
    }

    public void replacePlaceDateCompanyBlankText(XWPFDocument doc, Long id) {

        replaceText(doc, "Place",
                "" + insolvencyProcess.getAdmin().getPlace());
        LocalDate documentDate = LocalDate.now();
        replaceText(doc, "Date", String.valueOf(documentDate));
    }
}
