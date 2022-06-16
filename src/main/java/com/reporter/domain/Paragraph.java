package com.reporter.domain;

import com.reporter.domain.styles.TextStyle;
import com.reporter.formatter.FormatterVisitor;
import com.google.common.base.MoreObjects;

/**
 * Paragraph
 */
public class Paragraph extends TextItem<Paragraph> {
    public static Paragraph create(String text, TextStyle textStyle) {
        return new Paragraph().setText(text).setStyle(textStyle);
    }

    public static Paragraph create(String text) {
        return create(text, null);
    }

    public static Paragraph create() {
        return create(null, null);
    }

    public Paragraph accept(FormatterVisitor visitor) throws Exception {
        visitor.visitParagraph(this);
        return this;
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("parent", super.toString())
                .toString();
    }
}
