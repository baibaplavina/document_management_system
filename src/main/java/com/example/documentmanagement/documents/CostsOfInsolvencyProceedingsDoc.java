package com.example.documentmanagement.documents;

import com.example.documentmanagement.insolvencyprocess.InsolvencyProcess;
import com.example.documentmanagement.otherExpenses.OtherExpenses;
import com.example.documentmanagement.otherExpenses.OtherExpensesService;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CostsOfInsolvencyProceedingsDoc extends TemplateService {

    private final InsolvencyProcess insolvencyProcess;
    private final OtherExpensesService otherExpensesService;
    private XWPFDocument doc;

    public CostsOfInsolvencyProceedingsDoc(InsolvencyProcess insolvencyProcess, OtherExpensesService otherExpensesService) {

        this.insolvencyProcess = insolvencyProcess;
        this.otherExpensesService = otherExpensesService;

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

        replaceText(doc, "Place",
                insolvencyProcess.getAdmin().getPlace());
        replaceText(doc, "Document_date",

                LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth());
        replaceText(doc, "/administratorName AdministratorSurname/",
                insolvencyProcess.getAdmin().getAdminName() + " " +
                        insolvencyProcess.getAdmin().getAdminSurname());
        replaceText(doc, "/sertificateNumber/",
                insolvencyProcess.getAdmin().getCertificateNumber());
        replaceText(doc, "/administratorAddress/",
                insolvencyProcess.getAdmin().getAdminAddress());
        replaceText(doc, "/administratorPhoneNumber/",
                insolvencyProcess.getAdmin().getAdminPhoneNumber());
        replaceText(doc, "/adminisratorEmail/",
                insolvencyProcess.getAdmin().getAdminEmail());
        replaceText(doc, "/administratorEAddress/",
                " " + insolvencyProcess.getAdmin().getAdminE_address());
        replaceText(doc, "InsolvencyCompanyName",
                insolvencyProcess.getCompanyName());
        replaceText(doc, "CompanyName",
                insolvencyProcess.getCompanyName());
        replaceText(doc, "companyName",
                insolvencyProcess.getCompanyName());
        replaceText(doc, "vienotais reģistrācijas Nr. ", " vienotais reģistrācijas Nr. " +
                insolvencyProcess.getRegistrationNumber());

        replaceText(doc, "courtName",
                insolvencyProcess.getCourtName());
        replaceText(doc, "courtDesitionDate",
                " " + insolvencyProcess.getCourtDecisionDate() + " ");
        replaceText(doc, "courtCaseNumber",
                insolvencyProcess.getCourtCaseNumber());
        replaceText(doc, "registrationNumber",
                insolvencyProcess.getRegistrationNumber());
        replaceText(doc, "administratorName",
                insolvencyProcess.getAdmin().getAdminName());
        replaceText(doc, "administratorSurname",
                insolvencyProcess.getAdmin().getAdminSurname());
        replaceText(doc, "certificateNumber",
                insolvencyProcess.getAdmin().getCertificateNumber());
        replaceText(doc, "administratorAdress",
                insolvencyProcess.getAdmin().getAdminAddress());

    }

    private void createCreditorsPart() {

        String creditors = insolvencyProcess.getCreditorsList();
        if (creditors != null && !creditors.isEmpty()) {
            List<String> listOfCreditors = new ArrayList<>(Arrays.asList(creditors.split(";")));
            int creditorTextPosition = getParagraphPositionIfContainsText("Kreditori:", doc);
            if (creditorTextPosition > -1) {
                for (int i = listOfCreditors.size() - 1; i >= 0; i--) {

                    XWPFParagraph new_par = insertNewParagraph(creditorTextPosition + 1, doc);
                    new_par.setAlignment(ParagraphAlignment.RIGHT);
                    new_par.createRun().setText(listOfCreditors.get(i));
                }
            }
        }
    }

    private void createSecuredAssetsPart() {
        int beforeAssetsTablePosition = getParagraphPositionIfContainsText("maksātnespējas procesa izmaksu sarakstu, kas attiecas uz nodrošināto mantu:", doc);
        XWPFParagraph new_paragraph = insertNewParagraph(beforeAssetsTablePosition + 2, doc);

        if (!insolvencyProcess.getSecuredAssets()) {
            new_paragraph.setFirstLineIndent(700);
            XWPFRun run = new_paragraph.createRun();
            run.setText("Sabiedrībai nav mantas, kas kalpotu par nodrošinājumu maksātnespējas procesā.");
            XWPFParagraph new_paragraph1 = insertNewParagraph(beforeAssetsTablePosition + 3, doc);
            new_paragraph1.setFirstLineIndent(700);
            new_paragraph1.createRun().setText("Sabiedrības maksātnespējas procesā nav nodrošināto kreditoru prasījumu.");

        } else {
            XWPFRun run = new_paragraph.createRun();
            run.setBold(true);
            run.setText("1.1. Ienākumi no nodrošinātās mantas maksātnespējas procesā");

            XWPFParagraph new_paragraph1 = insertNewParagraph(beforeAssetsTablePosition + 3, doc);
            create_1_1_or_2_1table(new_paragraph1, insolvencyProcess.getAssetsList_iekilata(), insolvencyProcess.getAssetsListCosts_iekilata(),
                    insolvencyProcess.getAssetsTotalCosts_iekilata());

            XmlCursor cursor3 = new_paragraph1.getCTP().newCursor();
            cursor3.toNextSibling();

            XWPFParagraph new_paragraph3 = doc.insertNewParagraph(cursor3);
            XWPFRun run3 = new_paragraph3.createRun();
            run3.setBold(true);
            run3.setText("1.2. Izmaksas, kas saistītas ar nodrošināto mantu ");

            XmlCursor cursor4 = new_paragraph3.getCTP().newCursor();
            cursor4.toNextSibling();
            XWPFParagraph new_paragraph5 = doc.insertNewParagraph(cursor4);
            List<OtherExpenses> listSecuredExpenses = otherExpensesService.findSecuredAssetsByProcess(insolvencyProcess);
            create1_2_or_2_2_table(new_paragraph5, listSecuredExpenses);
        }
    }

    private void createUnsecuredAssetsPart() {

        int table2_1_HeaderPosition = getParagraphPositionIfContainsText("Ienākumi no nenodrošinātās mantas maksātnespējas procesā ", doc);
        XWPFParagraph new_paragraph1 = insertNewParagraph(table2_1_HeaderPosition + 1, doc);
        create_1_1_or_2_1table(new_paragraph1, insolvencyProcess.getAssetsList_neiekilata(), insolvencyProcess.getAssetsListCosts_neiekilata(), insolvencyProcess.getAssetsTotalCosts_neiekilata());

        int table2_2_HeaderPosition = getParagraphPositionIfContainsText("Izmaksas, kas saistītas ar nenodrošināto kustamo mantu", doc);
        XWPFParagraph new_paragraph2 = insertNewParagraph(table2_2_HeaderPosition + 1, doc);
        List<OtherExpenses> listUnsecuredExpenses = otherExpensesService.findUnsecuredAssetsByProcess(insolvencyProcess);
        create1_2_or_2_2_table(new_paragraph2, listUnsecuredExpenses);

    }

    private void create_1_1_or_2_1table(XWPFParagraph p, String assetList, String sums, String totalCost) {

        XWPFTable createdTable = p.getBody().insertNewTbl(p.getCTP().newCursor());

        createStyledTableHeader(createdTable, Arrays.asList(
                "Summa, EUR",
                "Pamatojums"
        ));

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
        styleCellBold(row.getCell(0), getStyledString(totalCost));
        styleCellBold(row.getCell(1), "Kopā /Total");

        doc.createParagraph();

    }

    private void create1_2_or_2_2_table(XWPFParagraph p, List<OtherExpenses> listOfFilteredExpenses) {
        XWPFTable createdTable = p.getBody().insertNewTbl(p.getCTP().newCursor());
        List<String> headerValues = Arrays.asList(
                "Maksājuma tips",
                "Pakalpojoums",
                "Saņēmējs",
                "Izmaksu rašanās datums",
                "Segtā summa, EUR",
                "Maksājuma datums",
                "Radušās un nav apmaksātas izmaksas EUR");

        createStyledTableHeader(createdTable, headerValues);
        double totalSegtaSummaIzdevumi = 0;
        double totalNavApmaksataIzdevumi = 0;

        for (OtherExpenses expense : listOfFilteredExpenses) {

            if (expense.getName().equals("MPA atlīdzība") &&
                    (expense.getSum() != 0 || expense.getUnpaid() != 0)) {
                XWPFTableRow row = createdTable.createRow();

                styleCellMinimized(row.getCell(0), "Administratora atlīdzība");
                styleCellMinimized(row.getCell(1), expense.getName());
                styleCellMinimized(row.getCell(2), insolvencyProcess.getAdmin().getAdminName() + " " + insolvencyProcess.getAdmin().getAdminSurname());
                styleCellMinimized(row.getCell(3), expense.getCreatingDate());
                styleCellMinimized(row.getCell(4), expense.getSum());
                styleCellMinimized(row.getCell(5), expense.getOtherDate());
                styleCellMinimized(row.getCell(6), expense.getUnpaid());
            }
        }

        XWPFTableRow row = createdTable.createRow();
        styleCellMinimized(row.getCell(0), "Izdevumi");

        for (OtherExpenses expense : listOfFilteredExpenses) {

            if ((expense.getSum() != 0 || expense.getUnpaid() != 0) &&
                    (!expense.getName().equals("MPA atlīdzība")) && (!expense.getName().contains("Administratora"))) {

                XWPFTableRow row1 = createdTable.createRow();
                styleCellMinimized(row1.getCell(1), expense.getName());
                styleCellMinimized(row1.getCell(2), expense.getRecipient());
                styleCellMinimized(row1.getCell(3), expense.getCreatingDate());
                styleCellMinimized(row1.getCell(4), expense.getSum());
                styleCellMinimized(row1.getCell(5), expense.getOtherDate());
                styleCellMinimized(row1.getCell(6), expense.getUnpaid());

                totalSegtaSummaIzdevumi = totalSegtaSummaIzdevumi + expense.getSum();
                totalNavApmaksataIzdevumi = totalNavApmaksataIzdevumi + expense.getUnpaid();
            }
        }

        XWPFTableRow row2 = createdTable.createRow();
        styleCellMinimized(row2.getCell(3), "Kopā izdevumi, Eur:");
        styleCellMinimized(row2.getCell(4), totalSegtaSummaIzdevumi);
        styleCellMinimized(row2.getCell(6), totalNavApmaksataIzdevumi);
    }


    private void createPart3And4() {

        int indexOf3rdTable = doc.getTables().size() - 1;
        XWPFTable table = doc.getTables().get(indexOf3rdTable);

        double lineNumber5Value =
                insolvencyProcess.getNeikilataMantaSum() +
                        doubleFromString(insolvencyProcess.getProcessMoney()) -
                        insolvencyProcess.getTotalExpenses() -
                        insolvencyProcess.getAdminSalary();

        double lineNumber6Value = lineNumber5Value * 0.1;
        double lineNumber7Value = lineNumber5Value - lineNumber6Value;

        styleCell(table.getRow(1).getCell(2), insolvencyProcess.getNeikilataMantaSum());
        styleCell(table.getRow(2).getCell(2), insolvencyProcess.getProcessMoney());
        styleCell(table.getRow(3).getCell(2), insolvencyProcess.getTotalExpenses());
        styleCell(table.getRow(4).getCell(2), insolvencyProcess.getAdminSalary());
        styleCell(table.getRow(5).getCell(2), lineNumber5Value);
        styleCell(table.getRow(6).getCell(2), lineNumber6Value);
        styleCellBold(table.getRow(7).getCell(2), getStyledString(lineNumber7Value));

        createPart4(lineNumber7Value);
    }


    private void createPart4(double lineNumber7Value) {

        replaceText(doc, "/This document table 3 last No/", getStyledString(lineNumber7Value) + " EUR");
        replaceText(doc, "InsertUnsecuredAssetCostsTotal", getStyledString(insolvencyProcess.getTotalExpenses()) + " EUR");
        replaceText(doc, "prasījumu apmērs: EUR", "prasījumu apmērs: " + getStyledString(insolvencyProcess.getCreditorsRequest()) + " EUR.");
        replaceText(doc, "InsertTOTALSecuredCosts", getStyledString(insolvencyProcess.getAssetsListCosts_iekilata()) + " EUR");

        if (insolvencyProcess.getMaksatnespejasKontrolesApmers() != null && !insolvencyProcess.getMaksatnespejasKontrolesApmers().isEmpty()) {
            replaceText(doc, "ir/nav", "ir");
            int par_index = getParagraphPositionIfContainsText("sedzis parādnieka darbinieka prasījumus", doc);
            XWPFRun run = doc.getParagraphs().get(par_index).createRun();
            run.setBold(true);
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.setText("Maksātnespējas kontroles dienesta segto darbinieku prasījumu summa darbiniekiem: " +
                    insolvencyProcess.getMaksatnespejasKontrolesApmers() + " EUR, valsts ieņēmumu dienestam: " +
                    insolvencyProcess.getValstsIenemumuApmers() + " EUR.");

        } else {
            replaceText(doc, "ir/nav", "nav");
        }

        if (insolvencyProcess.getValstsIenemumuApmers() != null && !insolvencyProcess.getValstsIenemumuApmers().isEmpty()) {

            int par_index = getParagraphPositionIfContainsText("nodokļu prasījums novirzot naudas līdzekļus tam", doc);
            XWPFParagraph paragraph = insertNewParagraph(par_index + 1, doc);
            XWPFRun run = paragraph.createRun();
            run.setBold(true);
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.setText("Valsts ieņēmumu dienesta kreditoru prasījuma apmērs: " +
                    insolvencyProcess.getValstsIenemumuApmers() + " EUR.");

        }
    }

}
