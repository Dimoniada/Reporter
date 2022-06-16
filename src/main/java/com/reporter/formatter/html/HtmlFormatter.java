package com.reporter.formatter.html;

import com.reporter.domain.Document;
import com.reporter.formatter.FormatterContext;
import com.google.common.base.MoreObjects;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * Custom class for saving {@link Document} document in HTML format.
 * {@link HtmlFormatter#handle handle(doc)} stores the result in
 * resource {@link HtmlFormatter#resource} that is created
 * from the passed filename or generated automatically.
 * If output stream {@link HtmlFormatter#outputStream} is set, then
 * writes to it.
 * {@link HtmlFormatter#handle handle(doc)} must be called
 * after {@link HtmlFormatter#setResource(WritableResource)}
 */
@Component
public class HtmlFormatter extends HtmlFormatterVisitor {
    protected List<WritableResource> attachments;

    public HtmlFormatter() {
        /**/
    }

    public HtmlFormatter(String encoding) {
        this.encoding = encoding;
    }

    public HtmlFormatter(String encoding, Locale locale) {
        this.encoding = encoding;
        this.locale = locale;
    }

    public HtmlFormatter(FormatterContext context) {
        this.encoding = context.getEncoding();
        this.locale = context.getLocale();
    }

    public static HtmlFormatter create() {
        return new HtmlFormatter(StandardCharsets.UTF_8.name());
    }

    public static HtmlFormatter create(String encoding, Locale locale) {
        return new HtmlFormatter(encoding, locale);
    }

    public static HtmlFormatter create(FormatterContext context) {
        return new HtmlFormatter(context);
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

    public HtmlFormatter setAttachments(List<WritableResource> attachments) {
        this.attachments = attachments;
        return this;
    }
}
