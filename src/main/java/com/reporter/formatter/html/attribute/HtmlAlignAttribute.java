package com.reporter.formatter.html.attribute;

public class HtmlAlignAttribute extends HtmlAttribute {
    public static final String ATTR_NAME = "align";

    @Override
    public String getAttribute() {
        return ATTR_NAME;
    }

    public HtmlAlignAttribute setAlign(String align) {
        setAttributeValue(align);
        return this;
    }
}
