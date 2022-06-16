package com.reporter.formatter;

import com.reporter.domain.CompositionPart;
import com.reporter.domain.Document;
import com.reporter.domain.DocumentCase;
import com.reporter.domain.DocumentItem;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class writes to the log calls of non-overridden Visitor methods,
 * warning about a possible error in the implementation.
 */
public class FormatterVisitor implements Visitor {

    private static final Logger log = LoggerFactory.getLogger(FormatterVisitor.class);

    @Override
    public void visitComposition(CompositionPart<?, ?> compositionPart) throws Throwable {
        for (final DocumentItem item : compositionPart.getParts()) {
            item.accept(this);
        }
    }

    @Override
    public void visitDocument(Document documentObj) throws Throwable {
        log.debug("No overriding for visitDocument: {}", documentObj);
    }

    @Override
    public void visitDocumentCase(DocumentCase documentCase) throws Throwable {
        log.debug("No overriding for visitDocumentCase: {}", documentCase);
    }

    @Override
    public void visitTitle(Title titleObj) throws Exception {
        log.debug("No overriding for visitTitle: {}", titleObj);
    }

    @Override
    public void visitHeading(Heading headingObj) throws Exception {
        log.debug("No overriding for visitHeading: {}", headingObj);
    }

    @Override
    public void visitParagraph(Paragraph paragraphObj) throws Exception {
        log.debug("No overriding for visitParagraph: {}", paragraphObj);
    }

    @Override
    public void visitTable(Table tableObj) throws Throwable {
        log.debug("No overriding for visitTable: {}", tableObj);
    }

    @Override
    public void visitTableHeaderRow(TableHeaderRow tableHeaderRowObj) throws Throwable {
        log.debug("No overriding for visitTableHeaderRow: {}", tableHeaderRowObj);
    }

    @Override
    public void visitTableHeaderCell(TableHeaderCell tableHeaderCellObj) throws Throwable {
        log.debug("No overriding for visitTableHeaderCell: {}", tableHeaderCellObj);
    }

    @Override
    public void visitTableRow(TableRow tableRowObj) throws Throwable {
        log.debug("No overriding for visitTableRow: {}", tableRowObj);
    }

    @Override
    public void visitTableCell(TableCell tableCellObj) throws Exception {
        log.debug("No overriding for visitTableCell: {}", tableCellObj);
    }

    @Override
    public void visitSeparator(Separator separatorObj) throws Exception {
        log.debug("No overriding for visitSeparator: {}", separatorObj);
    }

    @Override
    public void visitFooter(Footer footerObj) throws Exception {
        log.debug("No overriding for visitFooter: {}", footerObj);
    }
}
