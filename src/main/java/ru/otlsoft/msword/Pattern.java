package ru.otlsoft.msword;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Pattern {
    public final String search;
    public final String replace;

    public Pattern(String search, String replace) {
        this.search = "${" + search + "}";
        this.replace = replace;
    }

    public static List<Pattern> parseJson(final String json)
            throws JSONException {
        final JSONObject object = new JSONObject(json);
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
