package com.example.documentmanagement;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Template_first {
    private String getAllText() {

        String firstPart = "this is first part";
        String secondPart = "this is second part";
        String thirdPart = "this is third part";

        return firstPart + " " + secondPart + " " + thirdPart;
    }

    public ByteArrayOutputStream exportWordDoc() throws IOException {
        XWPFDocument doc = new XWPFDocument();
        XWPFRun run = doc.createParagraph().createRun();
        run.setText(getAllText());
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        out.close();
        doc.close();

        return out;
    }

}
