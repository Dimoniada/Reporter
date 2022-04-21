package com.reporter.formatter.html.attribute;

public class HtmlSizeAttribute extends HtmlAttribute {
    public static final String ATTR_NAME = "size";

    @Override
    public String getAttribute() {
        return ATTR_NAME;
    }

    public HtmlSizeAttribute setSize(int size) {
        setAttributeValue(size);
        return this;
    }
}
