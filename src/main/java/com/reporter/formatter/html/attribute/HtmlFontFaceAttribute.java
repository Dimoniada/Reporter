package com.reporter.formatter.html.attribute;

public class HtmlFontFaceAttribute extends HtmlAttribute {
    public static final String ATTR_NAME = "face";

    @Override
    public String getAttribute() {
        return ATTR_NAME;
    }

    public HtmlFontFaceAttribute setFace(String face) {
        setAttributeValue(face);
        return this;
    }
}
