package com.reporter.formatter.html;

import com.reporter.domain.*;
import com.reporter.domain.styles.BorderStyle;
import com.reporter.domain.styles.LayoutStyle;
import com.reporter.domain.styles.Style;
import com.reporter.domain.styles.StyleService;
import com.reporter.domain.styles.constants.BorderWeight;
import com.reporter.domain.styles.constants.Color;
import com.reporter.formatter.Formatter;
import com.reporter.formatter.html.styles.HtmlStyleService;
import com.reporter.formatter.html.tag.Html;
import com.reporter.formatter.html.tag.HtmlBody;
import com.reporter.formatter.html.tag.HtmlFooter;
import com.reporter.formatter.html.tag.HtmlH1;
import com.reporter.formatter.html.tag.HtmlHead;
import com.reporter.formatter.html.tag.HtmlHeading;
import com.reporter.formatter.html.tag.HtmlLineSeparator;
import com.reporter.formatter.html.tag.HtmlParagraph;
import com.reporter.formatter.html.tag.HtmlTable;
import com.reporter.formatter.html.tag.HtmlTableCell;
import com.reporter.formatter.html.tag.HtmlTableHeaderCell;
import com.reporter.formatter.html.tag.HtmlTableRow;
import com.reporter.formatter.html.tag.HtmlTag;
import com.reporter.formatter.html.tag.HtmlTitle;
import com.google.common.base.MoreObjects;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * The class generates a representation of the html document {@link Document}
 */
public class HtmlFormatterVisitor extends Formatter {
    private static final String EXTENSION = "html";
    private static final MediaType MEDIA_TYPE = MediaType.parseMediaType("text/html");

    protected OutputStreamWriter outputStreamWriter;

    protected String encoding;
    protected Locale locale;
    protected DecimalFormat decimalFormat;

    protected StyleService styleService;
    protected TagCreator tagCreator;

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
        outputStreamWriter = new OutputStreamWriter(outputStream, encoding);
        tagCreator = new TagCreator(outputStreamWriter, decimalFormat);
        tagCreator.write("<!doctype html>");
        final var html = new Html();
        final var htmlHead = new HtmlHead();
        final var htmlBody = new HtmlBody();

