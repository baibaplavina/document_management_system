package com.example.documentmanagement.documents;

import com.example.documentmanagement.insolvencyProcess.InsolvencyProcess;
import org.apache.poi.xwpf.usermodel.XWPFDocument;



public class AuthorityBlank_4 extends AuthorityBlank {
    private InsolvencyProcess insolvencyProcess;

    public AuthorityBlank_4(InsolvencyProcess insolvencyProcess) {
        super(insolvencyProcess);
        this.insolvencyProcess = insolvencyProcess;

    }

    public XWPFDocument createFilledDocument() {

        try {
            XWPFDocument doc = super.getAuthorityDocument();
            replaceAuthorityMainText4(doc, insolvencyProcess.getId());
            return doc;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


      public void replaceAuthorityMainText4(XWPFDocument doc, Long id) {

          replaceText(doc, "Nosaukums (recipientName)",
                  "Valsts tehniskās uzraudzības aģentūra,");
          replaceText(doc, "Reģistrācijas Nr.(Registration No)",
                  "Reģistrācijas numurs: 90001834941,");
          replaceText(doc, "Adrese/ E-pasts/ E-Adrese:",
                  "E-pasts: vtua@vtua.gov.lv");
          replaceText(doc, "TEXT",
                  "Lūdzu sniegt Jūsu rīcībā esošo informāciju kāda traktortehnika un tās piekabes šobrīd ir un / vai ir " +
                          "bijuši reģistrēti uz Sabiedrības vārda un kādi liegumi šobrīd ir vai ir bijuši reģistrēti.");
      }

}
