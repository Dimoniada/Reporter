package com.reporter.domain;

import com.reporter.formatter.FormatterVisitor;
import com.google.common.base.MoreObjects;

/**
 * Document title,
 * contains only text
 */
public class Title extends TextItem<Title> {

    public static Title create() {
        return new Title();
    }

    public static Title create(String text) {
        return new Title().setText(text);
    }

    public Title accept(FormatterVisitor visitor) throws Exception {
        visitor.visitTitle(this);
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
