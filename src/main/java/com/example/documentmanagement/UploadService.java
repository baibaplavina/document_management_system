package com.example.documentmanagement;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class UploadService {


    private final InsolvencyProcessService insolvencyProcessService;

    @Autowired
    public UploadService(InsolvencyProcessService insolvencyProcessService) {

        this.insolvencyProcessService = insolvencyProcessService;
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

    public void handleAssetsUpload(InputStream stream, Long processId) throws Exception {
        Workbook workbook = WorkbookFactory.create(stream);

        Sheet dataSheet = workbook.getSheetAt(0);
        int colMantasTips = -1;
        for (int i = 0; i <= dataSheet.getRow(0).getLastCellNum(); i++) {
            Cell cell1 = dataSheet.getRow(0).getCell(i);
            String cellValue1 = cell1.getStringCellValue();
            if ("Mantas tips".equals(cellValue1)) {
                colMantasTips = i;
                break;
            }
        }

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

        if (colMantasTips > -1 && colNumMpaRiciba > -1 && colNumApmers > -1) {
            StringBuilder sbMpa = new StringBuilder();
            StringBuilder sbSums = new StringBuilder();
            char separator = ';';
            boolean securedAssets = false;

            for (int i = 1; i <= dataSheet.getLastRowNum(); i++) {
                if(dataSheet.getRow(i).getCell(colMantasTips).getStringCellValue().equals("Ieķīlātā manta")) {
                    securedAssets = true;
                    break;
                }
            }

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
            insolvencyProcess.setSecuredAssets(securedAssets);

            insolvencyProcess.setAssetsTotalCosts(dataSheet.getRow(dataSheet.getLastRowNum()).getCell(colNumApmers).getStringCellValue());
            insolvencyProcessService.editInsolvencyProcessAssets(insolvencyProcess);

        }
    }
}
