package com.reporter.formatter.html.tag;

import com.reporter.formatter.html.attribute.HtmlAlignAttribute;
import com.reporter.formatter.html.attribute.HtmlAttribute;
import com.reporter.formatter.html.attribute.HtmlBgColorAttribute;
import com.reporter.formatter.html.attribute.HtmlBorderAttribute;
import com.reporter.formatter.html.attribute.HtmlCellSpacingAttribute;

import java.util.HashMap;
import java.util.Map;

/**
 * The class stores attributes that describe the style of the element for HTML4.
 */
public abstract class Html4StyledTag {
    protected Map<String, HtmlAttribute> availableAttributes = new HashMap<>();

    final HtmlBorderAttribute border = new HtmlBorderAttribute();
    final HtmlCellSpacingAttribute cellSpacing = new HtmlCellSpacingAttribute();
    final HtmlBgColorAttribute bgColor = new HtmlBgColorAttribute();
    final HtmlAlignAttribute align = new HtmlAlignAttribute();

    public Html4StyledTag() {
        availableAttributes.putAll(
            Map.ofEntries(
                bgColor.getAttributeMapper(),
                cellSpacing.getAttributeMapper(),
                border.getAttributeMapper(),
                align.getAttributeMapper()
            )
        );
    }

    public Html4StyledTag setBgColor(String bgColor) {
        this.bgColor.setBgColor(bgColor);
        return this;
    }

    public Html4StyledTag setCellSpacing(int cellSpacing) {
        this.cellSpacing.setCellSpacing(cellSpacing);
        return this;
    }

    public Html4StyledTag setBorder(int border) {
        this.border.setBorder(border);
        return this;
    }

    public Html4StyledTag setAlign(String align) {
        this.align.setAlign(align);
        return this;
    }

    public Map<String, HtmlAttribute> getAvailableAttributes() {
        return availableAttributes;
    }
}
