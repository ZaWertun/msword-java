package ru.otlsoft.msword;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    private static void error(int code, final String message) {
        System.err.println(message);
        System.exit(code);
    }

    private static File checkFile(final String path, boolean checkWrite) {
        final File result = new File(path);

        if (checkWrite) {
            try {
                if (!result.exists()) {
                    result.createNewFile();
                }

                if (!result.canWrite()) {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }
        } else if (!(result.isFile() && result.canRead())) {
            return null;
        }

        return result;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            error(1, "Not enough arguments\n\n" +
                    "\tUsage: java -jar msword-java.jar <template_file_path> <json_array_with_patterns>\n" +
                    "\tPattern syntax: [[\"what to find 1\", \"replace with 1\"], [\"what to find 2\", \"replace with 1\"], ...]");
        }

        final File template = checkFile(args[0], false);
        if (template == null) {
            error(2, "Template file doesn't exists or doesn't have read permissions");
        }

        final DocumentType templateType = DocumentType.get(template);
        if (templateType == DocumentType.UNKNOWN) {
            error(4, "Template file has unsupported extensions");
        }

        List<Pattern> patterns = null;
        try {
            patterns = Pattern.parseJson(args[1]);
        } catch (JSONException _) {
            error(6, "Third argument must be valid JSON array with format: " +
                    "[[\"pattern_1\", \"replace_str_1\"], [\"pattern_2\", \"replace_str_2\"], ...]");
        }
        if (patterns.size() == 0) {
            error(7, "Pattern list is empty");
        }

        try {
            Processor.process(templateType, template, patterns);
        } catch (IOException | InvalidFormatException e) {
            error(8, e.getMessage());
        }
    }
}
