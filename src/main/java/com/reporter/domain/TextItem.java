package com.reporter.domain;

import com.reporter.domain.styles.TextStyle;
import com.google.common.base.MoreObjects;

/**
 * Base text class,
 * contains text and {@link TextStyle style}
 */
public abstract class TextItem<T> extends DocumentItem {
    protected String text;

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("text", text)
                .add("parent", super.toString())
                .toString();
    }

    public String getText() {
        return text;
    }

    @SuppressWarnings("unchecked")
    public T setText(String text) {
        this.text = text;
        return (T) this;
    }

}
