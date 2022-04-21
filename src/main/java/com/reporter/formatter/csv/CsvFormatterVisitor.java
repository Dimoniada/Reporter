package com.reporter.formatter.csv;

import com.reporter.domain.Document;
import com.reporter.domain.Footer;
import com.reporter.domain.Heading;
import com.reporter.domain.Paragraph;
import com.reporter.domain.Separator;
import com.reporter.domain.Table;
import com.reporter.domain.TableHeaderRow;
import com.reporter.domain.TableRow;
import com.reporter.domain.Title;
import com.reporter.domain.styles.StyleService;
import com.reporter.formatter.Formatter;
import com.google.common.base.MoreObjects;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.reporter.utils.LocalizedNumberUtils.applyDecimalFormat;

/**
 * The class generates a csv representation of the document {@link Document}
 */
public class CsvFormatterVisitor extends Formatter {
    private static final String EXTENSION = "csv";
    private static final MediaType MEDIA_TYPE = MediaType.parseMediaType("text/csv");
    protected OutputStreamWriter writer;
    protected CsvListWriter csvWriter;

    protected String encoding;
    protected DecimalFormat decimalFormat;

    protected CsvPreference csvPreference = CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE;

    @Override
    public String getExtension() {
        return EXTENSION;
    }

    @Override
    public MediaType getContentMediaType() {
        return MEDIA_TYPE;
    }

    @Override
    public StyleService getStyleService() {
        return null;
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

        if (!Charset.isSupported(encoding)) {
            throw new IllegalArgumentException(
                String.format(
                    "Unsupported character set %s in the current Java virtual machine",
                    encoding)
            );
        }
        writer = new OutputStreamWriter(outputStream, encoding);
        csvWriter = new CsvListWriter(writer, csvPreference);
        this.visitComposition(documentObj);
        csvWriter.close();
    }

    @Override
    public void visitTitle(Title titleObj) throws Exception {
        csvWriter.write(applyDecimalFormat(titleObj, decimalFormat));
    }

    @Override
    public void visitHeading(Heading headingObj) throws Exception {
        csvWriter.write(applyDecimalFormat(headingObj, decimalFormat));
    }

    @Override
    public void visitParagraph(Paragraph paragraphObj) throws Exception {
        csvWriter.write(applyDecimalFormat(paragraphObj, decimalFormat));
    }

    @Override
    public void visitTable(Table tableObj) throws Throwable {
        if (tableObj.getTableHeaderRow().isPresent()) {
            this.visitTableHeaderRow(tableObj.getTableHeaderRow().get());
        }
        this.visitComposition(tableObj);
    }

    @Override
    public void visitTableHeaderRow(TableHeaderRow tableHeaderRowObj) throws Throwable {
        final var tableRow = new ArrayList<String>();
        for (final var o : tableHeaderRowObj.getParts()) {
            tableRow.add(applyDecimalFormat(o, decimalFormat));
        }
        csvWriter.write(tableRow);
    }

    @Override
    public void visitTableRow(TableRow tableRowObj) throws Throwable {
        final var tableRow = new ArrayList<String>();
        for (final var o : tableRowObj.getParts()) {
            tableRow.add(applyDecimalFormat(o, decimalFormat));
        }
        csvWriter.write(tableRow);
    }

    @Override
    public void visitSeparator(Separator separatorObj) throws Exception {
        csvWriter.write("");
    }

    @Override
    public void visitFooter(Footer footerObj) throws Exception {
        csvWriter.write("");
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("writer", writer)
                .add("csvWriter", csvWriter)
                .add("os", outputStream)
                .add("encoding", encoding)
                .add("decimalFormat", decimalFormat)
                .add("csvPreference", csvPreference)
                .add("parent", super.toString())
                .toString();
    }

    public OutputStreamWriter getWriter() {
        return writer;
    }

    public OutputStream getOs() {
        return outputStream;
    }

    public CsvListWriter getCsvWriter() {
        return csvWriter;
    }

    public String getEncoding() {
        return encoding;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public CsvPreference getCsvPreference() {
        return csvPreference;
    }

    public CsvFormatterVisitor setWriter(OutputStreamWriter writer) {
        this.writer = writer;
        return this;
    }

    public CsvFormatterVisitor setOs(OutputStream os) {
        this.outputStream = os;
        return this;
    }

    public CsvFormatterVisitor setCsvWriter(CsvListWriter csvWriter) {
        this.csvWriter = csvWriter;
        return this;
    }

    public CsvFormatterVisitor setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public CsvFormatterVisitor setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
        return this;
    }

    public CsvFormatterVisitor setCsvPreference(CsvPreference csvPreference) {
        this.csvPreference = csvPreference;
        return this;
    }
}
