package com.reporter.formatter.excel;

import com.reporter.formatter.BaseDocument;
import com.reporter.formatter.DocumentHolder;
import com.reporter.formatter.excel.styles.ExcelStyleService;
import org.apache.poi.common.usermodel.fonts.FontCharset;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

class ExcelFormatterTest extends BaseDocument {

    public static final String check = "Title 1,paragraph 1,column1,column2 (столбец2),1,000,2,000,3,000,4,000," +
        "5,000,6,000,Test document v.1,,,Chapter 1,,,,,This is an example of text in paragraph,Column 1,Column 2," +
        "Cell 1.1,Cell 1.2,Cell 2.1,Cell 2.2,Cell 3.1,Cell 3.2,Cell 4.1,Cell 4.2,,Chapter 2,,,,,This is an example" +
        " of text in paragraph 2,Column 1,Column 2,Cell 1.1,Cell 1.2,Cell 2.1,Cell 2.2,Title 1,paragraph 1,,," +
        "столбец1,column2,1,000,2,000,3,000,4 and some escape characters (символы) %;;;;;\\/,5,000,6,000";

    @BeforeEach
    public void initStyledDoc() throws Exception {
        super.initDoc();
    }

    /**
     * Test {@link XlsFormatter#handle handle} call
     * and proper saving texts in "doc1.xls"
     *
     * @throws Throwable Exception/IOException
     */
    @Test
    public void testSaveTextToXlsxFile() throws Throwable {

        final var xlsxFormatter = XlsxFormatter.create();
        final var documentHolder = xlsxFormatter.handle(doc);

        final var checked = new ArrayList<String>();

        final Workbook wb = WorkbookFactory.create(documentHolder.getResource().getFile());
        final Sheet sheet = wb.getSheetAt(0);
        sheet.rowIterator().forEachRemaining(r -> {
            Cell cell = r.getCell(0);
            if (cell != null) {
                checked.add(cell.getStringCellValue());
            }
            cell = r.getCell(1);
            if (cell != null) {
                checked.add(cell.getStringCellValue());
            }
        });
        wb.close();

        Assertions.assertEquals(check, String.join(",", checked));

        documentHolder.close();
    }

    /**
     * Test {@link XlsxFormatter#handle handle} call
     * and proper saving table in "doc1.xls",
     * check cell value in saved table on some position
     *
     * @throws Throwable Exception/IOException
     */
    @Test
    public void testSaveTableToXlsxFile() throws Throwable {

        final var styleService = ExcelStyleService.create(FontCharset.DEFAULT, null);

        final XlsxFormatter xlsxFormatter = XlsxFormatter.create().setStyleService(styleService);
        final var documentHolder = xlsxFormatter.handle(doc);

        final Workbook wb = WorkbookFactory.create(documentHolder.getResource().getFile());
        final Sheet sheet = wb.getSheetAt(0);
        final String check = sheet.getRow(15).getCell(1).getStringCellValue();
        wb.close();

        documentHolder.close();

        Assertions.assertEquals("Cell 3.2", check);
    }

    /**
     * Tests some properties of {@link XlsFormatter}
     *
     * @throws Throwable when Document.handle() fails
     */
    @Test
    public void testFormatterProperties() throws Throwable {
        final var os = new ByteArrayOutputStream();
        final XlsFormatter xlsFormatter = XlsFormatter.create();

        final DecimalFormat df = new DecimalFormat("\u203000");

        final DocumentHolder documentHolder = xlsFormatter.handle(doc);

        final Workbook wb = WorkbookFactory.create(documentHolder.getResource().getFile());

        xlsFormatter
            .setFontCharset(FontCharset.JOHAB)
            .setWorkbook(wb)
            .setOutputStream(os)
            .getStyleService()
            .setDecimalFormat(df);

        Assertions.assertEquals(os, xlsFormatter.getOutputStream());
        Assertions.assertEquals(wb, xlsFormatter.getWorkbook());
        Assertions.assertEquals(FontCharset.JOHAB, xlsFormatter.getFontCharset());
        Assertions.assertEquals(df, xlsFormatter.getStyleService().getDecimalFormat());

        wb.close();
        documentHolder.close();
    }

}
