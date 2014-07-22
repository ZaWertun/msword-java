package ru.otlsoft.msword;

import org.json.JSONArray;
import org.json.JSONException;

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
        final JSONArray array = new JSONArray(json);
        final List<Pattern> result = new ArrayList<Pattern>(array.length());

        for (int i = 0; i < array.length(); ++i) {
            JSONArray tmp = array.getJSONArray(i);
            result.add(new Pattern(tmp.getString(0), tmp.getString(1)));
        }

        return result;
    }
}
