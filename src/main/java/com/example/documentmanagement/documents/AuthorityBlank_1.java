package com.example.documentmanagement.documents;

import com.example.documentmanagement.insolvencyProcess.InsolvencyProcess;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlCursor;
import java.util.Arrays;
import java.util.List;


public class AuthorityBlank_1 extends AuthorityBlank {

    public AuthorityBlank_1(InsolvencyProcess insolvencyProcess) {
        super(insolvencyProcess);

    }

    public XWPFDocument createFilledDocument() {

        try {
            XWPFDocument doc = super.getAuthorityDocument();
            replaceAuthorityRecipientText1(doc);
            replaceAuthorityMainText1(doc);
            return doc;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void replaceAuthorityRecipientText1(XWPFDocument doc) {

        replaceText(doc, "Nosaukums (recipientName)",
                "Uzņēmumu reģistrs, ");
        replaceText(doc, "Reģistrācijas Nr.(Registration No)",
                "Reģistrācijas numurs 90000270634,");
        replaceText(doc, "Adrese/ E-pasts/ E-Adrese:",
                "E-pasts: info@ur.gov.lv");
    }

    public void replaceAuthorityMainText1(XWPFDocument doc) {
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
    }

}
