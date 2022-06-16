package com.reporter.domain;

import com.reporter.domain.db.QueryTable;
import com.reporter.formatter.FormatterVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

//Tests to verify the contract between the visitor and domain objects
@ExtendWith(MockitoExtension.class)
public class VisitorContractTest {

    public static final int DEFAULT_DEPTH = 1;

    @InjectMocks
    public FormatterVisitor mockForLog;

    public FormatterVisitor mock;

    @BeforeEach
    public void initFormatter() {
        mock = Mockito.mock(FormatterVisitor.class,
                invocationOnMock -> {
                    throw new RuntimeException("This method " + invocationOnMock.getMethod() + " must not be called.");
                });
    }

    @Test
    public void testHeadingVisit() throws Throwable {
        final Heading heading = new Heading(DEFAULT_DEPTH);

        doNothing().when(mock).visitHeading(any());
        heading.accept(mock);
        Mockito.verify(mock, times(1)).visitHeading(heading);
    }

    @Test
    public void testTitleVisit() throws Throwable {
        final Title title = Title.create();

        doNothing().when(mock).visitTitle(any());
        title.accept(mock);
        Mockito.verify(mock, times(1)).visitTitle(title);
    }

    @Test
    public void testParagraphVisit() throws Throwable {
        final Paragraph paragraph = new Paragraph();

        doNothing().when(mock).visitParagraph(any());
        paragraph.accept(mock);
        Mockito.verify(mock, times(1)).visitParagraph(paragraph);
    }

    @Test
    public void testTableVisit() throws Throwable {
        final Table table = new Table();

        doNothing().when(mock).visitTable(table);
        table.accept(mock);
        Mockito.verify(mock, times(1)).visitTable(table);
    }

    @Test
    public void testTableRowVisit() throws Throwable {
        final TableRow tableRow = new TableRow();

        doNothing().when(mock).visitTableRow(tableRow);
        tableRow.accept(mock);
        Mockito.verify(mock, times(1)).visitTableRow(tableRow);
    }

    @Test
    public void testTableCellVisit() throws Throwable {
        final TableCell tableCell = new TableCell();

        doNothing().when(mock).visitTableCell(tableCell);
        tableCell.accept(mock);
        Mockito.verify(mock, times(1)).visitTableCell(tableCell);
    }

    @Test
    public void testTableHeaderCellVisit() throws Throwable {
        final TableHeaderCell tableHeaderCell = new TableHeaderCell();

        doNothing().when(mock).visitTableHeaderCell(tableHeaderCell);
        tableHeaderCell.accept(mock);
        Mockito.verify(mock, times(1)).visitTableHeaderCell(tableHeaderCell);
    }

    @Test
    public void testTableHeaderRowVisit() throws Throwable {
        final TableHeaderRow tableHeaderRow = new TableHeaderRow();

        doNothing().when(mock).visitTableHeaderRow(tableHeaderRow);
        tableHeaderRow.accept(mock);
        Mockito.verify(mock, times(1)).visitTableHeaderRow(tableHeaderRow);
    }

    @Test
    public void testDocumentCaseVisit() throws Throwable {
        final DocumentCase documentCase = DocumentCase.create();

        doNothing().when(mock).visitDocumentCase(documentCase);
        documentCase.accept(mock);
        Mockito.verify(mock, times(1)).visitDocumentCase(documentCase);
    }

    @Test
    public void testDocumentVisit() throws Throwable {
        final Document document = new Document();

        doNothing().when(mock).visitDocument(document);
        document.accept(mock);
        Mockito.verify(mock, times(1)).visitDocument(document);
    }

    @Test
    public void testFooterVisit() throws Throwable {
        final Footer footer = new Footer();

        doNothing().when(mock).visitFooter(footer);
        footer.accept(mock);
        Mockito.verify(mock, times(1)).visitFooter(footer);
    }

    @Test
    public void testReportTableVisit() throws Throwable {
        final ReportTable reportTable = new ReportTable();

        doNothing().when(mock).visitTable(reportTable);
        reportTable.accept(mock);
        Mockito.verify(mock, times(1)).visitTable(reportTable);
    }

    @Test
    public void testQueryTableVisit() throws Throwable {
        final QueryTable queryTable = new QueryTable().setQuery("select 1;");

        doNothing().when(mock).visitTable(queryTable);
        queryTable.accept(mock);
        Mockito.verify(mock, times(1)).visitTable(queryTable);
    }

    @Test
    public void testSeparatorVisit() throws Throwable {
        final Separator separator = new Separator();

        doNothing().when(mock).visitSeparator(separator);
        separator.accept(mock);
        Mockito.verify(mock, times(1)).visitSeparator(separator);
    }
}
