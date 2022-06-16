package com.reporter.formatter.word;

import com.google.common.base.MoreObjects;
import com.reporter.domain.*;
import com.reporter.domain.styles.StyleService;
import com.reporter.formatter.Formatter;
import com.reporter.formatter.word.styles.WordStyleService;
import org.apache.poi.common.usermodel.fonts.FontCharset;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class WordFormatterVisitor extends Formatter {
    private final Logger log = LoggerFactory.getLogger(WordFormatterVisitor.class);

    /**
     * docx native document {@link org.apache.poi.xwpf.usermodel.XWPFDocument}
     */
    protected XWPFDocument wordDocument;

    /**
     * docx native table {@link org.apache.poi.xwpf.usermodel.XWPFTable}
     */
    protected XWPFTable docxTable;

    protected FontCharset fontCharset;
    protected DecimalFormat decimalFormat;
    protected StyleService styleService;

    @Override
    public String getExtension() {
        return null;
    }

    @Override
    public MediaType getContentMediaType() {
        return null;
    }

    @Override
    public void initializeResource() throws IOException {
        wordDocument = getWordDocument();
        outputStream = getOutputStream();
    }

    @Override
    public void cleanupResource() throws IOException {
        wordDocument.write(outputStream);
        wordDocument.close();
    }

    @Override
    public void visitDocument(Document documentObj) throws Throwable {
        styleService = getStyleService();
        styleService.writeStyles(wordDocument);
        this.visitComposition(documentObj);
    }

    /**
     * Creates page and writes text on it, holding blank space before next page
     * @param documentCase paper which text will be written on
     * @throws Throwable
     */
    @Override
    public void visitDocumentCase(DocumentCase documentCase) throws Throwable {
        this.visitComposition(documentCase);
        wordDocument.createParagraph().createRun().addBreak(BreakType.PAGE);
    }

    @Override
    public void visitTitle(Title titleObj) throws Exception {
        handleCustomTextItem(titleObj, wordDocument.createParagraph());
    }

    @Override
    public void visitHeading(Heading headingObj) throws Exception {
        final var heading = headingObj.getText();
        final var depth = headingObj.getDepth();
        final var paragraph = wordDocument
            .createParagraph();
        paragraph.createRun().setText(heading);
    }

    @Override
    public void visitParagraph(Paragraph paragraphObj) throws Exception {
        handleCustomTextItem(paragraphObj, wordDocument.createParagraph());
    }

    @Override
    public void visitTable(Table tableObj) throws Throwable {
//        final var watch = new StopWatch();
//        watch.start();
        final var style =
            styleService
                .extractStyleFor(tableObj)
                .orElse(tableObj.getStyle());
        final var label = tableObj.getLabel();
        if (StringUtils.hasText(label)) {
            final var paragraph = wordDocument.createParagraph();
            final var run = paragraph.createRun();
            ((WordStyleService) styleService).convertStyleToElement(style, run, paragraph);
            run.setText(label);
        }
        createEmptyTable();
        if (tableObj.getTableHeaderRow().isPresent()) {
            this.visitTableHeaderRow(tableObj.getTableHeaderRow().get());
        }
        this.visitComposition(tableObj);
//        watch.stop();
//        log.info("Table visited in {} ms", watch.getTotalTimeMillis());
    }

    @Override
    public void visitTableHeaderRow(TableHeaderRow tableHeaderRowObj) throws Throwable {
        docxTable.insertNewTableRow(0);
        this.visitComposition(tableHeaderRowObj);
    }

    @Override
    public void visitTableHeaderCell(TableHeaderCell tableHeaderCellObj) throws Exception {
        final var headerRow = docxTable.getRow(0);
        final var cell = headerRow.createCell();
        handleCustomTextItem(tableHeaderCellObj, cell);
    }

    @Override
    public void visitTableRow(TableRow tableRowObj) throws Throwable {
        final var row = docxTable.createRow();
        row.getCtRow().setTcArray(new CTTc[0]);
        row.getTableCells().clear();
        styleService.extractStyleFor(tableRowObj);
        visitComposition(tableRowObj);
    }

    @Override
    public void visitTableCell(TableCell tableCellObj) throws Exception {
        final var row = docxTable.getRow(docxTable.getNumberOfRows() - 1);
        final var cell = row.createCell();
        handleCustomTextItem(tableCellObj, cell);
    }

    @Override
    public void visitSeparator(Separator separatorObj) {
        wordDocument
            .createParagraph()
            .createRun()
            .addBreak(BreakType.TEXT_WRAPPING);
    }

    @Override
    public void visitFooter(Footer footerObj) throws Exception {
        final var sectPr = wordDocument.getDocument().getBody().addNewSectPr();
        final var headerFooterPolicy = new XWPFHeaderFooterPolicy(wordDocument, sectPr);
        final var ctp = CTP.Factory.newInstance();
        final var paragraph = new XWPFParagraph(ctp, wordDocument);
        handleCustomTextItem(footerObj, paragraph);
        headerFooterPolicy
            .createFooter(STHdrFtr.DEFAULT, List.of(paragraph).toArray(XWPFParagraph[]::new));
    }

    public void handleCustomTextItem(TextItem<?> item, Object element) throws Exception {
        ((WordStyleService) styleService).handleCustomText(item, element);
    }

    public void createEmptyTable() {
        docxTable = wordDocument.createTable();
        docxTable.setTopBorder(XWPFTable.XWPFBorderType.NONE,0,0, "auto");
        docxTable.setLeftBorder(XWPFTable.XWPFBorderType.NONE,0,0, "auto");
        docxTable.setRightBorder(XWPFTable.XWPFBorderType.NONE,0,0, "auto");
        docxTable.setBottomBorder(XWPFTable.XWPFBorderType.NONE,0,0, "auto");
        docxTable.setInsideHBorder(XWPFTable.XWPFBorderType.NONE,0,0, "auto");
        docxTable.setInsideVBorder(XWPFTable.XWPFBorderType.NONE,0,0, "auto");
        docxTable.removeRow(0);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("wordDocument", wordDocument)
            .add("docxTable", docxTable)
            .add("fontCharset", fontCharset)
            .add("decimalFormat", decimalFormat)
            .add("styleService", styleService)
            .toString();
    }

    public XWPFDocument getWordDocument() {
        return wordDocument;
    }

    public WordFormatterVisitor setWordDocument(XWPFDocument wordDocument) {
        this.wordDocument = wordDocument;
        return this;
    }

    public FontCharset getFontCharset() {
        return fontCharset;
    }

    public WordFormatterVisitor setFontCharset(FontCharset fontCharset) {
        this.fontCharset = fontCharset;
        return this;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public WordFormatterVisitor setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
        return this;
    }

    @Override
    public StyleService getStyleService() throws Exception {
        if (styleService == null) {
            styleService = WordStyleService.create(fontCharset, decimalFormat);
        }
        return styleService;
    }

    public WordFormatterVisitor setStyleService(StyleService styleService) {
        this.styleService = styleService;
        return this;
    }
}
