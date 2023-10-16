package com.example.documentmanagement;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CostsOfInsolvencyProceedingsDoc {

    private final InsolvencyProcess insolvencyProcess;
    private XWPFDocument doc;

    public CostsOfInsolvencyProceedingsDoc(InsolvencyProcess insolvencyProcess) {

        this.insolvencyProcess = insolvencyProcess;

    }

    public XWPFDocument createFilledDocument() throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/template_tables.docx");
        assert inputStream != null;
        doc = new XWPFDocument(inputStream);

        try {
            createHeadersPart();
            createCreditorsPart();
            createSecuredAssetsPart();
            createUnsecuredAssetsPart();
            createPart3And4();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return doc;
    }

    private void createHeadersPart() {
        TemplateService templateService = new TemplateService();
        templateService.replaceText(doc, "/administratorName AdministratorSurname/",
                insolvencyProcess.getAdmin().getAdminName() + " " +
                        insolvencyProcess.getAdmin().getAdminSurname());
        templateService.replaceText(doc, "/sertificateNumber/",
                insolvencyProcess.getAdmin().getCertificateNumber());
        templateService.replaceText(doc, "administratorAddress/",
                insolvencyProcess.getAdmin().getAdminAddress());
        templateService.replaceText(doc, "/administratorPhoneNumber/",
                insolvencyProcess.getAdmin().getAdminPhoneNumber());
        templateService.replaceText(doc, "/adminisratorEmail/",
                insolvencyProcess.getAdmin().getAdminEmail());
        templateService.replaceText(doc, "/administratorEAddress/",
                   " " + insolvencyProcess.getAdmin().getAdminE_address());
        templateService.replaceText(doc, "InsolvencyCompanyName",
                insolvencyProcess.getCompanyName());
        templateService.replaceText(doc, "CompanyName",
                insolvencyProcess.getCompanyName());
        templateService.replaceText(doc, "vienotais reģistrācijas Nr. ", " vienotais reģistrācijas Nr. " +
                insolvencyProcess.getRegistrationNumber());
    }

    private void createCreditorsPart() {

        String creditors = insolvencyProcess.getCreditorsList();
        List<String> listOfCreditors = new ArrayList<>(Arrays.asList(creditors.split(";")));
        int creditorTextPosition = getParagraphPositionIfContainsText( "Kreditori:");
        if (creditorTextPosition > -1) {
            for (int j = listOfCreditors.size() - 1; j >= 0; j--) {
                XmlCursor cursor = doc.getParagraphs().get(creditorTextPosition + 1).getCTP().newCursor();
                XWPFParagraph new_par = doc.insertNewParagraph(cursor);
                new_par.setAlignment(ParagraphAlignment.RIGHT);
                new_par.createRun().setText(listOfCreditors.get(j));
            }
        }
    }

    private void createSecuredAssetsPart() {
        int beforeAssetsTablePosition = getParagraphPositionIfContainsText("maksātnespējas procesa izmaksu sarakstu, kas attiecas uz nodrošināto mantu:");

        XmlCursor cursor1 = doc.getParagraphs().get(beforeAssetsTablePosition + 2).getCTP().newCursor();
        XWPFParagraph new_paragraph = doc.insertNewParagraph(cursor1);
        XWPFRun run = new_paragraph.createRun();

        if (!insolvencyProcess.getSecuredAssets()) {
            run.setText("Sabiedrībai nav mantas, kas kalpotu par nodrošinājumu maksātnespējas procesā.");
            XmlCursor cursor2 = doc.getParagraphs().get(beforeAssetsTablePosition + 3).getCTP().newCursor();
            XWPFParagraph new_paragraph1 = doc.insertNewParagraph(cursor2);
            new_paragraph1.createRun().setText("Sabiedrības maksātnespējas procesā nav nodrošināto kreditoru prasījumu.");
        } else {
            run.setBold(true);
            run.setText("1.1. Ienākumi no nodrošinātās mantas maksātnespējas procesā");

            XmlCursor cursor2 = doc.getParagraphs().get(beforeAssetsTablePosition + 4).getCTP().newCursor();
            XWPFParagraph new_paragraph1 = doc.insertNewParagraph(cursor2);
            create_1_1_or_2_1table(new_paragraph1, insolvencyProcess.getAssetsList(), insolvencyProcess.getAssetsListCosts());

            XmlCursor cursor3 = new_paragraph1.getCTP().newCursor();
            XWPFParagraph new_paragraph3 = doc.insertNewParagraph(cursor3);
            XWPFRun run3 = new_paragraph3.createRun();
            run3.setBold(true);
            run3.setText("1.2. Izmaksas, kas saistītas ar nodrošināto mantu ");

            XmlCursor cursor4 = new_paragraph3.getCTP().newCursor();
            cursor4.toNextSibling();
            cursor4.toNextSibling();

            XWPFParagraph new_paragraph5 = doc.insertNewParagraph(cursor4);

            create1_2table(new_paragraph5);
        }
    }

    private void createUnsecuredAssetsPart() {

        int table2_1_HeaderPosition = getParagraphPositionIfContainsText("Ienākumi no nenodrošinātās mantas maksātnespējas procesā ");

        XmlCursor cursor2 = doc.getParagraphs().get(table2_1_HeaderPosition + 1).getCTP().newCursor();
        XWPFParagraph new_paragraph1 = doc.insertNewParagraph(cursor2);
        create_1_1_or_2_1table(new_paragraph1, insolvencyProcess.getAssetsList(), insolvencyProcess.getAssetsListCosts());

        int table2_2_HeaderPosition = getParagraphPositionIfContainsText("Izmaksas, kas saistītas ar nenodrošināto kustamo mantu");

        XmlCursor cursor3 = doc.getParagraphs().get(table2_2_HeaderPosition + 1).getCTP().newCursor();
        XWPFParagraph new_paragraph5 = doc.insertNewParagraph(cursor3);
        create2_2table(new_paragraph5);

    }

    private int getParagraphPositionIfContainsText(String s) {
        int paragraphPosition = -1;
        for (int i = 0; i < doc.getParagraphs().size(); i++) {
            if (doc.getParagraphs().get(i).getText().contains(s)) {
                paragraphPosition = i;
                break;
            }
        }
        return paragraphPosition;
    }

    private void create_1_1_or_2_1table(XWPFParagraph p, String assetList, String sums) {

        XWPFTable createdTable = p.getBody().insertNewTbl(p.getCTP().newCursor());

        int headerSize = 2;

        for (int i = 1; i < headerSize; i++) {
            createdTable.getRow(0).addNewTableCell();
        }

        styleCellBold(createdTable.getRow(0).getCell(0), "Summa, EUR");
        styleCellBold(createdTable.getRow(0).getCell(1), "Pamatojums");

        List<String> listOfAssets = new ArrayList<>(Arrays.asList(assetList.split(";")));
        List<String> listOfSums = new ArrayList<>(Arrays.asList(sums.split(";")));


        for (int i = 0; i < listOfAssets.size(); i++) {
            createdTable.createRow();
        }

        for (int i = 1; i <= listOfAssets.size(); i++) {
            styleCell(createdTable.getRow(i).getCell(0), listOfSums.get(i - 1));
            styleCell(createdTable.getRow(i).getCell(1), listOfAssets.get(i - 1));
        }

        XWPFTableRow row = createdTable.createRow();
        styleCellBold(row.getCell(0), insolvencyProcess.getAssetsTotalCosts());
        styleCellBold(row.getCell(1), "Kopā /Total");

        doc.createParagraph();

    }

    private void create1_2table(XWPFParagraph p) {
        XWPFTable createdTable = p.getBody().insertNewTbl(p.getCTP().newCursor());

        int headerSize = 7;

        for (int i = 1; i < headerSize; i++) {
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

    private void create2_2table(XWPFParagraph p) {
        XWPFTable createdTable = p.getBody().insertNewTbl(p.getCTP().newCursor());

        int headerSize = 7;

        for (int i = 1; i < headerSize; i++) {
            createdTable.getRow(0).addNewTableCell();
        }
        styleCellBold(createdTable.getRow(0).getCell(0), "Maksājuma tips");
        styleCellBold(createdTable.getRow(0).getCell(1), "Pakalpojoums");
        styleCellBold(createdTable.getRow(0).getCell(2), "Saņēmējs");
        styleCellBold(createdTable.getRow(0).getCell(3), "Izmaksu rašanās datums");
        styleCellBold(createdTable.getRow(0).getCell(4), "Segtā summa, EUR");
        styleCellBold(createdTable.getRow(0).getCell(5), "Maksājuma datums");
        styleCellBold(createdTable.getRow(0).getCell(6), "Radušās un nav apmaksātas izmaksas EUR");

        createdTable.createRow();
        createdTable.createRow();
        createdTable.createRow();

        doc.createParagraph();
    }

    private void createPart3And4() {

        int indexOf3rdTable = doc.getTables().size() - 1;

        doc.getTables().get(indexOf3rdTable).getRow(1).getCell(2).
                setText(String.valueOf(insolvencyProcess.getNeikilataMantaSum()).replace('.', ','));
        doc.getTables().get(indexOf3rdTable).getRow(2).getCell(2).
                setText(String.valueOf(insolvencyProcess.getProcessMoney()));
        doc.getTables().get(indexOf3rdTable).getRow(3).getCell(2).
                setText(String.valueOf(insolvencyProcess.getTotalExpenses()).replace('.', ','));
        doc.getTables().get(indexOf3rdTable).getRow(4).getCell(2).
                setText(String.format("%1.2f", insolvencyProcess.getAdminSalary()));

        double lineNumber5Value =
                insolvencyProcess.getNeikilataMantaSum() +
                        Double.parseDouble(insolvencyProcess.getProcessMoney().replace(',', '.'))
                        - insolvencyProcess.getTotalExpenses() -
                        insolvencyProcess.getAdminSalary();

        double lineNumber6Value = lineNumber5Value * 0.1;
        double lineNumber7Value = lineNumber5Value - lineNumber6Value;

        doc.getTables().get(indexOf3rdTable).getRow(5).getCell(2).
                setText(String.format("%1.2f", lineNumber5Value).replace('.', ','));
        doc.getTables().get(indexOf3rdTable).getRow(6).getCell(2).
                setText(String.format("%1.2f", lineNumber6Value).replace('.', ','));
        doc.getTables().get(indexOf3rdTable).getRow(7).getCell(2).
                setText(String.format("%1.2f", lineNumber7Value).replace('.', ','));
        createPart4(lineNumber7Value);
    }

    private void createPart4(double lineNumber7Value) {
        TemplateService templateService = new TemplateService();
        templateService.replaceText(doc, "/This document table 3 last No/",
                String.format("%1.2f", lineNumber7Value).replace('.', ','));
    }

    public void styleCell(XWPFTableCell cell, String s) {
        XWPFRun run = cell.addParagraph().createRun();

        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setText(s);
        cell.removeParagraph(0);
    }

    public void styleCellBold(XWPFTableCell cell, String s) {
        XWPFRun run = cell.addParagraph().createRun();

        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setText(s);
        run.setBold(true);
        cell.removeParagraph(0);
    }
}
