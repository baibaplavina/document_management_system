package com.example.documentmanagement.documents;

import com.example.documentmanagement.insolvencyprocess.InsolvencyProcess;
import org.apache.poi.xwpf.usermodel.XWPFDocument;



public class AuthorityBlank_5 extends AuthorityBlank {
    private InsolvencyProcess insolvencyProcess;

    public AuthorityBlank_5(InsolvencyProcess insolvencyProcess) {
        super(insolvencyProcess);
        this.insolvencyProcess = insolvencyProcess;
    }

    public XWPFDocument createFilledDocument()  {

        try {
            XWPFDocument doc = super.getAuthorityDocument();
            replaceAuthorityMainText5(doc, insolvencyProcess.getId());
            return doc;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

      public void replaceAuthorityMainText5(XWPFDocument doc, Long id) {

        replaceText(doc,"Nosaukums (recipientName)",
                "Valsts Zemes dienests, ");
        replaceText(doc,"Reģistrācijas Nr.(Registration No)",
                "Reģistrācijas numurs: 90000030432, ");
        replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
                "E-pasts: kac.riga@vzd.gov.lv");
        replaceText(doc,"TEXT",
                "Lūdzu sniegt sekojošu aktuālo un vēsturisko informāciju par " + insolvencyProcess.getCompanyName() +
                       ", reģistrācijas numurs: " + insolvencyProcess.getRegistrationNumber()+ "īpašumā esošajiem " +
                        " un bijušajiem reģistrētajiem nekustamajiem īpašumiem, kā arī par reģistrētajiem īpašuma apgrūtinājumiem.");
    }

}
