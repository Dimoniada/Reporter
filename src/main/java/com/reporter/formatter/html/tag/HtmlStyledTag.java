package com.reporter.formatter.html.tag;

import com.reporter.formatter.html.attribute.HtmlClassAttribute;
import com.reporter.formatter.html.attribute.HtmlStyleAttribute;
import com.reporter.formatter.html.styles.CssStyle;

import java.util.Map;

/**
 * The class stores attributes that describe the style of the element for HTML.
 */
public abstract class HtmlStyledTag extends Html4StyledTag {

    final HtmlStyleAttribute style = new HtmlStyleAttribute();
    final HtmlClassAttribute clazz = new HtmlClassAttribute();

    public HtmlStyledTag() {
        availableAttributes.putAll(
            Map.ofEntries(
                style.getAttributeMapper(),
                clazz.getAttributeMapper()
            )
        );
    }

    public HtmlStyledTag setStyle(CssStyle cssStyle) {
        this.style.setStyle(cssStyle);
        return this;
    }

    public HtmlStyledTag setClass(String clazz) {
        this.clazz.setClass(clazz);
        return this;
    }
}
