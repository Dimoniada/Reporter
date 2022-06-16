package com.reporter.formatter.word;

import com.reporter.formatter.FormatterContext;
import org.apache.poi.common.usermodel.fonts.FontCharset;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.util.Optional;

/**
 * Class for writing to .docx format
 */
@Component
public class DocxFormatter extends WordFormatter {
    private static final String EXTENSION = "docx";
    private static final MediaType MEDIA_TYPE =
        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    public DocxFormatter() {
        super(FontCharset.DEFAULT);
    }

    public DocxFormatter(FormatterContext context) {
        super(context);
    }

    public static DocxFormatter create() {
        return new DocxFormatter();
    }

    public static DocxFormatter create(FormatterContext context) {
        return new DocxFormatter(context);
    }

    @Override
    public XWPFDocument getWordDocument() {
        return Optional.ofNullable(wordDocument).orElse(new XWPFDocument());
    }

    @Override
    public String getExtension() {
        return EXTENSION;
    }

    @Override
    public MediaType getContentMediaType() {
        return MEDIA_TYPE;
    }
}
