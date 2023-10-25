package com.example.documentmanagement.documents;

import com.example.documentmanagement.insolvencyProcess.InsolvencyProcess;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


public class AuthorityBlank_2 extends AuthorityBlank {

    public AuthorityBlank_2(InsolvencyProcess insolvencyProcess) {
        super(insolvencyProcess);
    }

    public XWPFDocument createFilledDocument() {

        try {
            XWPFDocument doc = super.getAuthorityDocument();
            replaceAuthorityMainText2(doc);
            return doc;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public void replaceAuthorityMainText2(XWPFDocument doc) {

        replaceText(doc, "Nosaukums (recipientName)",
                "VAS “Latvijas Jūras administrācija” kuģu reģistrs, ");
        replaceText(doc, "Reģistrācijas Nr.(Registration No)",
                "Reģistrācijas numurs: 40003022705,");
        replaceText(doc, "Adrese/ E-pasts/ E-Adrese:",
                "E-pasts: lja@lja.lv");
        replaceText(doc, "TEXT",
                "Lūdzu sniegt Jūsu rīcībā esošo informāciju par Sabiedrību, -kādi kuģi, " +
                        "tajā skaitā zvejas laivas, atpūtas kuģi - buru jahtas, motorjahtas un peldošās " +
                        "konstrukcijas (peldošie doki, peldošās darbnīcas, peldošās degvielas uzpildes stacijas, " +
                        "debarkaderi, kravas pontoni), šobrīd ir un vai ir bijuši reģistrēti Sabiedrībai, no kura " +
                        "līdz kuram brīdim reģistrēti, un kādi liegumi - hipotēkas un apgrūtinājumi šobrīd ir vai ir bijuši reģistrēti.");
    }

}
