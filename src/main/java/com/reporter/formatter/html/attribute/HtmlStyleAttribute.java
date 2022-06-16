package com.reporter.formatter.html.attribute;

import com.reporter.formatter.html.styles.CssStyle;

public class HtmlStyleAttribute extends HtmlAttribute {
    public static final String ATTR_NAME = "style";

    @Override
    public String getAttribute() {
        return ATTR_NAME;
    }

    public HtmlStyleAttribute setStyle(CssStyle cssStyle) {
        setAttributeValue(cssStyle);
        return this;
    }

    @Override
    public String produceDefaultStringAttribute(Object o) {
        if (o instanceof CssStyle) {
            return ((CssStyle) o).toCssStyleString();
        } else {
            throw new IllegalArgumentException("Can't get value for style attribute");
        }
    }

    @Override
    public String getAssignmentPattern(Boolean isHtml4) {
        return ASSIGNMENT_PATTERN_HTML4;
    }
}
