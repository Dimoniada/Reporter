package com.reporter.formatter.html.attribute;

public class HtmlClassAttribute extends HtmlAttribute {
    public static final String ATTR_NAME = "class";

    @Override
    public String getAttribute() {
        return ATTR_NAME;
    }

    public HtmlClassAttribute setClass(String clazz) {
        setAttributeValue(clazz);
        return this;
    }

    @Override
    public String getAssignmentPattern(Boolean isHtml4) {
        return ASSIGNMENT_PATTERN_HTML4;
    }
}
