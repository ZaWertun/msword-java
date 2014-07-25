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
                if (!result.exists() && !result.createNewFile()) {
                    return null;
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
        if (args.length < 1) {
            error(1, "Not enough arguments\n\n" +
                    "\tUsage: java -jar msword-java.jar <template_file_path>\n" +
                    "\tJSON object with patterns must be passed to standard input");
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
            patterns = Pattern.parseJson(System.in);
        } catch (IOException e) {
            error(6, "Error when reading from standard input: " + e.getMessage());
        } catch (JSONException e) {
            error(7, "You must pass valid JSON object to standard input");
        }
        if (patterns.size() == 0) {
            error(8, "Pattern list is empty");
        }

        try {
            Processor.process(templateType, template, patterns);
        } catch (IOException | InvalidFormatException e) {
            error(9, e.getMessage());
        }
    }
}
