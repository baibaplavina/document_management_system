package com.example.documentmanagement;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class TemplateService {

    private AdministratorService administratorService;

    @Autowired
    public TemplateService(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    public ByteArrayOutputStream exportBlankDoc() throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/template1.docx");
        XWPFDocument doc = new XWPFDocument(inputStream);

        try {
            replaceHeaderText(doc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        out.close();
        doc.close();

        return out;
    }

    private void replaceHeaderText(XWPFDocument doc) throws Exception {
        replaceText(doc, "Maksātnespējas procesa administrators /administratorName AdministratorSurname/ (amata apliecības Nr. /sertificateNumber/)",
                "Maksātnespējas procesa administrators" + " " +
                        administratorService.findAdministratorById(1L).getAdminName() + " " +
                        administratorService.findAdministratorById(1L).getAdminSurname() +
                        " (amata apliecības Nr. " + administratorService.findAdministratorById(1L).getCertificateNumber() + ")");


        replaceText(doc, "Adrese: /administratorAddress/, telefons: /administratorPhoneNumber/,  e-pasts: /adminisratorEmail/, e-Adrese:/administratorEAddress/",
                "Adrese: " + " " +
                        administratorService.findAdministratorById(1L).getAdminAddress() + ", telefons: " +
                        administratorService.findAdministratorById(1L).getAdminPhoneNumber() +
                        ", e-pasts: " + administratorService.findAdministratorById(1L).getAdminEmail() +
                        ", e-Adrese: " + administratorService.findAdministratorById(1L).getAdminE_address());
    }

    public ByteArrayOutputStream exportWordDoc() throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/template1.docx");
        XWPFDocument doc = new XWPFDocument(inputStream);

        try {

            replaceHeaderText(doc);

            replaceText(doc, "iecelta/iecelts", "Inserted_text");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        out.close();
        doc.close();

        return out;
    }


    private XWPFDocument replaceText(XWPFDocument doc, String originalText, String updatedText) {
        replaceTextInParagraphs(doc.getParagraphs(), originalText, updatedText);

        return doc;
    }

    private void replaceTextInParagraphs(List<XWPFParagraph> paragraphs, String originalText, String updatedText) {
        paragraphs.forEach(paragraph -> replaceTextInParagraph(paragraph, originalText, updatedText));
    }

    private void replaceTextInParagraph(XWPFParagraph paragraph, String originalText, String updatedText) {
        List<XWPFRun> runs = paragraph.getRuns();
        for (XWPFRun run : runs) {
            String text = run.getText(0);
            if (text != null && text.contains(originalText)) {
                String updatedRunText = text.replace(originalText, updatedText);
                run.setText(updatedRunText, 0);
            }
        }
    }

    public ByteArrayOutputStream exportTableDoc() throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/template1.docx");
        XWPFDocument doc = new XWPFDocument(inputStream);

        try {

            replaceHeaderText(doc);
            create1_1table(doc);
            create1_2table(doc);
            create3table(doc);

            replaceText(doc, "iecelta/iecelts", "Inserted_text");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        out.close();
        doc.close();

        return out;
    }


    private void create1_1table(XWPFDocument doc) {

        XWPFRun run = doc.createParagraph().createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);
        run.setText(" 1.1. Ienākumi no nodrošinātās mantas maksātnespējas procesā ");

        XWPFTable createdTable = doc.createTable();

        int headerSize = 2;

        for (int i = 1; i < (headerSize); i++) {
            createdTable.getRow(0).addNewTableCell();
        }

        styleCell(createdTable.getRow(0).getCell(0), "Summa, EUR");
        styleCell(createdTable.getRow(0).getCell(1), "Pamatojums");

        createdTable.createRow();
        createdTable.createRow();
        doc.createParagraph();


    }

    private void create1_2table(XWPFDocument doc) {

        XWPFRun run = doc.createParagraph().createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);
        run.setText(" 1.2.  Izmaksas, kas saistītas ar nodrošināto mantu  ");

        XWPFTable createdTable = doc.createTable();

        int headerSize = 7;

        for (int i = 1; i < (headerSize); i++) {
            createdTable.getRow(0).addNewTableCell();
        }
        styleCell(createdTable.getRow(0).getCell(0), "Maksājuma tips");
        styleCell(createdTable.getRow(0).getCell(1), "Pakalpojoums");
        styleCell(createdTable.getRow(0).getCell(2), "Saņēmējs");
        styleCell(createdTable.getRow(0).getCell(3), "Izmaksu rašanās datums");
        styleCell(createdTable.getRow(0).getCell(4), "Segtā summa, EUR");
        styleCell(createdTable.getRow(0).getCell(5), "Maksājuma datums");
        styleCell(createdTable.getRow(0).getCell(6), "Radušās un nav apmaksātas izmaksas EUR");

        createdTable.createRow();
        createdTable.createRow();
        createdTable.createRow();

        doc.createParagraph();
    }

    private void create3table(XWPFDocument doc) {

        XWPFRun run = doc.createParagraph().createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);
        run.setText(" 3. ... maksātnespējas procesa neieķīlātās mantas ienākumu-izmaksu kopsavilkums ");

        XWPFTable createdTable = doc.createTable();

        int headerSize = 3;

        for (int i = 1; i < (headerSize); i++) {
            createdTable.getRow(0).addNewTableCell();
        }

        styleCell(createdTable.getRow(0).getCell(1), "Pamatojums");
        styleCell(createdTable.getRow(0).getCell(2), "Summa, EUR");

        for (int i = 0; i < 8; i++) {
            createdTable.createRow();
        }

        styleCell(createdTable.getRow(1).getCell(0), "1.");
        styleCell(createdTable.getRow(1).getCell(1), "Maksātnespējas procesā iegūtie naudas līdzekļi (neķīlātā manta)");

        styleCell(createdTable.getRow(2).getCell(0), "2.");
        styleCell(createdTable.getRow(2).getCell(1), "Norēķinu kontā esošie līdzekļi uz maksātnespējas procesa pasludināšanas brīdi");

        styleCell(createdTable.getRow(3).getCell(0), "3.");
        styleCell(createdTable.getRow(3).getCell(1), "Maksātnespējas procesa izdevumi");

        styleCell(createdTable.getRow(4).getCell(0), "4.");
        styleCell(createdTable.getRow(4).getCell(1), "Administratora atlīdzība saskaņā ar Maksātnespējas likuma 169. panta otrās daļas 1. punktu  ");

        styleCell(createdTable.getRow(5).getCell(0), "5.");
        styleCell(createdTable.getRow(5).getCell(1), "Kreditoriem izmaksai paredzētā summa");

        styleCell(createdTable.getRow(6).getCell(0), "6.");
        styleCell(createdTable.getRow(6).getCell(1), "Administratora atlīdzība saskaņā ar Maksātnespējas likuma 169. panta otrās daļas 2. punktu ");

        styleCell(createdTable.getRow(7).getCell(0), "7.");
        styleCell(createdTable.getRow(7).getCell(1), "Pievienotās vērtības nodoklis administratora atlīdzībai (Maksātnespējas likuma 169. panta septītā daļa  )");

        styleCell(createdTable.getRow(8).getCell(0), "8.");
        styleCell(createdTable.getRow(8).getCell(1), "Faktiski kreditoriem izmaksājamā summa");
    }


    public void styleCell(XWPFTableCell cell, String s) {
        XWPFRun run = cell.addParagraph().createRun();

        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setText(s);
        cell.removeParagraph(0);
    }
}
