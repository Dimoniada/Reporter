package com.reporter.formatter.html.attribute;

public class HtmlBgColorAttribute extends HtmlAttribute {
    public static final String ATTR_NAME = "bgcolor";

    @Override
    public String getAttribute() {
        return ATTR_NAME;
    }

    public HtmlBgColorAttribute setBgColor(String bgColor) {
        setAttributeValue(bgColor);
        return this;
    }
}
