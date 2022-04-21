package com.reporter.domain;

import com.reporter.domain.styles.TextStyle;
import com.reporter.formatter.FormatterVisitor;
import com.google.common.base.MoreObjects;

/**
 * Table cell,
 * contains data in text form,
 * base {@link TextStyle style} text
 * and customIndex (default parent-row index)
 */
public class TableCell extends TextItem<TableCell> {

    protected int customIndex;

    public static TableCell create(String text) {
        return new TableCell().setText(text);
    }

    public static TableCell create() {
        return new TableCell();
    }

    public TableCell accept(FormatterVisitor visitor) throws Exception {
        visitor.visitTableCell(this);
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("customIndex", customIndex)
            .add("parent", super.toString())
            .toString();
    }

    public int getCustomIndex() {
        return customIndex;
    }

    public TableCell setCustomIndex(int customIndex) {
        this.customIndex = customIndex;
        return this;
    }
}
