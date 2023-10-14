package com.example.documentmanagement;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Data
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

    void replaceHeaderText(XWPFDocument doc) throws Exception {
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
    public ByteArrayOutputStream exportCostsOfInsolvencyProceedings(Long processId) {

        try {
            CostsOfInsolvencyProceedingsDoc document = new CostsOfInsolvencyProceedingsDoc(insolvencyProcessService.findInsolvencyProcessById(processId));
            XWPFDocument doc  = document.createFilledDocument();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.write(out);
            out.close();
            doc.close();
            return out;
        } catch (Exception e) {
            throw new RuntimeException(e);
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

            replaceHeaderText(doc);
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


   public ByteArrayOutputStream exportAuthorityBlank(Long id, int number) throws IOException {
       InputStream inputStream = getClass().getResourceAsStream("/companyBlank.docx");
       XWPFDocument doc = new XWPFDocument(inputStream);

       try {
           replaceHeaderText(doc);
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
                "" + insolvencyProcessService.findInsolvencyProcessById(id).getCourtDecisionDate() + " ");
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
              "");
      replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
              "e-pasts: info@ur.gov.lv");
      replaceText(doc,"TEXT",
                    "Lūdzu sniegt : 1) aktuālo   informāciju   par   Sabiedrības  dalībniekiem   un   valdi" +
                            "(Standartizēta izziņa no visiem Uzņēmumu reģistra reģistriem)."+"\n" +
                            "2) aktuālo   un   vēsturisko   informāciju   par   Sabiedrības,\n" +
                            "kura satur atbildes uz sekojošiem jautājumiem:\n" +
                            "2.1. Kas ir/ ir bijuši Sabiedrības valdes locekļi?\n" +
                            "2.2. Kas ir/ ir bijuši Sabiedrības dalībnieki?\n" +
                            "2.3. Vai Sabiedrībai ir/ ir bijuši reģistrēti prokūristi un ja ir tad kādi?\n" +
                            "2.4. Vai Sabiedrībai ir/ ir bijušas piederējušas vai pieder kapitāla daļas\n" +
                            "citā juridiskā personā un ja ir tad kādā?\n" +
                            "2.5. Vai Sabiedrībai ir/ ir bijuši reģistrētas komercķīlas, komercķīlu akti\n" +
                            "un ja ir tad kādas?\n" +
                            "2.6. Vai   Sabiedrībai   ir/   ir   bijuši   reģistrēti   nodrošinājuma   līdzekļi?   Vai\n" +
                            "Sabiedrībai ir/ ir bijuši reģistrēti aizliegumi un ja ir tad kādi?\n" +
                            "2.7. Vai Sabiedrībai ir/ ir bijušas reģistrētas filiāles un ja ir tad kāda?\n" );
     }

    private void replaceAuthorityMainText2 (XWPFDocument doc) throws Exception{

        replaceText(doc,"Nosaukums (recipientName)",
                "VAS “Latvijas Jūras administrācija” kuģu reģistrs ");
        replaceText(doc,"Reģistrācijas Nr.(Registration No)",
                "");
        replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
                "e-pasts: lja@lja.lv");
        replaceText(doc,"TEXT",
                "lūdzu sniegt Jūsu rīcībā esošo informāciju par Sabiedrību, -kādi kuģi, " +
                        "tajā skaitā zvejas laivas, atpūtas kuģi - buru jahtas, motorjahtas un peldošās " +
                        "konstrukcijas (peldošie doki, peldošās darbnīcas, peldošās degvielas uzpildes stacijas, " +
                        "debarkaderi, kravas pontoni), šobrīd ir un vai ir bijuši reģistrēti Sabiedrībai, no kura " +
                        "līdz kuram brīdim reģistrēti, un kādi liegumi - hipotēkas un apgrūtinājumi šobrīd ir vai ir bijuši reģistrēti.");
    }

    private void replaceAuthorityMainText3 (XWPFDocument doc, Long id) throws Exception{

        replaceText(doc,"Nosaukums (recipientName)",
                "Lauksaimniecības datu centrs");
        replaceText(doc,"Reģistrācijas Nr.(Registration No)",
                "");
        replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
                "e-pasts: ldc@ldc.gov.lv");
        String text = String.valueOf(replaceText(doc, "TEXT",
                "lūdzu sniegt informāciju par lauksaimniecības un citiem dzīvniekiem, kas ir reģistrēti un ir " +
                        "bijuši reģistrēti, kā arī tie kuri atrodas " + insolvencyProcessService.findInsolvencyProcessById(id).getCompanyName() +
                        ", vienotais reģistrācijas numurs " + insolvencyProcessService.findInsolvencyProcessById(id).getRegistrationNumber() +
                        " īpašumā un/vai valdījumā"));
    }
    private void replaceAuthorityMainText4 (XWPFDocument doc, Long id) throws Exception{

        replaceText(doc,"Nosaukums (recipientName)",
                "Valsts tehniskās uzraudzības aģentūra");
        replaceText(doc,"Reģistrācijas Nr.(Registration No)",
                "");
        replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
                "vtua@vtua.gov.lv");
        String text = String.valueOf(replaceText(doc, "TEXT",
                "lūdzu sniegt Jūsu rīcībā esošo informāciju kāda traktortehnika un tās piekabes šobrīd ir un / vai ir " +
                        "bijuši reģistrēti uz Sabiedrības vārda un kādi liegumi šobrīd ir vai ir bijuši reģistrēti."));
    }


    private void replaceAuthorityMainText5 (XWPFDocument doc, Long id) throws Exception{
        replaceText(doc,"Nosaukums (recipientName)",
                "Valsts Zemes dienests ");
        replaceText(doc,"Reģistrācijas Nr.(Registration No)",
                "");
        replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
                "kac.riga@vzd.gov.lv");
        replaceText(doc,"TEXT",
                "lūdzu sniegt sekojošu aktuālo un vēsturisko informāciju par " + insolvencyProcessService.findInsolvencyProcessById(id).getCompanyName() +
                        insolvencyProcessService.findInsolvencyProcessById(id).getRegistrationNumber()+ "īpašumā esošajiem " +
                        "un bijušajiem reģistrētajiem nekustamajiem īpašumiem, kā arī par reģistrētajiem īpašuma apgrūtinājumiem.");
    }

    private void replaceAuthorityMainText6 (XWPFDocument doc, Long id) throws Exception{
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

}
