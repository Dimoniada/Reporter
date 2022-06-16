package com.reporter.domain;

import com.reporter.domain.styles.TextStyle;
import com.reporter.formatter.FormatterVisitor;
import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Table class,
 * contains the label {@link Table#label},
 * optionally contain a header line with column names - {@link TableRow tableHeaderRow},
 * as well as an array of rows {@link TableRow tableRows}.
 * <p>
 * If the table style is {@link TextStyle},
 * then it only applies to {@link Table#label}
 */
public class Table extends CompositionPart<Table, TableRow> {
    private static final Logger log = LoggerFactory.getLogger(Table.class);
    /**
     * Table header, contains column names
     */
    protected TableHeaderRow tableHeaderRow;

    /**
     * Table label, displayed in front of its header
     */
    protected String label;

    /**
     * Current number of columns in the table
     */
    protected int colCount;

    /**
     * Current number of rows in the table
     */
    protected int rowCount;

    public static Table create(TableHeaderRow tableHeaderRow) {
        return new Table().setTableHeaderRow(tableHeaderRow);
    }

    public static Table create() {
        return new Table();
    }

    public Table accept(FormatterVisitor visitor) throws Throwable {
        visitor.visitTable(this);
        log.debug("accept: called on table {}", this);
        return this;
    }

    @Override
    public Table addPart(TableRow docItem) {
        docItem.setRowIndex(rowCount);
        this.rowCount += 1;
        this.colCount = Integer.max(this.colCount, docItem.getCellCount());
        log.debug("addPart: result columns - {} and rows - {}", rowCount, colCount);
        return super.addPart(docItem);
    }

    @Override
    public Table addParts(TableRow... docItems) {
        for (var i = 0; i < docItems.length; i++) {
            docItems[i].setRowIndex(i + rowCount);
        }
        this.rowCount += docItems.length;
        Stream.of(docItems)
            .map(TableRow::getCellCount)
            .mapToInt(v -> v)
            .max()
            .ifPresent(max -> this.colCount = Integer.max(this.colCount, max));
        log.debug("addParts: result columns - {} and rows - {}", rowCount, colCount);
        return super.addParts(docItems);
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("tableHeaderRow", tableHeaderRow)
                .add("label", label)
                .add("parent", super.toString())
                .toString();
    }

    public Optional<TableHeaderRow> getTableHeaderRow() {
        return Optional.ofNullable(tableHeaderRow);
    }

    public Table setTableHeaderRow(TableHeaderRow tableHeaderRow) {
        this.tableHeaderRow = tableHeaderRow;
        log.debug("setTableHeaderRow: result tableHeaderRow - {}", tableHeaderRow);
        return this;
    }

    public String getLabel() {
        return label;
    }

    public Table setLabel(String label) {
        this.label = label;
        log.debug("setLabel: result label - {}", label);
        return this;
    }
}
