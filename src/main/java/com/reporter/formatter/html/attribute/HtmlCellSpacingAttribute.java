package com.reporter.formatter.html.attribute;

public class HtmlCellSpacingAttribute extends HtmlAttribute {
    public static final String ATTR_NAME = "cellspacing";

    @Override
    public String getAttribute() {
        return ATTR_NAME;
    }

    public HtmlCellSpacingAttribute setCellSpacing(int cellSpacing) {
        setAttributeValue(cellSpacing);
        return this;
    }
}
