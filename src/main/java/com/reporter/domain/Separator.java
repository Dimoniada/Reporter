package com.reporter.domain;

import com.reporter.domain.styles.BorderStyle;
import com.reporter.formatter.FormatterVisitor;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * For Pdf and Html:
 * horizontal line from left to right in the inner border of the sheet,
 * analogue of <hr> for html.
 */

public class Separator extends DocumentItem {
    /**
     * Separator line type
     */
    protected BorderStyle borderStyle;

    public static Separator create() {
        return new Separator();
    }

    public static Separator create(BorderStyle borderStyle) {
        return new Separator().setBorderStyle(borderStyle);
    }

    public Separator accept(FormatterVisitor visitor) throws Exception {
        visitor.visitSeparator(this);
        return this;
    }

    public BorderStyle getBorderStyle() {
        return borderStyle;
    }

    public Separator setBorderStyle(BorderStyle borderStyle) {
        this.borderStyle = borderStyle;
        return this;
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("borderStyle", borderStyle)
                .add("parent", super.toString())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Separator that = (Separator) o;

        return
            Objects.equal(this.borderStyle, that.borderStyle)
                && Objects.equal(this.style, that.style);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(borderStyle, style);
    }
}
