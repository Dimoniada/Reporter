package com.reporter.formatter.html.attribute;

public class HtmlBorderAttribute extends HtmlAttribute {
    public static final String ATTR_NAME = "border";

    @Override
    public String getAttribute() {
        return ATTR_NAME;
    }

    public HtmlBorderAttribute setBorder(int border) {
        setAttributeValue(border);
        return this;
    }
}
