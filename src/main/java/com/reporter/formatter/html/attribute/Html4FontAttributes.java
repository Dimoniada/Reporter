package com.reporter.formatter.html.attribute;

import com.reporter.formatter.html.styles.CssStyle;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Html4FontAttributes {
    public static final String FONTSIZE_HTML4 = "size=";
    public static final String FONTCOLOR_HTML4 = "color=";
    public static final String FONTFACE_HTML4 = "face=";

    protected Map<String, Object> fontAttributes = new HashMap<>();

    private final Map<String, Function<Object, String>> fontAttrHtml4Mapper = new HashMap<>() {{
        put(FONTSIZE_HTML4, CssStyle::produceDefaultStringAttribute);
        put(FONTCOLOR_HTML4, CssStyle::produceDefaultStringAttribute);
        put(FONTFACE_HTML4, CssStyle::produceDefaultStringAttribute);
    }};

    public int getFontSizeHtml4() {
        return (int) fontAttributes.getOrDefault(FONTSIZE_HTML4, "10");
    }

    public String getFontColorHtml4() {
        return (String) fontAttributes.getOrDefault(FONTCOLOR_HTML4, "black");
    }

    public String getFontFaceHtml4() {
        return (String) fontAttributes.getOrDefault(FONTFACE_HTML4, "serif");
    }

    public Html4FontAttributes setFontSizeHtml4(int sizeHtml4) {
        fontAttributes.put(FONTSIZE_HTML4, sizeHtml4);
        return this;
    }

    public Html4FontAttributes setFontColorHtml4(String colorHtml4) {
        fontAttributes.put(FONTCOLOR_HTML4, colorHtml4);
        return this;
    }

    public Html4FontAttributes setFontFaceHtml4(String faceHtml4) {
        fontAttributes.put(FONTFACE_HTML4, faceHtml4);
        return this;
    }

    public String toFontHtml4String() {
        return
            fontAttributes
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .filter(s -> fontAttrHtml4Mapper.containsKey(s.getKey()))
                .map(e -> CssStyle.styleHtml4ItemMapping(e, fontAttrHtml4Mapper))
                .collect(Collectors.joining(" "));
    }
}
