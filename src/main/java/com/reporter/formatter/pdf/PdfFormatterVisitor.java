package com.reporter.formatter.pdf;

import com.google.common.base.MoreObjects;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.UnitValue;
import com.reporter.domain.*;
import com.reporter.domain.styles.StyleService;
import com.reporter.domain.styles.constants.BorderWeight;
import com.reporter.formatter.Formatter;
import com.reporter.formatter.pdf.styles.PdfPageEventHandler;
import com.reporter.formatter.pdf.styles.PdfStyleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * The class generates a representation of a pdf document {@link Document}
 */
public class PdfFormatterVisitor extends Formatter {
    protected PdfWriter writer;
    protected PdfDocument pdf;
    protected com.itextpdf.layout.element.Table table;
    protected com.itextpdf.layout.Document document;
    protected String encoding;
    protected DecimalFormat decimalFormat;
    protected StyleService styleService;

    private final String EXTENSION = "pdf";
    private final MediaType MEDIA_TYPE = MediaType.parseMediaType("application/pdf");
    private final float DEFAULT_MARGIN = 20;
    private final Logger log = LoggerFactory.getLogger(PdfFormatterVisitor.class);

    @Override
    public String getExtension() {
        return EXTENSION;
    }

    @Override
    public MediaType getContentMediaType() {
        return MEDIA_TYPE;
    }

    @Override
    public void initializeResource() throws IOException {
        outputStream = getOutputStream();
    }

    @Override
    public void cleanupResource() {
        /**/
    }

    @Override
    public void visitDocument(Document documentObj) throws Throwable {
        styleService = getStyleService();
        writer = new PdfWriter(outputStream);
        pdf = new PdfDocument(writer);

        /*Add MetaInfo*/
        final var pdfInfo = pdf.getDocumentInfo();
        if (StringUtils.hasText(documentObj.getAuthor())) {
            pdfInfo.setAuthor(documentObj.getAuthor());
        }
        if (StringUtils.hasText(documentObj.getLabel())) {
            pdfInfo.setTitle(documentObj.getLabel());
        }
        if (StringUtils.hasText(documentObj.getDescription())) {
            pdfInfo.setSubject(documentObj.getDescription());
        }
        pdfInfo.setCreator("PdfFormatter");

        document = new com.itextpdf.layout.Document(pdf);
        document.setMargins(DEFAULT_MARGIN, DEFAULT_MARGIN, DEFAULT_MARGIN, DEFAULT_MARGIN);
        visitComposition(documentObj);
        document.close();
    }

    /**
     * For a PDF document - {@link DocumentCase} is a new page.
     * While a {@link DocumentCase} added to a {@link Document} structure,
     * the last page will be ended and a new page will be created with a writer on it.
     */
    @Override
    public void visitDocumentCase(DocumentCase documentCase) throws Throwable {
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        visitComposition(documentCase);
    }

    @Override
    public void visitTitle(Title titleObj) throws Exception {
        ((PdfStyleService) styleService).handleSimpleElement(titleObj, document);
    }

    @Override
    public void visitParagraph(Paragraph paragraphObj) throws Exception {
        ((PdfStyleService) styleService).handleSimpleElement(paragraphObj, document);
    }

    @Override
    public void visitHeading(Heading headingObj) throws Exception {
        ((PdfStyleService) styleService).handleSimpleElement(headingObj, document);
    }

