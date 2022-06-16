package com.reporter.formatter.html.tag;

import com.reporter.formatter.html.attribute.HtmlColorAttribute;
import com.reporter.formatter.html.attribute.HtmlFontFaceAttribute;
import com.reporter.formatter.html.attribute.HtmlSizeAttribute;

import java.util.Map;

public class Html4Font extends HtmlTag {
    public static final String TAG_NAME = "font";

    final HtmlColorAttribute color = new HtmlColorAttribute();
    final HtmlFontFaceAttribute face = new HtmlFontFaceAttribute();
    final HtmlSizeAttribute size = new HtmlSizeAttribute();

    public Html4Font() {
        availableAttributes = Map.ofEntries(
            size.getAttributeMapper(),
            face.getAttributeMapper(),
            color.getAttributeMapper()
        );
    }

    @Override
    public String getTagName() {
        return TAG_NAME;
    }

    public Html4Font setSize(int size) {
        this.size.setSize(size);
        return this;
    }

    public Html4Font setFace(String face) {
        this.face.setFace(face);
        return this;
    }

    public Html4Font setColor(String color) {
        this.color.setColor(color);
        return this;
    }
}
