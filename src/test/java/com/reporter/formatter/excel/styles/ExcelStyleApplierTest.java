package com.reporter.formatter.excel.styles;

import com.reporter.domain.styles.BorderStyle;
import com.reporter.domain.styles.LayoutStyle;
import com.reporter.domain.styles.constants.BorderWeight;
import com.reporter.domain.styles.constants.Color;
import com.reporter.domain.styles.constants.FillPattern;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.reporter.formatter.excel.styles.ExcelStyleService.toExcelBorder;
import static com.reporter.formatter.excel.styles.ExcelStyleService.toExcelColor;
import static com.reporter.formatter.excel.styles.ExcelStyleService.toExcelFillPattern;
import static com.reporter.formatter.excel.styles.ExcelStyleService.toExcelHorAlignment;
import static com.reporter.formatter.excel.styles.ExcelStyleService.toExcelVertAlignment;

public class ExcelStyleApplierTest {

    private CellStyle cellStyle;

    private final LayoutStyle layoutStyle = LayoutStyle.create()
        .setBorderTop(BorderStyle.create(Color.BLACK, BorderWeight.DOUBLE))
        .setBorderLeft(BorderStyle.create(Color.WHITE, BorderWeight.MEDIUM))
        .setBorderRight(BorderStyle.create(Color.RED, BorderWeight.THICK))
        .setBorderBottom(BorderStyle.create(Color.PINK, BorderWeight.THIN))
        .setFillPattern(FillPattern.THIN_VERT_BANDS)
        .setFillForegroundColor(Color.WHITE)
        .setFillBackgroundColor(Color.WHITE)
        .setShrinkToFit(true);

    @BeforeEach
    public void init() {
        final HSSFWorkbook wb = new HSSFWorkbook();
        cellStyle = wb.createCellStyle();
    }

    @Test
    public void testConvertFit() {
        ExcelStyleService.convertFit(cellStyle, layoutStyle);
        Assertions.assertTrue(cellStyle.getShrinkToFit());
    }

    @Test
    public void testConvertVerticalAlignment() throws Exception {
        ExcelStyleService.convertVerticalAlignment(cellStyle, layoutStyle);
        Assertions.assertEquals(cellStyle.getVerticalAlignment(), toExcelVertAlignment(layoutStyle.getVertAlignment()));
    }

    @Test
    public void testConvertHorizontalAlignment() throws Exception {
        ExcelStyleService.convertHorizontalAlignment(cellStyle, layoutStyle);
        Assertions.assertEquals(cellStyle.getAlignment(), toExcelHorAlignment(layoutStyle.getHorAlignment()));
    }

    @Test
    public void testConvertBorders() throws Exception {
        ExcelStyleService.convertBorders(cellStyle, layoutStyle);

        Assertions.assertEquals(cellStyle.getBorderTop(), toExcelBorder(layoutStyle.getBorderTop().getWeight()));
        Assertions.assertEquals(cellStyle.getBorderLeft(), toExcelBorder(layoutStyle.getBorderLeft().getWeight()));
        Assertions.assertEquals(cellStyle.getBorderRight(), toExcelBorder(layoutStyle.getBorderRight().getWeight()));
        Assertions.assertEquals(cellStyle.getBorderBottom(), toExcelBorder(layoutStyle.getBorderBottom().getWeight()));
    }

    @Test
    public void testConvertBorderColors() throws Exception {
        ExcelStyleService.convertBorderColors(cellStyle, layoutStyle);

        Assertions.assertEquals(cellStyle.getTopBorderColor(),
                toExcelColor(layoutStyle.getBorderTop().getColor()));
        Assertions.assertEquals(cellStyle.getLeftBorderColor(),
                toExcelColor(layoutStyle.getBorderLeft().getColor()));
        Assertions.assertEquals(cellStyle.getRightBorderColor(),
                toExcelColor(layoutStyle.getBorderRight().getColor()));
        Assertions.assertEquals(cellStyle.getBottomBorderColor(),
                toExcelColor(layoutStyle.getBorderBottom().getColor()));
    }

    //Look https://stackoverflow.com/questions/38874115/
    //java-apache-poi-how-to-set-background-color-and-borders-at-same-time/46996790#46996790
    @Test
    public void testConvertGround() throws Exception {
        ExcelStyleService.convertGround(cellStyle, layoutStyle);

        Assertions.assertEquals(cellStyle.getFillBackgroundColor(), toExcelColor(layoutStyle.getFillBackgroundColor()));
        Assertions.assertEquals(cellStyle.getFillForegroundColor(), toExcelColor(layoutStyle.getFillForegroundColor()));
        Assertions.assertEquals(cellStyle.getFillPattern(), toExcelFillPattern(layoutStyle.getFillPattern()));
    }

}
