package com.reporter.domain;

import com.reporter.formatter.FormatterVisitor;
import com.google.common.base.MoreObjects;

/**
 * Text footer
 */
public class Footer extends TextItem<Footer> {

    public static Footer create() {
        return new Footer();
    }

    public static Footer create(String text) {
        return new Footer().setText(text);
    }

    public Footer accept(FormatterVisitor visitor) throws Exception {
        visitor.visitFooter(this);
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
