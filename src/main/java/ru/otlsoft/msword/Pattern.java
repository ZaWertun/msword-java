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

    public Pattern(Pair<String, String> keyValuePair) {
        this.search = "${" + keyValuePair.key + "}";
        this.replace = keyValuePair.value;
    }

    private static void parsePatterns(String parentKeyPath, JSONObject object, Callback<Pair<String, String>> callback) {
        for (Object k : object.keySet()) {
            String key = (String) k;
            JSONObject subObject = object.optJSONObject(key);
            String keyPath = (parentKeyPath == null) ? key : (parentKeyPath + '.' + key);
            if (subObject != null) {
                parsePatterns(keyPath, subObject, callback);
            } else {
                String value = object.isNull(key) ? "" : object.get(key).toString();
                callback.run(new Pair<>(keyPath, value));
            }
        }
    }

    public static List<Pattern> parseJson(final InputStream inputStream)
            throws JSONException, IOException {
        final StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer);

        final JSONObject object = new JSONObject(writer.toString());
        final List<Pattern> result = new ArrayList<>(object.keySet().size());
        parsePatterns(null, object, new Callback<Pair<String, String>>() {
            @Override
            public void run(Pair<String, String> keyValuePair) {
                result.add(new Pattern(keyValuePair));
            }
        });

        return result;
    }
}
