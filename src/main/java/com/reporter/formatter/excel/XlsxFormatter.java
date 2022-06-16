package com.reporter.formatter.excel;

import com.reporter.formatter.FormatterContext;
import org.springframework.http.MediaType;
import org.apache.poi.common.usermodel.fonts.FontCharset;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Class for writing to .xlsx format
 */
@Component
public class XlsxFormatter extends ExcelFormatter {
    private static final String EXTENSION = "xlsx";
    private static final MediaType MEDIA_TYPE =
        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    public XlsxFormatter() {
        super(FontCharset.DEFAULT);
    }

    public XlsxFormatter(FormatterContext context) {
        super(context);
    }

    public static XlsxFormatter create() {
        return new XlsxFormatter();
    }

    public static XlsxFormatter create(FormatterContext context) {
        return new XlsxFormatter(context);
    }

    @Override
    public Workbook getWorkbook() {
        return Optional.ofNullable(workbook).orElse(new XSSFWorkbook());
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
