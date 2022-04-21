package com.reporter.formatter.excel;

import com.reporter.formatter.FormatterContext;
import org.springframework.http.MediaType;
import org.apache.poi.common.usermodel.fonts.FontCharset;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Class for writing to .xls format
 */
@Component
public class XlsFormatter extends ExcelFormatter {
    private static final String EXTENSION = "xls";
    private static final MediaType MEDIA_TYPE = MediaType.parseMediaType("application/vnd.ms-excel");

    public XlsFormatter() {
        super(FontCharset.DEFAULT);
    }

    public XlsFormatter(FormatterContext context) {
        super(context);
    }

    public static XlsFormatter create() {
        return new XlsFormatter();
    }

    public static XlsFormatter create(FormatterContext context) {
        return new XlsFormatter(context);
    }

    @Override
    public Workbook getWorkbook() {
        return Optional.ofNullable(workbook).orElse(new HSSFWorkbook());
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
