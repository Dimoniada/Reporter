package com.reporter.domain;

import com.reporter.formatter.FormatterVisitor;
import com.google.common.base.MoreObjects;

/**
 * Table header cell,
 * contains data in text form
 */
public class TableHeaderCell extends TextItem<TableHeaderCell> {
    /**
     * Real column name in SQL view
     */
    protected String aliasName = "";

    public static TableHeaderCell create(String text) {
        return new TableHeaderCell().setText(text);
    }

    public static TableHeaderCell create() {
        return new TableHeaderCell();
    }

    public TableHeaderCell accept(FormatterVisitor visitor) throws Throwable {
        visitor.visitTableHeaderCell(this);
        return this;
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("aliasName", aliasName)
                .add("parent", super.toString())
                .toString();
    }

    public String getAliasName() {
        return aliasName;
    }

    public TableHeaderCell setAliasName(String aliasName) {
        this.aliasName = aliasName;
        return this;
    }
}
