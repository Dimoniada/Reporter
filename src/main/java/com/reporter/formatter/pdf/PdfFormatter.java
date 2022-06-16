package com.reporter.formatter.pdf;

import com.reporter.domain.Document;
import com.reporter.formatter.FormatterContext;
import com.google.common.base.MoreObjects;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;

import java.awt.FontFormatException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * Custom class for saving {@link Document} to PDF.
 * {@link PdfFormatter#handle handle(doc)} - store result in
 * resource {@link PdfFormatter#resource} that is created
 * from the passed filename or generated automatically.
 * If output stream {@link PdfFormatter#outputStream} is set, then
 * writes to it.
 * {@link PdfFormatter#handle handle(doc)} must be called
 * after {@link PdfFormatter#setResource(WritableResource)}
 */
@Component
public class PdfFormatter extends PdfFormatterVisitor {

    protected List<WritableResource> attachments;

    public PdfFormatter() {
        this.encoding = StandardCharsets.UTF_8.name();
    }

    public PdfFormatter(String encoding) {
        this.encoding = encoding;
    }

    public PdfFormatter(FormatterContext context) throws IOException, FontFormatException {
        this.encoding = context.getEncoding();
        this.decimalFormat = context.getDecimalFormat();
        if (this.styleService != null) {
            this.styleService.setFontService(context.createFontService().initializeFonts());
        }
    }

    public static PdfFormatter create() {
        return new PdfFormatter();
    }

    public static PdfFormatter create(String encoding) {
        return new PdfFormatter(encoding);
    }

    public static PdfFormatter create(FormatterContext context) throws IOException, FontFormatException {
        return new PdfFormatter(context);
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("attachments", attachments)
                .add("resource", resource)
                .add("parent", super.toString())
                .toString();
    }

    public List<WritableResource> getAttachments() {
        return attachments;
    }

    public PdfFormatter setAttachments(List<WritableResource> attachments) {
        this.attachments = attachments;
        return this;
    }

}
