package com.reporter.formatter.html.attribute;

import java.util.AbstractMap;
import java.util.Locale;
import java.util.Map;

/**
 * Class of the html attribute.
 */
public abstract class HtmlAttribute {
    public static final String DELIMITER_PATTERN_HTML = ";";
    public static final String DELIMITER_PATTERN_HTML4 = " ";

    public static final String ASSIGNMENT_PATTERN_HTML = "%s:%s";
    public static final String ASSIGNMENT_PATTERN_HTML4 = "%s=\"%s\"";

    public final Map.Entry<String, HtmlAttribute> attributeMapper;

    protected Object attributeValue;

    public HtmlAttribute() {
        attributeMapper = new AbstractMap
            .SimpleEntry<>(getAttribute(), this);
    }

    public abstract String getAttribute();

    public String toLower(Locale locale) {
        return getAttribute().toLowerCase(locale);
    }

    public String toLower() {
        return getAttribute().toLowerCase(Locale.ENGLISH);
    }

    public String toUpper(Locale locale) {
        return getAttribute().toUpperCase(locale);
    }

    public String toUpper() {
        return getAttribute().toUpperCase(Locale.ENGLISH);
    }

    public String produceDefaultStringAttribute(Object o) {
        return String.valueOf(o);
    }

    public Map.Entry<String, HtmlAttribute> getAttributeMapper() {
        return attributeMapper;
    }

    public Object getAttributeValue() {
        return attributeValue;
    }

    protected void setAttributeValue(Object attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getAssignmentPattern(Boolean isHtml4) {
        return
            isHtml4
                ? ASSIGNMENT_PATTERN_HTML4
                : ASSIGNMENT_PATTERN_HTML;
    }
}
