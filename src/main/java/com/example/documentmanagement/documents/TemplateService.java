package com.example.documentmanagement.documents;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import java.util.List;


public class TemplateService {

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

    public int getParagraphPositionIfContainsText(String s, XWPFDocument doc) {
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
