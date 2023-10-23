package com.example.documentmanagement.download;

import com.example.documentmanagement.documents.*;
import com.example.documentmanagement.insolvencyProcess.InsolvencyProcessService;
import com.example.documentmanagement.insolvencyProcess.otherExpenses.OtherExpensesService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


@Service
public class DownloadService {
    private XWPFDocument doc;
    private final InsolvencyProcessService insolvencyProcessService;
    private final OtherExpensesService otherExpensesService;


    @Autowired
    public DownloadService(InsolvencyProcessService insolvencyProcessService, OtherExpensesService otherExpensesService) {
        this.insolvencyProcessService = insolvencyProcessService;
        this.otherExpensesService = otherExpensesService;

    }

    public ByteArrayOutputStream getStreamOfDocument(XWPFDocument doc) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            doc.write(out);
            out.close();
            doc.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return out;
    }


    public ByteArrayOutputStream exportCostsOfInsolvencyProceedings(Long processId) {

        try {
            CostsOfInsolvencyProceedingsDoc document = new CostsOfInsolvencyProceedingsDoc(insolvencyProcessService.findInsolvencyProcessById(processId), otherExpensesService);
            return getStreamOfDocument(document.createFilledDocument());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ByteArrayOutputStream exportAdminBlank(Long id) {

        try {
            AdminBlank adminBlank = new AdminBlank(insolvencyProcessService.findInsolvencyProcessById(id));
            return getStreamOfDocument(adminBlank.createFilledDocument());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ByteArrayOutputStream exportCompanyBlank(Long id) {

        try {
            CompanyBlank companyBlank = new CompanyBlank(insolvencyProcessService.findInsolvencyProcessById(id));
            return getStreamOfDocument(companyBlank.createFilledDocument());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ByteArrayOutputStream exportAuthorityBlank(Long id, int number) {

        try {
            AuthorityBlankFactory authorityBlankFactory = new AuthorityBlankFactory();
            AuthorityBlank authorityBlank = authorityBlankFactory.getAuthorityBlank(insolvencyProcessService.findInsolvencyProcessById(id), number);
            return getStreamOfDocument(authorityBlank.createFilledDocument());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