        tagCreator.write(html.open());
        tagCreator.write(htmlHead.open());
        tagCreator.write("<meta charset=\"" + encoding + "\">");
        styleService.writeStyles(outputStreamWriter);
        if (StringUtils.hasText(documentObj.getLabel())) {
            final var htmlTitle = new HtmlTitle();
            tagCreator.write(htmlTitle.open());
            tagCreator.write(HtmlUtils.htmlEscape(documentObj.getLabel()));
            tagCreator.write(htmlTitle.close());
        }
        tagCreator.write(htmlHead.close());
        tagCreator.write(htmlBody.open());
        this.visitComposition(documentObj);
        tagCreator.write(htmlBody.close());
        tagCreator.write(html.close());
        outputStreamWriter.close();
    }

    @Override
    public void visitDocumentCase(DocumentCase documentCase) throws Throwable {
        this.visitComposition(documentCase);
    }

    @Override
    public void visitTitle(Title titleObj) throws Exception {
        final var htmlTitle = new HtmlH1();
        final var style = styleService.extractStyleFor(titleObj).orElse(titleObj.getStyle());
        handleTag(htmlTitle, titleObj, style, true);
    }

    @Override
    public void visitHeading(Heading headingObj) throws Exception {
        final var htmlHeading = new HtmlHeading(headingObj.getDepth());
        final var style = styleService.extractStyleFor(headingObj).orElse(headingObj.getStyle());
        handleTag(htmlHeading, headingObj, style, true);
    }

    @Override
    public void visitParagraph(Paragraph paragraphObj) throws Exception {
        final var htmlParagraph = new HtmlParagraph();
        final var style = styleService.extractStyleFor(paragraphObj).orElse(paragraphObj.getStyle());
        handleTag(htmlParagraph, paragraphObj, style, true);
    }

    @Override
    public void visitTable(Table tableObj) throws Throwable {
        final var htmlTable = new HtmlTable();
        var style = styleService.extractStyleFor(tableObj).orElse(tableObj.getStyle());
        final var htmlStyleService = (HtmlStyleService) styleService;

        if (style == null && htmlStyleService.getUseHtml4Tags()) {
            style = LayoutStyle.create().setBorderBottom(BorderStyle.create(Color.BLACK, BorderWeight.THIN));
            handleTag(htmlTable, tableObj, style, false);
        } else if (htmlStyleService.getUseHtml4Tags()) {
            handleTag(htmlTable, tableObj, style, false);
        } else {
            outputStreamWriter.write(htmlTable.open());
        }

        if (tableObj.getTableHeaderRow().isPresent()) {
            visitTableHeaderRow(tableObj.getTableHeaderRow().get());
        }

        this.visitComposition(tableObj);
        outputStreamWriter.write(htmlTable.close());
    }

    @Override
    public void visitTableHeaderRow(TableHeaderRow tableHeaderRowObj) throws Throwable {
        visitRow(tableHeaderRowObj);
    }

    @Override
    public void visitTableHeaderCell(TableHeaderCell tableHeaderCellObj) throws Throwable {
        final var htmlTableHeaderCell = new HtmlTableHeaderCell();
        final var style = ((HtmlStyleService) styleService).handleCustomTableCellStyle(tableHeaderCellObj);
        handleTag(htmlTableHeaderCell, tableHeaderCellObj, style, true);
    }

    @Override
    public void visitTableRow(TableRow tableRowObj) throws Throwable {
        styleService.extractStyleFor(tableRowObj);
        visitRow(tableRowObj);
    }

    @Override
    public void visitTableCell(TableCell tableCellObj) throws Exception {
        final var htmlTableCell = new HtmlTableCell();
        final var style = ((HtmlStyleService) styleService).handleCustomTableCellStyle(tableCellObj);
        handleTag(htmlTableCell, tableCellObj, style, true);
    }

    @Override
    public void visitSeparator(Separator separatorObj) throws Exception {
        final var htmlLineSeparator = new HtmlLineSeparator();
        final var style = LayoutStyle.create().setBorderBottom(separatorObj.getBorderStyle());
        handleTag(htmlLineSeparator, separatorObj, style, false);
    }

    @Override
    public void visitFooter(Footer footerObj) throws Exception {
        final var htmlFooter = new HtmlFooter();
        final var style = styleService.extractStyleFor(footerObj).orElse(footerObj.getStyle());
        handleTag(htmlFooter, footerObj, style, true);
    }

    private void handleTag(HtmlTag tag, DocumentItem item, Style style, Boolean needCloseTag) throws Exception {
        final var htmlStyleService = ((HtmlStyleService) styleService);
        new TagCreator(outputStreamWriter, decimalFormat)
            .setItem(item)
            .writeTag(
                tag,
                style,
                htmlStyleService.contains(style),
                htmlStyleService.getUseHtml4Tags(),
                needCloseTag
            );
    }

    private void visitRow(CompositionPart<?, ?> row) throws Throwable {
        final var htmlTableRow = new HtmlTableRow();
        outputStreamWriter.write(htmlTableRow.open());
        this.visitComposition(row);
        outputStreamWriter.write(htmlTableRow.close());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("outputStreamWriter", outputStreamWriter)
            .add("encoding", encoding)
            .add("locale", locale)
            .add("decimalFormat", decimalFormat)
            .add("styleService", styleService)
            .add("parent", super.toString())
            .toString();
    }

    public OutputStreamWriter getOutputStreamWriter() {
        return outputStreamWriter;
    }

    public HtmlFormatterVisitor setOutputStreamWriter(OutputStreamWriter outputStreamWriter) {
        this.outputStreamWriter = outputStreamWriter;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public HtmlFormatterVisitor setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public StyleService getStyleService() {
        if (styleService == null) {
            styleService = HtmlStyleService.create(false, decimalFormat);
        }
        return styleService;
    }

    @SuppressWarnings("unchecked")
    public <T extends HtmlFormatterVisitor> T setStyleService(StyleService styleService) {
        this.styleService = styleService;
        return (T) this;
    }

    public Locale getLocale() {
        return locale;
    }

    public HtmlFormatterVisitor setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public HtmlFormatterVisitor setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
        return this;
    }
}
