package com.example.documentmanagement.documents;

import com.example.documentmanagement.insolvencyprocess.InsolvencyProcess;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


public class AuthorityBlank_3 extends AuthorityBlank {
    private InsolvencyProcess insolvencyProcess;

    public AuthorityBlank_3(InsolvencyProcess insolvencyProcess) {
        super(insolvencyProcess);
        this.insolvencyProcess = insolvencyProcess;

    }

    public XWPFDocument createFilledDocument()  {

        try {
            XWPFDocument doc = super.getAuthorityDocument();
            replaceAuthorityMainText3(doc, this.insolvencyProcess.getId());
            return doc;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public void replaceAuthorityMainText3(XWPFDocument doc, Long id) {

        replaceText(doc,"Nosaukums (recipientName)",
                "Lauksaimniecības datu centrs,");
        replaceText(doc,"Reģistrācijas Nr.(Registration No)",
                "Reģistrācijas numurs: 90001840100,");
        replaceText(doc,"Adrese/ E-pasts/ E-Adrese:",
                "E-pasts: ldc@ldc.gov.lv");
        String text = String.valueOf(replaceText(doc, "TEXT",
                "Lūdzu sniegt informāciju: par lauksaimniecības un citiem dzīvniekiem, kas ir reģistrēti un ir " +
                        "bijuši reģistrēti, kā arī tie kuri atrodas " + insolvencyProcess.getCompanyName() +
                        ", vienotais reģistrācijas numurs " + insolvencyProcess.getRegistrationNumber() +
                        " īpašumā un/vai valdījumā"));
    }

}
