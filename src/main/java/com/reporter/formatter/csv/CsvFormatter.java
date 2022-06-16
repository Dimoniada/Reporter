package com.reporter.formatter.csv;

import com.reporter.domain.Document;
import com.reporter.formatter.FormatterContext;
import com.google.common.base.MoreObjects;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;
import org.supercsv.prefs.CsvPreference;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Custom class for saving {@link Document} document in CSV format.
 * {@link CsvFormatter#handle handle(doc)} - store result in
 * resource {@link CsvFormatter#resource} that is created
 * from the passed filename or generated automatically.
 * If output stream {@link CsvFormatter#outputStream} is set, then
 * writes to it.
 * {@link CsvFormatter#handle handle(doc)} must be called
 * after {@link CsvFormatter#setResource(WritableResource) setResource(res)}
 */
@Component
public class CsvFormatter extends CsvFormatterVisitor {
    protected List<WritableResource> attachments;

    public CsvFormatter() {
        this(StandardCharsets.UTF_8.name());
    }

    public CsvFormatter(String encoding) {
        this.encoding = encoding;
    }

    public CsvFormatter(CsvPreference csvPreference) {
        this.setCsvPreference(csvPreference);
    }

    public CsvFormatter(FormatterContext context) {
        this.encoding = context.getEncoding();
        this.decimalFormat = context.getDecimalFormat();
        this.csvPreference = context.createCsvPreference();
    }

    public static CsvFormatter create() {
        return new CsvFormatter(StandardCharsets.UTF_8.name());
    }

    public static CsvFormatter create(String encoding) {
        return new CsvFormatter(encoding);
    }

    public static CsvFormatter create(CsvPreference csvPreference) {
        return new CsvFormatter(csvPreference);
    }

    public static CsvFormatter create(FormatterContext context) {
        return new CsvFormatter(context);
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("attachments", attachments)
                .add("resource", resource)
                .add("fileName", fileName)
                .add("parent", super.toString())
                .toString();
    }

    public List<WritableResource> getAttachments() {
        return attachments;
    }

    public CsvFormatter setAttachments(List<WritableResource> attachments) {
        this.attachments = attachments;
        return this;
    }
}
