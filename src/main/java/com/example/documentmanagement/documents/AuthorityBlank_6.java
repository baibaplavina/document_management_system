package com.example.documentmanagement.documents;

import com.example.documentmanagement.insolvencyprocess.InsolvencyProcess;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlCursor;
import java.util.Arrays;
import java.util.List;


public class AuthorityBlank_6 extends AuthorityBlank {
    private InsolvencyProcess insolvencyProcess;

    public AuthorityBlank_6(InsolvencyProcess insolvencyProcess) {
        super(insolvencyProcess);
        this.insolvencyProcess = insolvencyProcess;

    }

    public XWPFDocument createFilledDocument() {

        try {
            XWPFDocument doc = super.getAuthorityDocument();
            replaceAuthorityRecipientText6(doc, insolvencyProcess.getId());
            replaceAuthorityMainText6(doc, insolvencyProcess.getId());
            return doc;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void replaceAuthorityRecipientText6(XWPFDocument doc, Long id) {
        replaceText(doc, "Nosaukums (recipientName)",
                "Zvērinātam tiesu izpildītājam _______________, ");
        replaceText(doc, "Reģistrācijas Nr.(Registration No)",
                "Reģistrācijas numurs: ___________________, ");
        replaceText(doc, "Adrese/ E-pasts/ E-Adrese:",
                "E-pasts: _____________________");
    }

   public void replaceAuthorityMainText6(XWPFDocument doc, Long id) {
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
}
