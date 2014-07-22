package ru.otlsoft.msword;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.List;


public class Processor {
    private static void replace(HWPFDocument document, List<Pattern> patterns) {
        Range range = document.getRange();

        for (Pattern p : patterns) {
            boolean keepLooking = true;
            int offset = -p.search.length();

            while (keepLooking) {
                String text = range.text();
                offset = text.indexOf(p.search, offset + p.replace.length());
                if (offset >= 0) {
                    range.replaceText(p.search, p.replace, offset);
                } else {
                    keepLooking = false;
                }
            }
        }
    }

    private static void replace(XWPFDocument document, List<Pattern> patterns) {
        for (Pattern p : patterns) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                for (XWPFRun r : paragraph.getRuns()) {
                    String text = r.getText(0);

                    if (text.contains(p.search)) {
                        text = text.replace(p.search, p.replace);
                        r.setText(text, 0);
                    }
                }
            }

            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {
                            for (XWPFRun r : paragraph.getRuns()) {
                                String text = r.getText(0);

                                if (text.contains(p.search)) {
                                    text = text.replace(p.search, p.replace);
                                    r.setText(text, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void process(final DocumentType type, final File template, final List<Pattern> patterns)
            throws IOException, InvalidFormatException {
        InputStream input = null;
        OutputStream output = null;

        try {
            input = new FileInputStream(template);
            output = System.out;

            switch (type) {
                case DOC: {
                    POIFSFileSystem fs = new POIFSFileSystem(input);
                    HWPFDocument document = new HWPFDocument(fs);

                    replace(document, patterns);
                    document.write(output);
                } break;

                case DOCX: {
                    XWPFDocument document = new XWPFDocument(OPCPackage.open(input));

                    replace(document, patterns);
                    document.write(output);
                } break;
            }
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
