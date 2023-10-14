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

    public CostsOfInsolvencyProceedingsDoc(InsolvencyProcess insolvencyProcess) {

        this.insolvencyProcess = insolvencyProcess;

    }

    public XWPFDocument createFilledDocument() throws IOException {
        TemplateService templateService = new TemplateService();
        InputStream inputStream = getClass().getResourceAsStream("/template_tables.docx");
        assert inputStream != null;
        XWPFDocument doc = new XWPFDocument(inputStream);

        try {
            String creditors = insolvencyProcess.getCreditorsList();

            List<String> listOfCreditors = new ArrayList<>(Arrays.asList(creditors.split(";")));

            int beforeAssetsTableposition = 0;
            for (int i = 0; i < doc.getParagraphs().size(); i++) {
                if (doc.getParagraphs().get(i).getText().contains("maksātnespējas procesa izmaksu sarakstu, kas attiecas uz nodrošināto mantu:")) {
                    beforeAssetsTableposition = i;
                    break;
                }
            }

            XmlCursor cursor1 = doc.getParagraphs().get(beforeAssetsTableposition + 2).getCTP().newCursor();
            XWPFParagraph new_paragraph = doc.insertNewParagraph(cursor1);

            if (!insolvencyProcess.getSecuredAssets()) {
                new_paragraph.createRun().setText("Sabiedrībai nav mantas, kas kalpotu par nodrošinājumu maksātnespējas procesā.");
                XmlCursor cursor2 = doc.getParagraphs().get(beforeAssetsTableposition + 3).getCTP().newCursor();
                XWPFParagraph new_paragraph1 = doc.insertNewParagraph(cursor2);
                new_paragraph1.createRun().setText("Sabiedrības maksātnespējas procesā nav nodrošināto kreditoru prasījumu.");
            } else {
                XWPFRun run = new_paragraph.createRun();
                run.setBold(true);
                run.setText("[1.1.] Ienākumi no nodrošinātās mantas maksātnespējas procesā");
                XmlCursor cursor2 = doc.getParagraphs().get(beforeAssetsTableposition + 4).getCTP().newCursor();
                XWPFParagraph new_paragraph1 = doc.insertNewParagraph(cursor2);
                create1_1table(doc, new_paragraph1);

            }

            int creditorTextPosition = 0;
            for (int i = 0; i < doc.getParagraphs().size(); i++) {
                if (doc.getParagraphs().get(i).getText().equals("Kreditori:")) {
                    creditorTextPosition = i;
                    break;
                }
            }

            for (int j = listOfCreditors.size() - 1; j >= 0; j--) {
                XmlCursor cursor = doc.getParagraphs().get(creditorTextPosition + 1).getCTP().newCursor();
                XWPFParagraph new_par = doc.insertNewParagraph(cursor);
                new_par.setAlignment(ParagraphAlignment.RIGHT);
                new_par.createRun().setText(listOfCreditors.get(j));
            }

          //  templateService.replaceHeaderText(doc);
            templateService.replaceText(doc, "InsolvencyCompanyName",
                    insolvencyProcess.getCompanyName());
            templateService.replaceText(doc, "CompanyName",
                    insolvencyProcess.getCompanyName());
            templateService.replaceText(doc, "vienotais reģistrācijas Nr. ", " vienotais reģistrācijas Nr. " +
                    insolvencyProcess.getRegistrationNumber());

            create3table(doc);


         //   templateService.replaceText(doc, "/This document table 3 last No/",
                //    String.format("%1.2f", lineNumber7Value).replace('.', ','));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return doc;
    }


    private void create1_1table(XWPFDocument doc, XWPFParagraph p) {

        XWPFTable createdTable = p.getBody().insertNewTbl(p.getCTP().newCursor());

        int headerSize = 2;

        for (int i = 1; i < headerSize; i++) {
            createdTable.getRow(0).addNewTableCell();
        }

        styleCellBold(createdTable.getRow(0).getCell(0), "Summa, EUR");
        styleCellBold(createdTable.getRow(0).getCell(1), "Pamatojums");

        String assetList = insolvencyProcess.getAssetsList();
        String sums = insolvencyProcess.getAssetsListCosts();

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

        int indexOf3rdTable = doc.getTables().size() - 1;

        doc.getTables().get(indexOf3rdTable).getRow(1).getCell(2).
                setText(String.valueOf(insolvencyProcess.getNeikilataMantaSum()).replace('.', ','));
        doc.getTables().get(indexOf3rdTable).getRow(2).getCell(2).
                setText(String.valueOf(insolvencyProcess.getProcessMoney()));
        doc.getTables().get(indexOf3rdTable).getRow(3).getCell(2).
                setText(String.valueOf(insolvencyProcess.getTotalExpenses()).replace('.', ','));
        doc.getTables().get(indexOf3rdTable).getRow(4).getCell(2).
                setText(String.format("%1.2f", insolvencyProcess.getAdminSalary()));


        Double lineNumber5Value =
                insolvencyProcess.getNeikilataMantaSum() +
                        Double.parseDouble(insolvencyProcess.getProcessMoney().replace(',', '.'))
                        - insolvencyProcess.getTotalExpenses() -
                        insolvencyProcess.getAdminSalary();

        Double lineNumber6Value = lineNumber5Value * 0.1;
        Double lineNumber7Value = lineNumber5Value - lineNumber6Value;

        doc.getTables().get(indexOf3rdTable).getRow(5).getCell(2).
                setText(String.format("%1.2f", lineNumber5Value).replace('.', ','));
        doc.getTables().get(indexOf3rdTable).getRow(6).getCell(2).
                setText(String.format("%1.2f", lineNumber6Value).replace('.', ','));
        doc.getTables().get(indexOf3rdTable).getRow(7).getCell(2).
                setText(String.format("%1.2f", lineNumber7Value).replace('.', ','));
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
