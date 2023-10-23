package com.example.documentmanagement.documents;


import com.example.documentmanagement.administrator.AdministratorService;
import com.example.documentmanagement.administrator.Gender;
import com.example.documentmanagement.insolvencyProcess.InsolvencyProcess;
import com.example.documentmanagement.insolvencyProcess.InsolvencyProcessService;
import com.example.documentmanagement.insolvencyProcess.otherExpenses.OtherExpensesService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Data
@Component
public class TemplateService {

    private AdministratorService administratorService;
    private InsolvencyProcessService insolvencyProcessService;
    private OtherExpensesService otherExpensesService;

    @Autowired
    public TemplateService(AdministratorService administratorService, InsolvencyProcessService insolvencyProcessService, OtherExpensesService otherExpensesService) {
        this.administratorService = administratorService;
        this.insolvencyProcessService = insolvencyProcessService;
        this.otherExpensesService = otherExpensesService;
    }


  void replaceHeaderText(XWPFDocument doc) throws Exception {
        replaceText(doc, "administratorName administratorSurname/ (amata apliecības Nr. /sertificateNumber/)",
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


   public void replaceCompanyAdminHeaderText(XWPFDocument doc, Long id) throws Exception {
       InsolvencyProcess insolvencyProcess = insolvencyProcessService.findInsolvencyProcessById(id);
       replaceText(doc,"administratorName",
               insolvencyProcess.getAdmin().getAdminName());
       replaceText(doc,"administratorSurname",
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

       void replacePlaceDateCompanyBlankText(XWPFDocument doc, Long id) throws Exception {
        InsolvencyProcess insolvencyProcess = insolvencyProcessService.findInsolvencyProcessById(id);
       replaceText(doc,"Place",
                "" + insolvencyProcessService.findInsolvencyProcessById(id).getAdmin().getPlace());
        LocalDate documentDate = LocalDate.now();
        replaceText(doc, "Date", String.valueOf(documentDate));
   }

    void replacePlaceDateText(XWPFDocument doc) throws Exception {
        LocalDate date = LocalDate.now();
        replaceText(doc,"Place, Date.",
                insolvencyProcessService.findInsolvencyProcessById(2L).getAdmin().getPlace() + ", " + date);
    }



    XWPFDocument replaceText(XWPFDocument doc, String originalText, String updatedText) {
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


       void replaceCompanyParagraphText(XWPFDocument doc, Long id) throws Exception{

       /* replaceText(doc,"companyName",
        ""+insolvencyProcessService.findInsolvencyProcessById(id).getCompanyName());
        replaceText(doc,"registrationNumber",
        ""+insolvencyProcessService.findInsolvencyProcessById(id).getRegistrationNumber());*/
        replaceText(doc,"courtName",
        ""+insolvencyProcessService.findInsolvencyProcessById(id).getCourtName());
        replaceText(doc,"courtDesitionDate",
        ""+insolvencyProcessService.findInsolvencyProcessById(id).getCourtDecisionDate());
        replaceText(doc,"courtCaseNumber",
        ""+insolvencyProcessService.findInsolvencyProcessById(id).getCourtCaseNumber()+" ");
        }

    void replaceIeceltaIeceltsParagraphText(XWPFDocument doc, Long id) throws Exception {
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

  /*  void replaceAuthorityDocNameText(XWPFDocument doc) throws Exception {
        replaceText(doc, "Document Name (Dokumenta nosaukums)",
                "Informācijas pieprasījums");
    } */

  int getParagraphPositionIfContainsText(XWPFDocument doc) {
        int paragraphPosition = -1;
        for (int i = 0; i < doc.getParagraphs().size(); i++) {
            if (doc.getParagraphs().get(i).getText().contains("Dokuments parakstīts elektroniski ar drošu elektronisko parakstu un satur laika zīmogu.")) {
                paragraphPosition = i;
                break;
            }
        }
        return paragraphPosition;
    }

  /*      void replaceAuthorityRecipientText1(XWPFDocument doc) throws Exception {

           replaceText(doc, "Nosaukums (recipientName)",
                   "Uzņēmumu reģistrs, ");
           replaceText(doc, "Reģistrācijas Nr.(Registration No)",
                   "Reģistrācijas numurs 90000270634,");
           replaceText(doc, "Adrese/ E-pasts/ E-Adrese:",
                   "E-pasts: info@ur.gov.lv");
       }
       */
  /*  void replaceAuthorityMainText1(XWPFDocument doc) throws Exception {
        String URtextContent = """
                Lūdzu sniegt :;
                1) aktuālo informāciju par Sabiedrības dalībniekiem un valdi (Standartizēta izziņa no visiem Uzņēmumu reģistra reģistriem).;
                2) aktuālo un vēsturisko informāciju par Sabiedrības, kura satur atbildes uz sekojošiem jautājumiem:;
                2.1. Kas ir/ir bijuši Sabiedrības valdes locekļi?;
                2.2. Kas ir/ir bijuši Sabiedrības dalībnieki?;
                2.3. Vai Sabiedrībai ir/ir bijuši reģistrēti prokūristi un ja ir tad kādi?;
                2.4. Vai Sabiedrībai ir/ir bijušas piederējušas vai pieder kapitāla daļas citā juridiskā personā un ja ir tad kādā?;
                2.5. Vai Sabiedrībai ir/ir bijuši reģistrētas komercķīlas, komercķīlu akti un ja ir tad kādas?;
                2.6. Vai Sabiedrībai ir/ir bijuši reģistrēti nodrošinājuma līdzekļi? Vai Sabiedrībai ir/ir bijuši reģistrēti aizliegumi un ja ir tad kādi?;
                2.7. Vai Sabiedrībai ir/ir bijušas reģistrētas filiāles un ja ir tad kāda?;
                """;

        List<String> URtextList = Arrays.asList(URtextContent.split(";"));
        int index = getParagraphPositionIfContainsText(doc);

       for (int i = URtextList.size() - 1; i >= 0; i--) {
            XmlCursor cursor = doc.getParagraphs().get(index - 1).getCTP().newCursor();
            XWPFParagraph new_par = doc.insertNewParagraph(cursor);
            XWPFRun run = new_par.createRun();
           styleReplacedText(new_par, run);
           run.setText(URtextList.get(i).trim());
         }

      replaceText(doc, "TEXT", "");

    } */

  /*  void replaceAuthorityMainText2(XWPFDocument doc) throws Exception{

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
    } */
  /*  void replaceAuthorityMainText3(XWPFDocument doc, Long id) throws Exception{

        replaceText(doc,"Nosaukums (recipientName)",
                "Lauksaimniecības datu centrs,");
        replaceText(doc,"Reģistrācijas Nr.(Registration No)",
                "Reģistrācijas numurs: 90001840100,");
        replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
                "E-pasts: ldc@ldc.gov.lv");
        String text = String.valueOf(replaceText(doc, "TEXT",
                "Lūdzu sniegt informāciju: par lauksaimniecības un citiem dzīvniekiem, kas ir reģistrēti un ir " +
                        "bijuši reģistrēti, kā arī tie kuri atrodas " + insolvencyProcessService.findInsolvencyProcessById(id).getCompanyName() +
                        ", vienotais reģistrācijas numurs " + insolvencyProcessService.findInsolvencyProcessById(id).getRegistrationNumber() +
                        " īpašumā un/vai valdījumā"));
    } */
  /*  void replaceAuthorityMainText4(XWPFDocument doc, Long id) throws Exception{

        replaceText(doc,"Nosaukums (recipientName)",
                "Valsts tehniskās uzraudzības aģentūra,");
        replaceText(doc,"Reģistrācijas Nr.(Registration No)",
                "Reģistrācijas numurs: 90001834941,");
        replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
                "E-pasts: vtua@vtua.gov.lv");
       replaceText(doc, "TEXT",
                "Lūdzu sniegt Jūsu rīcībā esošo informāciju kāda traktortehnika un tās piekabes šobrīd ir un / vai ir " +
                        "bijuši reģistrēti uz Sabiedrības vārda un kādi liegumi šobrīd ir vai ir bijuši reģistrēti.");

    } */

  /*  void replaceAuthorityMainText5(XWPFDocument doc, Long id) throws Exception{

        replaceText(doc,"Nosaukums (recipientName)",
                "Valsts Zemes dienests, ");
        replaceText(doc,"Reģistrācijas Nr.(Registration No)",
                "Reģistrācijas numurs: 90000030432, ");
        replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
                "E-pasts: kac.riga@vzd.gov.lv");
        replaceText(doc,"TEXT",
                "Lūdzu sniegt sekojošu aktuālo un vēsturisko informāciju par " + insolvencyProcessService.findInsolvencyProcessById(id).getCompanyName() +
                       ", reģistrācijas numurs: " + insolvencyProcessService.findInsolvencyProcessById(id).getRegistrationNumber()+ "īpašumā esošajiem " +
                        " un bijušajiem reģistrētajiem nekustamajiem īpašumiem, kā arī par reģistrētajiem īpašuma apgrūtinājumiem.");
    } */
/*
    void replaceAuthorityRecipientText6(XWPFDocument doc, Long id) throws Exception {
        replaceText(doc, "Nosaukums (recipientName)",
                "Zvērinātam tiesu izpildītājam _______________, ");
        replaceText(doc, "Reģistrācijas Nr.(Registration No)",
                "Reģistrācijas numurs: ___________________, ");
        replaceText(doc, "Adrese/ E-pasts/ E-Adrese:",
                "E-pasts: _____________________");
    } */
/*
       void replaceAuthorityMainText6(XWPFDocument doc, Long id) throws Exception {
       String ZTItextContent = """  
            Saskaņā ar Maksātnespējas likuma 65.pantu Administratora pienākumi pēc juridiskās personas maksātnespējas procesa
            pasludināšanas – pirmās daļas 12. punktu - pēc juridiskās personas maksātnespējas procesa pasludināšanas administrators
            iesniedz tiesu izpildītājam pieteikumu par izpildu lietvedības izbeigšanu lietās par piespriesto, bet no parādnieka
            nepiedzīto summu piedziņu un lietās par saistību izpildīšanu tiesas ceļā.;
            Maksātnespējas likuma 63. panta Juridiskās personas maksātnespējas procesa pasludināšanas sekas otrā daļa nosaka, ka, 
            ja sprieduma izpildes lietvedība uzsākta pirms juridiskās personas maksātnespējas procesa pasludināšanas, tā ir 
            izbeidzama Civilprocesa likumā noteiktajā kārtībā. Pēc juridiskās personas maksātnespējas procesa pasludināšanas 
            kreditori piesaka prasījumus administratoram šajā likumā noteiktajā kārtībā.;
            Saskaņā ar Civilprocesa likuma 563.panta Izpildu lietvedības izbeigšana otro daļu (2) Izpildu lietvedība par 
            piespriesto naudas summu piedziņu no juridiskajām personām, personālsabiedrībām, individuālajiem komersantiem, 
            ārvalstī reģistrētām personām, kas veic pastāvīgu saimniecisko darbību Latvijā, un lauksaimniecības produktu 
            ražotājiem izbeidzama pēc administratora pieteikuma, ja parādniekam likumā noteiktajā kārtībā pasludināta 
            maksātnespēja. Šajā gadījumā tiesu izpildītājs pabeidz uzsākto mantas pārdošanu, ja tā jau ir izsludināta 
            vai manta nodota tirdzniecības uzņēmumam pārdošanai, ja vien administrators nav pieprasījis atcelt izsludinātās 
            izsoles, lai nodrošinātu mantas pārdošanu lietu kopības sastāvā. No pārdošanā saņemtās naudas tiesu izpildītājs 
            ietur sprieduma izpildes izdevumus, bet pārējo naudu nodod administratoram kreditoru prasījumu segšanai atbilstoši 
            Maksātnespējas likumā noteiktajai kārtībai, ievērojot nodrošinātā kreditora tiesības. Tiesu izpildītājs paziņo 
            mantas glabātājam par pienākumu nodot administratoram mantu, kuras pārdošana nav uzsākta.;
                    
            Ņemot vērā iepriekš minēto, lūdzu:;
            1.\tIzbeigt visas uzsāktās izpildu lietvedības pret companyName, vienotais reģistrācijas numurs:   
            registrationNumber.;
            2.\tAtcelt visus uzliktos liegumus, atzīmes, kā arī cita veida aizliegumus, kas uzlikti companyName, vienotais reģistrācijas numurs:   
            registrationNumber, mantai un norēķinu kontiem.;
            3.\tAtcelt pieņemtos piespiedu izpildes līdzekļus.;
            4.\tSniegt informāciju vai izpildu lietu ietvaros ir saņemti naudas līdzekļi, cik, kādā veidā un kādā apmērā;
            5.\tSniegt informāciju kādā apmērā un kad segts piedzinēja prasījums?;
            6.\tAtsūtīt sprieduma un/vai izpildu raksta kopiju, uz kura pamata uzsākta izpildu lietvedība pret companyName, vienotais reģistrācijas numurs:   
            registrationNumber.;
            7.\tNorādīt vai šobrīd ir piedziņas procesā saņemtie Piedzinējam neizmaksāti naudas līdzekļi? Ja ir, tad tos 
            lūdzu neizmaksāt Piedzinējam, bet sniegt informāciju par summas apmēru ar mērķi atgūt tos un veltīt izmaksai maksātnespējas procesā kreditoriem Maksātnespējas procesa noteiktajā kārtībā.
               """;

       List<String> ZTItextList = Arrays.asList(ZTItextContent.split(";"));
            int index = getParagraphPositionIfContainsText(doc);


           for (int i = ZTItextList.size() - 1; i >= 0; i--) {
            XmlCursor cursor = doc.getParagraphs().get(index - 1).getCTP().newCursor();

            XWPFParagraph new_par = doc.insertNewParagraph(cursor);
            XWPFRun run = new_par.createRun();
            styleReplacedText(new_par, run);
            run.setText(ZTItextList.get(i).trim());
            }

        replaceText(doc, "TEXT", "");

    }
*/
   public void styleReplacedText(XWPFParagraph new_par, XWPFRun run) {
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        new_par.setAlignment(ParagraphAlignment.BOTH);
       new_par.setFirstLineIndent(400);
       }

    public void styleCell(XWPFTableCell cell, String s) {
        XWPFRun run = cell.addParagraph().createRun();

        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setText(s.replace('.', ','));
        cell.removeParagraph(0);
    }

    public void styleCell(XWPFTableCell cell, double s) {
        XWPFRun run = cell.addParagraph().createRun();

        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setText(String.format("%1.2f", s));
        cell.removeParagraph(0);
    }

    public void styleCellBold(XWPFTableCell cell, String s) {
        XWPFRun run = cell.addParagraph().createRun();

        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setText(s.replace('.', ','));
        run.setBold(true);
        cell.removeParagraph(0);
    }

    public void styleCellMinimized(XWPFTableCell cell, String s) {
        XWPFRun run = cell.addParagraph().createRun();

        run.setFontFamily("Times New Roman");
        run.setFontSize(9);
        run.setText(s);
        cell.removeParagraph(0);
    }

    public void styleCellMinimized(XWPFTableCell cell, double s) {
        XWPFRun run = cell.addParagraph().createRun();

        run.setFontFamily("Times New Roman");
        run.setFontSize(9);
        run.setText(String.format("%1.2f", s));
        cell.removeParagraph(0);
    }

    public double doubleFromString(String string) {
        return Double.parseDouble(string.replace(',', '.'));
    }

    public String getStyledString(double doubleValue) {
        return String.format("%1.2f", doubleValue);
    }

    public String getStyledString(String stringValue) {
        return stringValue.replace('.', ',');
    }

    public void createStyledTableHeader(XWPFTable createdTable, List<String> list) {
        styleCellBold(createdTable.getRow(0).getCell(0), list.get(0));
        for (int i = 1; i < list.size(); i++) {
            XWPFTableCell cell = createdTable.getRow(0).createCell();
            styleCellBold(cell, list.get(i));
        }
    }

    public int getParagraphPositionIfContainsText(String s, XWPFDocument doc ) {
        int paragraphPosition = -1;
        for (int i = 0; i < doc.getParagraphs().size(); i++) {
            if (doc.getParagraphs().get(i).getText().contains(s)) {
                paragraphPosition = i;
                break;
            }
        }
        return paragraphPosition;
    }

    public XWPFParagraph insertNewParagraph(int textPosition, XWPFDocument doc) {
        XmlCursor cursor = doc.getParagraphs().get(textPosition).getCTP().newCursor();
        return doc.insertNewParagraph(cursor);
    }

}
