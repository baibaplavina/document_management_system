package com.example.documentmanagement;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TemplateService {

    private AdministratorService administratorService;
    private InsolvencyProcessService insolvencyProcessService;

    @Autowired
    public TemplateService(AdministratorService administratorService, InsolvencyProcessService insolvencyProcessService) {
        this.administratorService = administratorService;
        this.insolvencyProcessService = insolvencyProcessService;
    }

    public ByteArrayOutputStream exportBlankDoc() throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/adminBlank.docx");
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

    public ByteArrayOutputStream exportTableDoc(Long processId) throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/template_tables.docx");
        XWPFDocument doc = new XWPFDocument(inputStream);

        try {

            replaceHeaderText(doc);

            doc.getTables().get(2).getRow(1).getCell(2).
                    setText(String.valueOf(insolvencyProcessService.findInsolvencyProcessById(processId).getNeikilataMantaSum()).replace('.', ','));
            doc.getTables().get(2).getRow(2).getCell(2).
                    setText(String.valueOf(insolvencyProcessService.findInsolvencyProcessById(processId).getProcessMoney()));
            doc.getTables().get(2).getRow(3).getCell(2).
                    setText(String.valueOf(insolvencyProcessService.findInsolvencyProcessById(processId).getTotalExpenses()).replace('.', ','));
            doc.getTables().get(2).getRow(4).getCell(2).
                    setText(String.format("%1.2f", insolvencyProcessService.findInsolvencyProcessById(processId).getAdminSalary()));


            Double lineNumber5Value =
                    insolvencyProcessService.findInsolvencyProcessById(processId).getNeikilataMantaSum() +
                            Double.valueOf(insolvencyProcessService.findInsolvencyProcessById(processId).getProcessMoney().replace(',', '.'))
                    -insolvencyProcessService.findInsolvencyProcessById(processId).getTotalExpenses()-
                            insolvencyProcessService.findInsolvencyProcessById(processId).getAdminSalary();

            Double lineNumber6Value = lineNumber5Value * 0.1;
            Double lineNumber7Value = lineNumber5Value-lineNumber6Value;

            doc.getTables().get(2).getRow(5).getCell(2).
                    setText(String.format("%1.2f", lineNumber5Value).replace('.', ','));
            doc.getTables().get(2).getRow(6).getCell(2).
                    setText(String.format("%1.2f", lineNumber6Value).replace('.', ','));
            doc.getTables().get(2).getRow(7).getCell(2).
                    setText(String.format("%1.2f", lineNumber7Value).replace('.', ','));

             create1_1table(doc, processId);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        out.close();
        doc.close();

        return out;
    }


    private void create1_1table(XWPFDocument doc, Long processId) throws Exception {

        List<XWPFParagraph> paragraphs = doc.getParagraphs();
     //   XWPFRun run = doc.getParagraphs().get(25).insertNewRun(0);

      //  run.setFontFamily("Times New Roman");
      //  run.setFontSize(12);
      //  run.setBold(true);
      //  run.setText(" 1.1. Ienākumi no nodrošinātās mantas maksātnespējas procesā ");

        XWPFParagraph p = doc.insertNewParagraph(paragraphs.get(43).getCTP().newCursor());
        XWPFTable createdTable = p.getBody().insertNewTbl(paragraphs.get(43).getCTP().newCursor());


        int headerSize = 2;

        for (int i = 1; i < (headerSize); i++) {
            createdTable.getRow(0).addNewTableCell();
        }

        styleCell(createdTable.getRow(0).getCell(0), "Summa, EUR");
        styleCell(createdTable.getRow(0).getCell(1), "Pamatojums");

        InsolvencyProcess insolvencyProcess = insolvencyProcessService.findInsolvencyProcessById(processId);
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
        styleCell(row.getCell(0), insolvencyProcess.getAssetsTotalCosts());
        styleCell(row.getCell(1), "Kopā /Total");

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


    public void styleCell(XWPFTableCell cell, String s) {
        XWPFRun run = cell.addParagraph().createRun();

        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setText(s);
        cell.removeParagraph(0);
    }

    public void handleAssetsUpload(InputStream stream, Long processId) throws Exception {
        Workbook workbook = WorkbookFactory.create(stream);

        Sheet dataSheet = workbook.getSheetAt(0);
        int colNumMpaRiciba = -1;
        for (int i = 0; i <= dataSheet.getRow(0).getLastCellNum(); i++) {
            Cell cell1 = dataSheet.getRow(0).getCell(i);
            String cellValue1 = cell1.getStringCellValue();
            if ("MPA rīcība".equals(cellValue1)) {
                colNumMpaRiciba = i;
                break;
            }
        }

        int colNumApmers = -1;
        for (int i = 0; i <= dataSheet.getRow(0).getLastCellNum(); i++) {
            Cell cell1 = dataSheet.getRow(0).getCell(i);
            String cellValue1 = cell1.getStringCellValue();
            if ("Iegūto naudas līdzekļu apmērs".equals(cellValue1)) {
                colNumApmers = i;
                break;
            }
        }

        if (colNumMpaRiciba > -1 && colNumApmers > -1) {
            StringBuilder sbMpa = new StringBuilder();
            StringBuilder sbSums = new StringBuilder();
            char separator = ';';

            for (int i = 1; i <= dataSheet.getLastRowNum(); i++) {
                if (dataSheet.getRow(i).getCell(colNumMpaRiciba) != null && !dataSheet.getRow(i).getCell(colNumMpaRiciba).toString().isEmpty()) {
                    sbMpa.append(dataSheet.getRow(i).getCell(colNumMpaRiciba).getStringCellValue());
                    if (dataSheet.getRow(i).getCell(colNumApmers) == null || dataSheet.getRow(i).getCell(colNumApmers).toString().isEmpty()) {
                        sbSums.append("-");
                    }
                    Cell cell = dataSheet.getRow(i).getCell(colNumApmers);
                    cell.setCellType(Cell.CELL_TYPE_STRING);

                    sbSums.append(cell.getStringCellValue());
                    sbMpa.append(separator);
                    sbSums.append(separator);
                }

            }
            String assets = sbMpa.substring(0, sbMpa.length() - 1);
            String assetSums = sbSums.substring(0, sbSums.length() - 1);

            InsolvencyProcess insolvencyProcess = new InsolvencyProcess();
            insolvencyProcess.setId(processId);
            insolvencyProcess.setAssetsList(assets);
            insolvencyProcess.setAssetsListCosts(assetSums);

            insolvencyProcess.setAssetsTotalCosts(dataSheet.getRow(dataSheet.getLastRowNum()).getCell(colNumApmers).getStringCellValue());
            insolvencyProcessService.editInsolvencyProcessAssets(insolvencyProcess);

        }
    }
    /*------------------------------------------------*////

    public ByteArrayOutputStream exportAdminBlank(Long id) throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/adminBlank.docx");
        XWPFDocument adminBlank = new XWPFDocument(inputStream);

        try {

            this.replaceHeaderText(adminBlank);
            replaceText(adminBlank, "Place", administratorService.findAdministratorById(1L).getAdminAddress());
            replaceText(adminBlank, "Maksātnespējīgā companyName", "Maksātnespējīgā " +  insolvencyProcessService.findInsolvencyProcessById(id).getCompanyName());
            replaceText(adminBlank, "vienotais reģistrācijas Nr. registrationNumber", "vienotais reģistrācijas Nr. " +  insolvencyProcessService.findInsolvencyProcessById(id).getRegistrationNumber());
            replaceText(adminBlank, "Place", administratorService.findAdministratorById(1L).getAdminAddress());



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

            replaceHeaderText(doc);
            replaceText(doc, "Maksātnespējīgā companyName", "Maksātnespējīgā " +  insolvencyProcessService.findInsolvencyProcessById(id).getCompanyName());
            replaceText(doc, "vienotais reģistrācijas Nr. registrationNumber", "vienotais reģistrācijas Nr. " +  insolvencyProcessService.findInsolvencyProcessById(id).getRegistrationNumber());
            replaceText(doc, "Place", administratorService.findAdministratorById(1L).getAdminAddress());



        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        out.close();
        doc.close();

        return out;
    }

    public void handleCostsUpload(InputStream stream, Long processId) throws Exception {
        Workbook workbook = WorkbookFactory.create(stream);

        Sheet dataSheet = workbook.getSheetAt(0);
        int colIegutieNaudas = -1;
        for (int i = 0; i <= dataSheet.getRow(0).getLastCellNum(); i++) {
            Cell cell1 = dataSheet.getRow(0).getCell(i);
            String cellValue1 = cell1.getStringCellValue();
            if ("Iegūtie naudas līdzekļi no".equals(cellValue1)) {
                colIegutieNaudas = i;
                break;
            }
        }

        int colSumma = -1;
        for (int i = 0; i <= dataSheet.getRow(0).getLastCellNum(); i++) {
            Cell cell1 = dataSheet.getRow(0).getCell(i);
            String cellValue1 = cell1.getStringCellValue();
            if ("Summa".equals(cellValue1)) {
                colSumma = i;
                break;
            }
        }
        double neikilataMantaSum = 0;
        if (colIegutieNaudas > -1 && colSumma > -1) {

            for (int i = 1; i <= dataSheet.getLastRowNum(); i++) {
                Cell cell = dataSheet.getRow(i).getCell(colIegutieNaudas);
                String stringCostValue = dataSheet.getRow(i).getCell(colSumma).getStringCellValue();
                if (cell.getStringCellValue().equals("Neieķīlātā manta")) {
                    double value = Double.parseDouble(stringCostValue.replace(',', '.'));
                    neikilataMantaSum = neikilataMantaSum + value;
                }

            }

        }

        InsolvencyProcess insolvencyProcess = new InsolvencyProcess();
        insolvencyProcess.setId(processId);
        insolvencyProcess.setNeikilataMantaSum(neikilataMantaSum);

        insolvencyProcessService.editInsolvencyProcessCosts(insolvencyProcess);
        System.out.println(neikilataMantaSum);

    }


    public void handleMoneyUpload(InputStream stream, Long processId) throws Exception {

        Workbook workbook = WorkbookFactory.create(stream);

        Sheet dataSheet = workbook.getSheetAt(0);
        int colNaudasLidzekli = -1;
        for (int i = 0; i <= dataSheet.getRow(0).getLastCellNum(); i++) {
            Cell cell1 = dataSheet.getRow(0).getCell(i);
            String cellValue1 = cell1.getStringCellValue();
            if ("Naudas līdzekļi kontā perioda sākumā (EUR)".equals(cellValue1)) {
                colNaudasLidzekli = i;
                break;
            }
        }
        String cellNaudasLidzekli = "";

        if (colNaudasLidzekli > -1) {

           cellNaudasLidzekli = dataSheet.getRow(dataSheet.getLastRowNum()).getCell(colNaudasLidzekli).getStringCellValue();

        }
        InsolvencyProcess insolvencyProcess = new InsolvencyProcess();
        insolvencyProcess.setId(processId);
        insolvencyProcess.setProcessMoney(cellNaudasLidzekli);

        insolvencyProcessService.editInsolvencyProcessMoney(insolvencyProcess);
        System.out.println(cellNaudasLidzekli);

    }

    public void handleExpensesUpload(InputStream stream, Long processId) throws Exception {

        Workbook workbook = WorkbookFactory.create(stream);

        Sheet dataSheet = workbook.getSheetAt(0);
        int colIzmaksasRadusasNo = -1;
        for (int i = 0; i <= dataSheet.getRow(0).getLastCellNum(); i++) {
            Cell cell1 = dataSheet.getRow(0).getCell(i);
            String cellValue1 = cell1.getStringCellValue();
            if ("Izmaksas radušās no".equals(cellValue1)) {
                colIzmaksasRadusasNo = i;
                break;
            }
        }

        int colPozicijas = -1;
        for (int i = 0; i <= dataSheet.getRow(0).getLastCellNum(); i++) {
            Cell cell1 = dataSheet.getRow(0).getCell(i);
            String cellValue1 = cell1.getStringCellValue();
            if ("Pozīcijas nosaukums".equals(cellValue1)) {
                colPozicijas = i;
                break;
            }
        }

        int colIzmaksuSum = -1;
        for (int i = 0; i <= dataSheet.getRow(0).getLastCellNum(); i++) {
            Cell cell1 = dataSheet.getRow(0).getCell(i);
            String cellValue1 = cell1.getStringCellValue();
            if ("Izmaksu summa".equals(cellValue1)) {
                colIzmaksuSum = i;
                break;
            }
        }
        double izmaksasTotal = 0;
        double administratorSalary = 0;
        if (colIzmaksasRadusasNo > -1 && colPozicijas > -1 && colIzmaksuSum > -1) {

            for (int i = 1; i <= dataSheet.getLastRowNum(); i++) {
                String cellIsmaksasRadusas = dataSheet.getRow(i).getCell(colIzmaksasRadusasNo).getStringCellValue();
                String cellPozicijas = dataSheet.getRow(i).getCell(colPozicijas).getStringCellValue();
                String stringExpensesValue = dataSheet.getRow(i).getCell(colIzmaksuSum).getStringCellValue();


                if (cellIsmaksasRadusas.equals("Neieķīlātā manta") && !cellPozicijas.contains("Administratora") ) {
                    double izmaksasTotalDouble = Double.parseDouble(stringExpensesValue.replace(',', '.'));
                    izmaksasTotal = izmaksasTotal + izmaksasTotalDouble;
                }

                if (cellPozicijas.equals("Administratora atlīdzība par pienākumu pildīšanu maksātnespējas procesā")) {
                    double administratorSalaryDouble = Double.parseDouble(stringExpensesValue.replace(',', '.'));
                    administratorSalary = administratorSalary + administratorSalaryDouble;
                }

            }

        }

        InsolvencyProcess insolvencyProcess = new InsolvencyProcess();
        insolvencyProcess.setId(processId);
        insolvencyProcess.setTotalExpenses(izmaksasTotal);
        insolvencyProcess.setAdminSalary(administratorSalary);

        insolvencyProcessService.editInsolvencyProcessExpenses(insolvencyProcess);
        System.out.println(izmaksasTotal);
        System.out.println(administratorSalary);

    }
}
