package com.reporter.domain;

import com.reporter.formatter.FormatterVisitor;
import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * Table column,
 * consists of {@link TableCell cells}
 */
public class TableRow extends CompositionPart<TableRow, TableCell> {

    protected int rowIndex;
    private int cellCount;

    public static TableRow create(TableCell... tableCells) {
        return new TableRow().addParts(tableCells);
    }

    public static TableRow create() {
        return new TableRow();
    }

    public TableRow accept(FormatterVisitor visitor) throws Throwable {
        visitor.visitTableRow(this);
        return this;
    }

    @Override
    public TableRow addPart(TableCell docItem) {
        this.cellCount ++;
        docItem.setCustomIndex(rowIndex);
        return super.addPart(docItem);
    }

    @Override
    public TableRow addParts(TableCell... docItems) {
        this.cellCount += docItems.length;
        for (var item : docItems) {
            item.setCustomIndex(rowIndex);
        }
        return super.addParts(docItems);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("parent", super.toString())
            .toString();
    }

    public int getCellCount() {
        return cellCount;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public TableRow setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
        if(parts != null) {
            for (var item : parts) {
                item.setCustomIndex(rowIndex);
            }
        }
        return this;
    }
}
