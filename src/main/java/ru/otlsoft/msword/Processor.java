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
            String replace = p.replace.replaceAll("\n", "\r");
            boolean keepLooking = true;
            int offset = -p.search.length();

            while (keepLooking) {
                String text = range.text();
                offset = text.indexOf(p.search, offset + replace.length());
                if (offset >= 0) {
                    range.replaceText(p.search, replace, offset);
                } else {
                    keepLooking = false;
                }
            }
        }
    }

    private static void setText(XWPFRun run, String value, Integer pos) {
        if (value.indexOf('\n') != -1) {
            String tmp = "";
            int i;

            for (i = 0; i < value.length(); ++i) {
                char ch = value.charAt(i);

                if (ch == '\n') {
                    if (tmp.length() != 0) {
                        run.setText(tmp);
                        tmp = "";
                    }
                    run.addBreak();
                } else {
                    tmp = tmp + ch;
                }
            }

            if (tmp.length() != 0) {
                run.setText(tmp);
            }
        } else if (pos != null) {
            run.setText(value, pos);
        } else {
            run.setText(value);
        }
    }

    private static void replaceParagraphText(final XWPFParagraph paragraph, final Pattern pattern) {
        List<XWPFRun> runs = paragraph.getRuns();
        TextSegement segement = paragraph.searchText(pattern.search, new PositionInParagraph());
        if (segement != null) {
            int begin = segement.getBeginRun();
            int end = segement.getEndRun();

            if (pattern.replace.isEmpty()) {
                runs.get(begin).setText("", 0);
            } else if (runs.size() == 1) {
                setText(runs.get(begin), pattern.replace, 0);
            } else {
                setText(runs.get(begin), pattern.replace, null);
            }

            if (end > begin) {
                for (int i = begin; i <= end; ++i) {
                    runs.get(i).setText("", 0);
                }
            }
        }
    }

    private static void replace(final XWPFDocument document, final List<Pattern> patterns) {
        for (Pattern p : patterns) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                replaceParagraphText(paragraph, p);
            }

            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {
                            replaceParagraphText(paragraph, p);
                        }
                    }
                }
            }
        }
    }

    public static void process(final DocumentType type, final File template,
                               final List<Pattern> patterns)
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
                }
                break;

                case DOCX: {
                    XWPFDocument document = new XWPFDocument(OPCPackage.open(input));

                    replace(document, patterns);
                    document.write(output);
                }
                break;

                default:
                    throw new IllegalArgumentException("Unknown file type passed");
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
