package com.reporter.formatter;

import com.reporter.domain.CompositionPart;
import com.reporter.domain.Document;
import com.reporter.domain.DocumentCase;
import com.reporter.domain.Footer;
import com.reporter.domain.Heading;
import com.reporter.domain.Paragraph;
import com.reporter.domain.Separator;
import com.reporter.domain.Table;
import com.reporter.domain.TableCell;
import com.reporter.domain.TableHeaderCell;
import com.reporter.domain.TableHeaderRow;
import com.reporter.domain.TableRow;
import com.reporter.domain.Title;

/**
 * Basic visitor interface
 */
public interface Visitor {
    void visitComposition(CompositionPart<?, ?> compositionPart) throws Throwable;

    void visitDocument(Document documentObj) throws Throwable;

    void visitDocumentCase(DocumentCase documentCase) throws Throwable;

    void visitTitle(Title titleObj) throws Exception;

    void visitHeading(Heading headingObj) throws Exception;

    void visitParagraph(Paragraph paragraphObj) throws Exception;

    void visitTable(Table tableObj) throws Throwable;

    void visitTableHeaderRow(TableHeaderRow tableHeaderRowObj) throws Throwable;

    void visitTableHeaderCell(TableHeaderCell tableHeaderCell) throws Throwable;

    void visitTableRow(TableRow tableRowObj) throws Throwable;

    void visitTableCell(TableCell tableCellObj) throws Exception;

    void visitSeparator(Separator separatorObj) throws Exception;

    void visitFooter(Footer footerObj) throws Exception;
}
