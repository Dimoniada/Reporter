package com.reporter.domain;

import com.reporter.formatter.FormatterVisitor;
import com.google.common.base.MoreObjects;

/**
 * Table header,
 * consists of {@link TableHeaderCell cells}
 */
public class TableHeaderRow extends CompositionPart<TableHeaderRow, TableHeaderCell> {

    private int cellCount;

    public static TableHeaderRow create(TableHeaderCell... docItems) {
        return new TableHeaderRow().addParts(docItems);
    }

    public static TableHeaderRow create() {
        return new TableHeaderRow();
    }

    public TableHeaderRow accept(FormatterVisitor visitor) throws Throwable {
        visitor.visitTableHeaderRow(this);
        return this;
    }

    @Override
    public TableHeaderRow addPart(TableHeaderCell docItem) {
        this.cellCount += 1;
        return super.addPart(docItem);
    }

    @Override
    public TableHeaderRow addParts(TableHeaderCell... docItems) {
        this.cellCount += docItems.length;
        return super.addParts(docItems);
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("cellCount", cellCount)
                .add("parent", super.toString())
                .toString();
    }

    public int getCellCount() {
        return cellCount;
    }
}
