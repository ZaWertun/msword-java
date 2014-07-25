package ru.otlsoft.msword;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Pattern {
    public final String search;
    public final String replace;

    public Pattern(String search, String replace) {
        this.search = "${" + search + "}";
        this.replace = replace;
    }

    public static List<Pattern> parseJson(final InputStream inputStream)
            throws JSONException, IOException {
        final StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer);

        final JSONObject object = new JSONObject(writer.toString());
        final List<Pattern> result = new ArrayList<>(object.keySet().size());

        for (Object k : object.keySet()) {
            String key = (String) k;
            result.add(new Pattern(
                    key,
                    object.isNull(key) ? "" : object.get(key).toString()));
        }

        return result;
    }
}