    @Override
    public void visitTable(Table tableObj) throws Throwable {
//        final var watch = new StopWatch();
//        watch.start();
        final var style =
                styleService
                        .extractStyleFor(tableObj)
                        .orElse(tableObj.getStyle());
        if (StringUtils.hasText(tableObj.getLabel())) {
            final var text = new Text(tableObj.getLabel());
            ((PdfStyleService) styleService).convertStyleToElement(style, text, text);
            document.add(new com.itextpdf.layout.element.Paragraph(text));
        }
        if (tableObj.getTableHeaderRow().isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("There is no header row in table %s", tableObj)
            );
        }
        final var tableHeaderRow = tableObj.getTableHeaderRow().get();
        final var colCount = tableHeaderRow.getCellCount();
        if (colCount > 0) {
            final var columns = new float[colCount];
            Arrays.fill(columns, 1);
            table = new com.itextpdf.layout.element.Table(UnitValue.createPercentArray(columns));
            table.setWidth(UnitValue.createPercentValue(100));
            visitTableHeaderRow(tableHeaderRow);
            visitComposition(tableObj);
            document.add(table);
        }
//        watch.stop();
//        log.info("Table visited in {} ms", watch.getTotalTimeMillis());
    }

    @Override
    public void visitTableHeaderRow(TableHeaderRow tableHeaderRowObj) throws Throwable {
        visitComposition(tableHeaderRowObj);
    }

    @Override
    public void visitTableHeaderCell(TableHeaderCell tableHeaderCellObj) throws Exception {
        final var cell = ((PdfStyleService) styleService).handleTableCustomCell(tableHeaderCellObj);
        table.addHeaderCell(cell);
    }

    @Override
    public void visitTableRow(TableRow tableRowObj) throws Throwable {
        styleService.extractStyleFor(tableRowObj);
        visitComposition(tableRowObj);
    }

    @Override
    public void visitTableCell(TableCell tableCellObj) throws Exception {
        final var cell = ((PdfStyleService) styleService).handleTableCustomCell(tableCellObj);
        table.addCell(cell);
    }

    @Override
    public void visitSeparator(Separator separatorObj) throws Exception {
        final var border = separatorObj.getBorderStyle();
        if (border != null && border.getWeight() != BorderWeight.NONE) {
            final var lineDrawer = PdfStyleService.toPdfILineDrawer(border.getWeight());
            lineDrawer.setColor(PdfStyleService.toPdfColor(border.getColor()));
            document.add(new LineSeparator(lineDrawer));
        }
    }

    @Override
    public void visitFooter(Footer footerObj) throws Exception {
        final var text = new Text(footerObj.getText());
        final var elParagraph = new com.itextpdf.layout.element.Paragraph(text);
        final var style =
                styleService
                        .extractStyleFor(footerObj)
                        .orElse(footerObj.getStyle());
        ((PdfStyleService) styleService).convertStyleToElement(style, text, elParagraph);

        pdf.addEventHandler(
                PdfDocumentEvent.END_PAGE,
                PdfPageEventHandler.create(elParagraph, document)
        );
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("writer", writer)
                .add("pdf", pdf)
                .add("table", table)
                .add("os", outputStream)
                .add("document", document)
                .add("encoding", encoding)
                .add("decimalFormat", decimalFormat)
                .add("styleService", styleService)
                .add("parent", super.toString())
                .toString();
    }

    public PdfWriter getWriter() {
        return writer;
    }

    public PdfFormatterVisitor setWriter(PdfWriter writer) {
        this.writer = writer;
        return this;
    }

    public PdfDocument getPdf() {
        return pdf;
    }

    public PdfFormatterVisitor setPdf(PdfDocument pdf) {
        this.pdf = pdf;
        return this;
    }

    public com.itextpdf.layout.element.Table getTable() {
        return table;
    }

    public PdfFormatterVisitor setTable(com.itextpdf.layout.element.Table table) {
        this.table = table;
        return this;
    }

    public com.itextpdf.layout.Document getDocument() {
        return document;
    }

    public PdfFormatterVisitor setDocument(com.itextpdf.layout.Document document) {
        this.document = document;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public PdfFormatterVisitor setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public PdfFormatterVisitor setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
        return this;
    }

    public StyleService getStyleService() throws Exception {
        if (styleService == null) {
            styleService = PdfStyleService.create(encoding, null, decimalFormat);
        }
        return styleService;
    }

    @SuppressWarnings("unchecked")
    public <T extends PdfFormatterVisitor> T setStyleService(StyleService styleService) {
        this.styleService = styleService;
        return (T) this;
    }
}
