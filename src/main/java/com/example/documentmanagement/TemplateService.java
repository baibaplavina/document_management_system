package com.example.documentmanagement;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
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
            replacePlaceDateText(doc);
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
                        administratorService.findAdministratorById(2L).getAdminName() + " " +
                        administratorService.findAdministratorById(2L).getAdminSurname() +
                        " (amata apliecības Nr. " + administratorService.findAdministratorById(2L).getCertificateNumber() + ")");


        replaceText(doc, "Adrese: /administratorAddress/, telefons: /administratorPhoneNumber/,  e-pasts: /adminisratorEmail/, e-Adrese:/administratorEAddress/",
                "Adrese: " + " " +
                        administratorService.findAdministratorById(2L).getAdminAddress() + ", telefons: " +
                        administratorService.findAdministratorById(2L).getAdminPhoneNumber() +
                        ", e-pasts: " + administratorService.findAdministratorById(2L).getAdminEmail() +
                        ", e-Adrese: " + administratorService.findAdministratorById(2L).getAdminE_address());
    }

    private void replaceCompanyAdminHeaderText(XWPFDocument doc, Long id) throws Exception {
       InsolvencyProcess process = insolvencyProcessService.findInsolvencyProcessById(id);
        replaceText(doc, "Maksātnespējas procesa administrators administratorName AdministratorSurname (amata apliecības Nr. sertificateNumber))",
                "Maksātnespējas procesa administrators " + process.getAdmin().getAdminName()+ " " + process.getAdmin().getAdminSurname() +
                        " (amata apliecības Nr. " + process.getAdmin().getCertificateNumber());

        replaceText(doc, "Adrese: /administratorAddress/, telefons: /administratorPhoneNumber/,  e-pasts: /adminisratorEmail/, e-Adrese:/administratorEAddress/",
                "Adrese: " + " " +
                        process.getAdmin().getAdminAddress() + ", telefons: " +
                        process.getAdmin().getAdminPhoneNumber() +
                        ", e-pasts: " + process.getAdmin().getAdminEmail() +
                        ", e-Adrese: " + process.getAdmin().getAdminE_address());
    }
    private void replacePlaceDateText(XWPFDocument doc) throws Exception {
        LocalDate date = LocalDate.now();
        replaceText(doc,"Place, Date.",
                administratorService.findAdministratorById(2L).getPlace() + ", " + date);
    }
    public ByteArrayOutputStream exportWordDoc() throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/template1.docx");
        XWPFDocument doc = new XWPFDocument(inputStream);

        try {

            replaceHeaderText(doc);
            replacePlaceDateText(doc);

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
            String creditors = insolvencyProcessService.findInsolvencyProcessById(processId).getCreditorsList();
            List<String> listOfCreditors = new ArrayList<>(Arrays.asList(creditors.split(";")));
            int position = 0;
            for (int i = 0; i<doc.getParagraphs().size(); i++) {
                if(doc.getParagraphs().get(i).getText().equals("Kreditori:")){
                    position = i;
                    break;
                }
            }

            for (int j = listOfCreditors.size()-1; j>=0; j--) {
                XmlCursor cursor = doc.getParagraphs().get(position+1).getCTP().newCursor();
                XWPFParagraph new_par = doc.insertNewParagraph(cursor);
                new_par.setAlignment(ParagraphAlignment.RIGHT);
                new_par.createRun().setText(listOfCreditors.get(j));

            }

            replaceHeaderText(doc);
            replaceText(doc, "InsolvencyCompanyName",
                    insolvencyProcessService.findInsolvencyProcessById(processId).getCompanyName());
            replaceText(doc, "vienotais reģistrācijas Nr. ", " vienotais reģistrācijas Nr. " +
                    insolvencyProcessService.findInsolvencyProcessById(processId).getRegistrationNumber());

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
            replaceText(adminBlank, "Place", administratorService.findAdministratorById(2L).getAdminAddress());
            replaceText(adminBlank, "Maksātnespējīgā companyName", "Maksātnespējīgā " +  insolvencyProcessService.findInsolvencyProcessById(id).getCompanyName());
            replaceText(adminBlank, "vienotais reģistrācijas Nr. registrationNumber", "vienotais reģistrācijas Nr. " +  insolvencyProcessService.findInsolvencyProcessById(id).getRegistrationNumber());
            replaceText(adminBlank, "Place", administratorService.findAdministratorById(2L).getAdminAddress());



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
            replaceCompanyAdminHeaderText(doc, id);
            replaceCompanyParagraphText(doc, id);
            replaceIeceltaIeceltsParagraphText(doc,id);
            replacePlaceDateText(doc);

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

   public ByteArrayOutputStream exportAuthorityBlank(Long id, int number) throws IOException {
       InputStream inputStream = getClass().getResourceAsStream("/companyBlank.docx");
       XWPFDocument doc = new XWPFDocument(inputStream);

       try {
          replaceCompanyAdminHeaderText(doc,id);
           replaceCompanyParagraphText(doc, id);
           replacePlaceDateText(doc);
           replaceIeceltaIeceltsParagraphText(doc,id);
           replaceAuthorityDocNameText(doc);

           switch (number){
               case 1:
                   replaceAuthorityMainText1(doc);
                   break;
               case 2:
                   replaceAuthorityMainText2(doc);
                   break;
               case 3:
                   replaceAuthorityMainText3(doc, id);
                   break;
               case 4:
                   replaceAuthorityMainText4(doc, id);
                   break;
               case 5:
                   replaceAuthorityMainText5(doc, id);
                   break;
               case 6:
                   replaceAuthorityMainText6(doc, id);
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

    private void replaceCompanyParagraphText(XWPFDocument doc, Long id) throws Exception{
        replaceText(doc,"companyName",
                      ""+ insolvencyProcessService.findInsolvencyProcessById(id).getCompanyName());
        replaceText(doc,"registrationNumber",
                "" + insolvencyProcessService.findInsolvencyProcessById(id).getRegistrationNumber());
        replaceText(doc,"courtName",
                "" + insolvencyProcessService.findInsolvencyProcessById(id).getCourtName());
        replaceText(doc,"administratorName administratorSurname",
                "" + insolvencyProcessService.findInsolvencyProcessById(id).getAdmin().getAdminName() + " " +
                insolvencyProcessService.findInsolvencyProcessById(id).getAdmin().getAdminSurname());
        replaceText(doc,"courtDesitionDate",
                "" + insolvencyProcessService.findInsolvencyProcessById(id).getCourtDecisionDate());
        replaceText(doc,"courtCaseNumber",
                "" + insolvencyProcessService.findInsolvencyProcessById(id).getCourtCaseNumber() + " ");
        replaceText(doc,"certificateNumber",
                "" + insolvencyProcessService.findInsolvencyProcessById(id).getAdmin().getCertificateNumber() + " ");
        replaceText(doc,"administratorAdress",
                "" + insolvencyProcessService.findInsolvencyProcessById(id).getAdmin().getAdminAddress());
                }



    private void replaceIeceltaIeceltsParagraphText(XWPFDocument doc, Long id) throws Exception {
        InsolvencyProcess process = insolvencyProcessService.findInsolvencyProcessById(id);

             boolean isFemale = process.getAdmin().getAdminGender().equals(Gender.FEMALE);
        if (isFemale){
        replaceText(doc,"iecelta/iecelts",
                        "iecelta");
    } else if (process.getAdmin().getAdminGender().equals(Gender.MALE)){
        replaceText(doc, "iecelta/iecelts",
                "iecelts");
    }

    }

    private void replaceAuthorityDocNameText (XWPFDocument doc) throws Exception {
        replaceText(doc, "Document Name (Dokumenta nosaukums)",
                "Informācijas pieprasījums");
    }

  private void replaceAuthorityMainText1 (XWPFDocument doc) throws Exception{

      replaceText(doc,"Nosaukums (recipientName)",
              "Uzņēmumu reģistrs, ");
      replaceText(doc,"Reģistrācijas Nr.(Registration No)",
              "Reģistrācijas numurs 90000270634,");
      replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
              "E-pasts: info@ur.gov.lv");
      replaceText(doc,"TEXT",
                    "Lūdzu sniegt : <br/>  1) aktuālo   informāciju   par   Sabiedrības  dalībniekiem   un   valdi" +
                            "(Standartizēta izziņa no visiem Uzņēmumu reģistra reģistriem).\r" +
                            "2) aktuālo   un   vēsturisko   informāciju   par   Sabiedrības," +
                            "kura satur atbildes uz sekojošiem jautājumiem:\r\n" +
                            "2.1. Kas ir/ ir bijuši Sabiedrības valdes locekļi?\n" +
                            "2.2. Kas ir/ ir bijuši Sabiedrības dalībnieki?\n" +
                            "2.3. Vai Sabiedrībai ir/ ir bijuši reģistrēti prokūristi un ja ir tad kādi?\n" +
                            "2.4. Vai Sabiedrībai ir/ ir bijušas piederējušas vai pieder kapitāla daļas\n" +
                            "citā juridiskā personā un ja ir tad kādā?\n" +
                            "2.5. Vai Sabiedrībai ir/ ir bijuši reģistrētas komercķīlas, komercķīlu akti\n" +
                            "un ja ir tad kādas?\n" +
                            "2.6. Vai   Sabiedrībai   ir/   ir   bijuši   reģistrēti   nodrošinājuma   līdzekļi?   Vai\n" +
                            "Sabiedrībai ir/ ir bijuši reģistrēti aizliegumi un ja ir tad kādi?\n" +
                            "2.7. Vai Sabiedrībai ir/ ir bijušas reģistrētas filiāles un ja ir tad kāda?\n");
     }

    private void replaceAuthorityMainText2 (XWPFDocument doc) throws Exception{

        replaceText(doc,"Nosaukums (recipientName)",
                "VAS “Latvijas Jūras administrācija” kuģu reģistrs, ");
        replaceText(doc,"Reģistrācijas Nr.(Registration No)",
                "Reģistrācijas numurs: 40003022705,");
        replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
                "E-pasts: lja@lja.lv");
        replaceText(doc,"TEXT",
                "Lūdzu sniegt Jūsu rīcībā esošo informāciju par Sabiedrību, -kādi kuģi, " +
                        "tajā skaitā zvejas laivas, atpūtas kuģi - buru jahtas, motorjahtas un peldošās " +
                        "konstrukcijas (peldošie doki, peldošās darbnīcas, peldošās degvielas uzpildes stacijas, " +
                        "debarkaderi, kravas pontoni), šobrīd ir un vai ir bijuši reģistrēti Sabiedrībai, no kura " +
                        "līdz kuram brīdim reģistrēti, un kādi liegumi - hipotēkas un apgrūtinājumi šobrīd ir vai ir bijuši reģistrēti.");
    }

    private void replaceAuthorityMainText3 (XWPFDocument doc, Long id) throws Exception{

        replaceText(doc,"Nosaukums (recipientName)",
                "Lauksaimniecības datu centrs,");
        replaceText(doc,"Reģistrācijas Nr.(Registration No)",
                "Reģistrācijas numurs: 90001840100,");
        replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
                "E-pasts: ldc@ldc.gov.lv");
        String text = String.valueOf(replaceText(doc, "TEXT",
                "Lūdzu sniegt informāciju par lauksaimniecības un citiem dzīvniekiem, kas ir reģistrēti un ir " +
                        "bijuši reģistrēti, kā arī tie kuri atrodas " + insolvencyProcessService.findInsolvencyProcessById(id).getCompanyName() +
                        ", vienotais reģistrācijas numurs " + insolvencyProcessService.findInsolvencyProcessById(id).getRegistrationNumber() +
                        " īpašumā un/vai valdījumā"));
    }
    private void replaceAuthorityMainText4 (XWPFDocument doc, Long id) throws Exception{

        replaceText(doc,"Nosaukums (recipientName)",
                "Valsts tehniskās uzraudzības aģentūra,");
        replaceText(doc,"Reģistrācijas Nr.(Registration No)",
                "Reģistrācijas numurs: 90001834941,");
        replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
                "E-pasts: vtua@vtua.gov.lv");
        String text = String.valueOf(replaceText(doc, "TEXT",
                "Lūdzu sniegt Jūsu rīcībā esošo informāciju kāda traktortehnika un tās piekabes šobrīd ir un / vai ir " +
                        "bijuši reģistrēti uz Sabiedrības vārda un kādi liegumi šobrīd ir vai ir bijuši reģistrēti."));
    }


    private void replaceAuthorityMainText5 (XWPFDocument doc, Long id) throws Exception{
        replaceText(doc,"Nosaukums (recipientName)",
                "Valsts Zemes dienests, ");
        replaceText(doc,"Reģistrācijas Nr.(Registration No)",
                "Reģistrācijas numurs: 90000030432, ");
        replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
                "E-pasts: kac.riga@vzd.gov.lv");
        replaceText(doc,"TEXT",
                "Lūdzu sniegt sekojošu aktuālo un vēsturisko informāciju par " + insolvencyProcessService.findInsolvencyProcessById(id).getCompanyName() +
                        insolvencyProcessService.findInsolvencyProcessById(id).getRegistrationNumber()+ "īpašumā esošajiem " +
                        "un bijušajiem reģistrētajiem nekustamajiem īpašumiem, kā arī par reģistrētajiem īpašuma apgrūtinājumiem.");
    }

    private void replaceAuthorityMainText6 (XWPFDocument doc, Long id) throws Exception{
        replaceText(doc,"Nosaukums (recipientName)",
                "Zvērinātam tiesu izpildītājam _______________, ");
        replaceText(doc,"Reģistrācijas Nr.(Registration No)",
                "Reģistrācijas numurs: ___________________, ");
        replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
                "E-pasts: _____________________");
        replaceText(doc,"TEXT",
                "TEXT: Saskaņā ar Maksātnespējas likuma 65.pantu Administratora pienākumi pēc juridiskās personas maksātnespējas procesa pasludināšanas – pirmās daļas 12. punktu - pēc juridiskās personas maksātnespējas procesa pasludināšanas administrators iesniedz tiesu izpildītājam pieteikumu par izpildu lietvedības izbeigšanu lietās par piespriesto, bet no parādnieka nepiedzīto summu piedziņu un lietās par saistību izpildīšanu tiesas ceļā.\n" +
                        "Maksātnespējas likuma 63. panta Juridiskās personas maksātnespējas procesa pasludināšanas sekas otrā daļa nosaka, ka, ja sprieduma izpildes lietvedība uzsākta pirms juridiskās personas maksātnespējas procesa pasludināšanas, tā ir izbeidzama Civilprocesa likumā noteiktajā kārtībā. Pēc juridiskās personas maksātnespējas procesa pasludināšanas kreditori piesaka prasījumus administratoram šajā likumā noteiktajā kārtībā.\n" +
                        "Saskaņā ar Civilprocesa likuma 563.panta Izpildu lietvedības izbeigšana otro daļu (2) Izpildu lietvedība par piespriesto naudas summu piedziņu no juridiskajām personām, personālsabiedrībām, individuālajiem komersantiem, ārvalstī reģistrētām personām, kas veic pastāvīgu saimniecisko darbību Latvijā, un lauksaimniecības produktu ražotājiem izbeidzama pēc administratora pieteikuma, ja parādniekam likumā noteiktajā kārtībā pasludināta maksātnespēja. Šajā gadījumā tiesu izpildītājs pabeidz uzsākto mantas pārdošanu, ja tā jau ir izsludināta vai manta nodota tirdzniecības uzņēmumam pārdošanai, ja vien administrators nav pieprasījis atcelt izsludinātās izsoles, lai nodrošinātu mantas pārdošanu lietu kopības sastāvā. No pārdošanā saņemtās naudas tiesu izpildītājs ietur sprieduma izpildes izdevumus, bet pārējo naudu nodod administratoram kreditoru prasījumu segšanai atbilstoši Maksātnespējas likumā noteiktajai kārtībai, ievērojot nodrošinātā kreditora tiesības. Tiesu izpildītājs paziņo mantas glabātājam par pienākumu nodot administratoram mantu, kuras pārdošana nav uzsākta.\n" +
                        "\n" +
                        "Ņemot vērā iepriekš minēto, lūdzu:\n" +
                        "1.\tIzbeigt visas uzsāktās izpildu lietvedības pret /Insolvent company name/, vienotais reģistrācijas numurs /company number/.\n" +
                        "2.\tAtcelt visus uzliktos liegumus, atzīmes, kā arī cita veida aizliegumus, kas uzlikti /Insolvent company name/, vienotais reģistrācijas numurs /company number/, mantai un norēķinu kontiem.\n" +
                        "3.\tAtcelt pieņemtos piespiedu izpildes līdzekļus.\n" +
                        "4.\tSniegt informāciju vai izpildu lietu ietvaros ir saņemti naudas līdzekļi, cik, kādā veidā un kādā apmērā; \n" +
                        "5.\tSniegt informāciju kādā apmērā un kad segts piedzinēja prasījums?\n" +
                        "6.\tAtsūtīt sprieduma un/vai izpildu raksta kopiju, uz kura pamata uzsākta izpildu lietvedība pret /Insolvent company name/, vienotais reģistrācijas numurs /company number/.\n" +
                        "7.\tNorādīt vai šobrīd ir piedziņas procesā saņemtie Piedzinējam neizmaksāti naudas līdzekļi? Ja ir, tad tos lūdzu neizmaksāt Piedzinējam, bet pārskaitīt uz maksātnespējas procesam atvērto norēķinu kontu – /private String accountNo/ , izmaksai maksātnespējas procesā kreditoriem Maksātnespējas procesa noteiktajā kārtībā. \n");
    }

    public void handleCreditorsUpload(InputStream stream, Long processId) throws Exception {

        Workbook workbook = WorkbookFactory.create(stream);

        Sheet dataSheet = workbook.getSheetAt(0);
        int colKreditors = -1;
        for (int i = 0; i <= dataSheet.getRow(0).getLastCellNum(); i++) {
            Cell cell1 = dataSheet.getRow(0).getCell(i);
            String cellValue1 = cell1.getStringCellValue();
            if ("Kreditors".equals(cellValue1)) {
                colKreditors = i;
                break;
            }
        }
        if (colKreditors > -1 ) {
            StringBuilder sbCreditors = new StringBuilder();

            char separator = ';';

            for (int i = 1; i <= dataSheet.getLastRowNum(); i++) {
                if (dataSheet.getRow(i).getCell(colKreditors) != null && !dataSheet.getRow(i).getCell(colKreditors).toString().isEmpty()) {
                    sbCreditors.append(dataSheet.getRow(i).getCell(colKreditors).getStringCellValue());

                    sbCreditors.append(separator);
                }
            }

            String sbCreditors_1 = sbCreditors.substring(0, sbCreditors.length() - 1);

            InsolvencyProcess insolvencyProcess = new InsolvencyProcess();
            insolvencyProcess.setId(processId);
            insolvencyProcess.setCreditorsList(sbCreditors_1);

            insolvencyProcessService.editInsolvencyProcessCreditors(insolvencyProcess);

        }
    }


}
