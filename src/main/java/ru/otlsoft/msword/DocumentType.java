package ru.otlsoft.msword;

import java.io.File;

public enum DocumentType {
    DOC,
    DOCX,
    UNKNOWN;

    public static DocumentType get(File file) {
        final String fileName = file.getName();
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i >= 0) {
            extension = fileName.substring(i + 1);
        }
        extension = extension.toLowerCase();

        switch (extension) {
            case "doc":
                return DOC;

            case "docx":
                return DOCX;

            default:
                return UNKNOWN;
        }
    }
}
