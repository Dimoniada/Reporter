package com.reporter.formatter.html.styles;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CssStyle {
    public static final String FONT_SIZE = "font-size";
    public static final String FONT_WEIGHT = "font-weight";
    public static final String FONT_STYLE = "font-style";
    public static final String FONT_COLOR = "color";
    public static final String FONT_FAMILY = "font-family";

    public static final String TEXT_ALIGN = "text-align";

    public static final String BORDER_COLLAPSE = "border-collapse";
    public static final String BORDER_TOP = "border-top";
    public static final String BORDER_LEFT = "border-left";
    public static final String BORDER_RIGHT = "border-right";
    public static final String BORDER_BOTTOM = "border-bottom";

    public static final String BACKGROUND_COLOR = "background-color";

    public static final String BORDER = "border";
    public static final String COLOR = "color";
    public static final String HEIGHT = "height";

    public static final String BORDER_HTML4 = "border=";
    public static final String CELLSPACING_HTML4 = "cellspacing=";
    public static final String BGCOLOR_HTML4 = "bgcolor=";
    public static final String ALIGN_HTML4 = "align=";

    protected Map<String, Object> attributes = new HashMap<>();

    protected Map<String, Function<Object, String>> attributeMapper;
    protected Map<String, Function<Object, String>> attributeHtml4Mapper;

    public CssStyle() {
        attributeMapper = new HashMap<>() {{
            put(FONT_SIZE, CssStyle::produceFontSizeAttribute);
            put(FONT_WEIGHT, CssStyle::produceFontWeightAttribute);
            put(FONT_STYLE, CssStyle::produceFontStyleAttribute);
            put(FONT_COLOR, CssStyle::produceFontColorAttribute);
            put(FONT_FAMILY, CssStyle::produceFontFamilyAttribute);
            put(TEXT_ALIGN, CssStyle::produceTextAlignAttribute);
            put(BORDER_TOP, CssStyle::produceBorderTopAttribute);
            put(BORDER_LEFT, CssStyle::produceBorderLeftAttribute);
            put(BORDER_RIGHT, CssStyle::produceBorderRightAttribute);
            put(BORDER_BOTTOM, CssStyle::produceBorderBottomAttribute);
            put(BACKGROUND_COLOR, CssStyle::produceBackgroundColorAttribute);
            put(BORDER_COLLAPSE, CssStyle::produceBorderCollapseAttribute);
            put(BORDER, CssStyle::produceBorderCollapseAttribute);
            put(HEIGHT, CssStyle::produceBorderCollapseAttribute);
        }};

        attributeHtml4Mapper = new HashMap<>() {{
            put(BORDER_HTML4, CssStyle::produceBorderHtml4Attribute);
            put(CELLSPACING_HTML4, CssStyle::produceCellspacingHtml4Attribute);
            put(BGCOLOR_HTML4, CssStyle::produceBgcolorHtml4Attribute);
            put(ALIGN_HTML4, CssStyle::produceAlignHtml4Attribute);
        }};
    }

    public int getFontSize() {
        return (Integer) attributes.getOrDefault(FONT_SIZE, 0);
    }

    public String getFontWeight() {
        return (String) attributes.getOrDefault(FONT_WEIGHT, "normal");
    }

    public String getFontStyle() {
        return (String) attributes.getOrDefault(FONT_STYLE, "normal");
    }

    public String getFontColor() {
        return (String) attributes.getOrDefault(FONT_COLOR, "#FFFFFF");
    }

    public String getFontFamily() {
        return (String) attributes.getOrDefault(FONT_FAMILY, "inherit");
    }

    public String getTextAlign() {
        return (String) attributes.getOrDefault(TEXT_ALIGN, "justify");
    }

    public String getBorderCollapse() {
        return (String) attributes.getOrDefault(BORDER_COLLAPSE, "collapse");
    }

    public String getBorderTop() {
        return (String) attributes.getOrDefault(BORDER_TOP, "inherit");
    }

    public String getBorderLeft() {
        return (String) attributes.getOrDefault(BORDER_LEFT, "inherit");
    }

    public String getBorderRight() {
        return (String) attributes.getOrDefault(BORDER_RIGHT, "inherit");
    }

    public String getBorderBottom() {
        return (String) attributes.getOrDefault(BORDER_BOTTOM, "inherit");
    }

    public String getBorder() {
        return (String) attributes.getOrDefault(BORDER, "none");
    }

    public String getColor() {
        return (String) attributes.getOrDefault(COLOR, "inherit");
    }

    public String getHeight() {
        return (String) attributes.getOrDefault(HEIGHT, "inherit");
    }

    public Integer getBorderHtml4() {
        return (Integer) attributes.getOrDefault(BORDER_HTML4, 0);
    }

    public Integer getCellspacingHtml4() {
        return (Integer) attributes.getOrDefault(CELLSPACING_HTML4, 0);
    }

    public String getBgcolorHtml4() {
        return (String) attributes.getOrDefault(BGCOLOR_HTML4, "#FFFFFF");
    }

    public String getAlignHtml4() {
        return (String) attributes.getOrDefault(ALIGN_HTML4, "center");
    }

    public String getBackgroundColor() {
        return (String) attributes.getOrDefault(BACKGROUND_COLOR, "#FFFFFF");
    }

    public CssStyle setFontSize(int size) {
        attributes.put(FONT_SIZE, size);
        return this;
    }

    public CssStyle setFontWeight(String fontWeight) {
        attributes.put(FONT_WEIGHT, fontWeight);
        return this;
    }

    public CssStyle setFontStyle(String fontStyle) {
        attributes.put(FONT_STYLE, fontStyle);
        return this;
    }

    public CssStyle setFontColor(String fontColor) {
        attributes.put(FONT_COLOR, fontColor);
        return this;
    }

    public CssStyle setFontFamily(String fontFamily) {
        attributes.put(FONT_FAMILY, fontFamily);
        return this;
    }

    public CssStyle setTextAlign(String textAlign) {
        attributes.put(TEXT_ALIGN, textAlign);
        return this;
    }

    public CssStyle setBorderCollapse(String borderCollapse) {
        attributes.put(BORDER_COLLAPSE, borderCollapse);
        return this;
    }

    public CssStyle setBorderTop(String borderTop) {
        attributes.put(BORDER_TOP, borderTop);
        return this;
    }

    public CssStyle setBorderLeft(String borderLeft) {
        attributes.put(BORDER_LEFT, borderLeft);
        return this;
    }

    public CssStyle setBorderRight(String borderRight) {
        attributes.put(BORDER_RIGHT, borderRight);
        return this;
    }

    public CssStyle setBorderBottom(String borderBottom) {
        attributes.put(BORDER_BOTTOM, borderBottom);
        return this;
    }

    public CssStyle setBackgroundColor(String backgroundColor) {
        attributes.put(BACKGROUND_COLOR, backgroundColor);
        return this;
    }

    public CssStyle setBorder(String border) {
        attributes.put(BORDER, border);
        return this;
    }

    public CssStyle setColor(String color) {
        attributes.put(COLOR, color);
        return this;
    }

    public CssStyle setHeight(String height) {
        attributes.put(HEIGHT, height);
        return this;
    }

    public CssStyle setBorderHtml4(Integer borderHtml4) {
        attributes.put(BORDER_HTML4, borderHtml4);
        return this;
    }

    public CssStyle setCellspacingHtml4(Integer cellspacingHtml4) {
        attributes.put(CELLSPACING_HTML4, cellspacingHtml4);
        return this;
    }

    public CssStyle setBgcolorHtml4(String bgcolorHtml4) {
        attributes.put(BGCOLOR_HTML4, bgcolorHtml4);
        return this;
    }

    public CssStyle setAlignHtml4(String alignHtml4) {
        if (alignHtml4 != null) {
            attributes.put(ALIGN_HTML4, alignHtml4);
        }
        return this;
    }

    public static String produceFontSizeAttribute(Object o) {
        return String.format("%dpt", o);
    }

    public static String produceFontWeightAttribute(Object o) {
        return produceDefaultStringAttribute(o);
    }

    public static String produceFontStyleAttribute(Object o) {
        return produceDefaultStringAttribute(o);
    }

    public static String produceFontColorAttribute(Object o) {
        return produceDefaultStringAttribute(o);
    }

    public static String produceFontFamilyAttribute(Object o) {
        if (!StringUtils.hasText((String) o)) {
            return "monospace";
        } else {
            return String.format("%s,monospace", o);
        }
    }

    public static String produceTextAlignAttribute(Object o) {
        return produceDefaultStringAttribute(o);
    }

    public static String produceBorderCollapseAttribute(Object o) {
        return produceDefaultStringAttribute(o);
    }

    public static String produceBorderTopAttribute(Object o) {
        return produceDefaultStringAttribute(o);
    }

    public static String produceBorderLeftAttribute(Object o) {
        return produceDefaultStringAttribute(o);
    }

    public static String produceBorderRightAttribute(Object o) {
        return produceDefaultStringAttribute(o);
    }

    public static String produceBorderBottomAttribute(Object o) {
        return produceDefaultStringAttribute(o);
    }

    public static String produceBackgroundColorAttribute(Object o) {
        return produceDefaultStringAttribute(o);
    }

    public static String produceBorderHtml4Attribute(Object o) {
        return String.format("%d", o);
    }

    public static String produceCellspacingHtml4Attribute(Object o) {
        return String.format("%d", o);
    }

    public static String produceBgcolorHtml4Attribute(Object o) {
        return produceDefaultStringAttribute(o);
    }

    public static String produceAlignHtml4Attribute(Object o) {
        return produceDefaultStringAttribute(o);
    }

    public static String produceDefaultStringAttribute(Object o) {
        return String.format("%s", o);
    }

    public String toCssStyleString() {
        return
            attributes
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .filter(s -> attributeMapper.containsKey(s.getKey()))
                .map(this::styleItemMapping)
                .collect(Collectors.joining(";"));
    }

    private String styleItemMapping(Map.Entry<String, Object> e) {
        final String key = e.getKey();

        final Object attributeValue = e.getValue();

        final String processedValue =
            attributeMapper
                .getOrDefault(e.getKey(), CssStyle::defaultAttributeMapping)
                .apply(attributeValue);

        return String.format("%s:%s", key, processedValue);
    }

    public String toHtml4StyleString() {
        return
            attributes
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .filter(s -> attributeHtml4Mapper.containsKey(s.getKey()))
                .map(e -> styleHtml4ItemMapping(e, attributeHtml4Mapper))
                .collect(Collectors.joining(" "));
    }

    public static String styleHtml4ItemMapping(
        Map.Entry<String, Object> e,
        Map<String, Function<Object, String>> map
    ) {
        final String key = e.getKey();

        final Object attributeValue = e.getValue();

        final String processedValue =
            map
                .getOrDefault(e.getKey(), CssStyle::defaultAttributeMapping)
                .apply(attributeValue);

        return String.format("%s\"%s\"", key, processedValue);
    }

    protected static String defaultAttributeMapping(Object o) {
        return String.valueOf(o);
    }
}
