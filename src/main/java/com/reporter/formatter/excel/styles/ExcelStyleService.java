package com.reporter.formatter.excel.styles;

import com.google.common.base.MoreObjects;
import com.reporter.domain.TextItem;
import com.reporter.domain.styles.LayoutStyle;
import com.reporter.domain.styles.LayoutTextStyle;
import com.reporter.domain.styles.Style;
import com.reporter.domain.styles.StyleService;
import com.reporter.domain.styles.StyleUtils;
import com.reporter.domain.styles.TextStyle;
import com.reporter.domain.styles.constants.BorderWeight;
import com.reporter.domain.styles.constants.Color;
import com.reporter.domain.styles.constants.FillPattern;
import com.reporter.domain.styles.constants.HorAlignment;
import com.reporter.domain.styles.constants.VertAlignment;
import org.apache.poi.common.usermodel.fonts.FontCharset;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static com.reporter.utils.LocalizedNumberUtils.applyDecimalFormat;

/**
 * The class caches {@link Style} styles,
 * maps {@link Style} attributes to native,
 * implements methods of their application for
 * {@link org.apache.poi.ss.usermodel.Cell},
 * {@link org.apache.poi.ss.usermodel.Row}
 */
public class ExcelStyleService extends StyleService {
    private static final Map<BorderWeight, org.apache.poi.ss.usermodel.BorderStyle>
        borderMap = new HashMap<>() {{
        put(BorderWeight.NONE, org.apache.poi.ss.usermodel.BorderStyle.NONE);
        put(BorderWeight.THIN, org.apache.poi.ss.usermodel.BorderStyle.THIN);
        put(BorderWeight.MEDIUM, org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);
        put(BorderWeight.THICK, org.apache.poi.ss.usermodel.BorderStyle.THICK);
        put(BorderWeight.DOUBLE, org.apache.poi.ss.usermodel.BorderStyle.DOUBLE);
        put(BorderWeight.DOTTED, org.apache.poi.ss.usermodel.BorderStyle.DOTTED);
    }};

    private static final Map<HorAlignment, org.apache.poi.ss.usermodel.HorizontalAlignment>
        horizontalAlignmentMap = new HashMap<>() {{
        put(HorAlignment.GENERAL, org.apache.poi.ss.usermodel.HorizontalAlignment.GENERAL);
        put(HorAlignment.LEFT, org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT);
        put(HorAlignment.CENTER, org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
        put(HorAlignment.RIGHT, org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT);
    }};

    private static final Map<VertAlignment, org.apache.poi.ss.usermodel.VerticalAlignment>
        verticalAlignmentMap = new HashMap<>() {{
        put(VertAlignment.TOP, org.apache.poi.ss.usermodel.VerticalAlignment.TOP);
        put(VertAlignment.CENTER, org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
        put(VertAlignment.BOTTOM, org.apache.poi.ss.usermodel.VerticalAlignment.BOTTOM);
    }};

    private static final Map<FillPattern, org.apache.poi.ss.usermodel.FillPatternType>
        fillPatternMap = new HashMap<>() {{
        put(FillPattern.NO_FILL, org.apache.poi.ss.usermodel.FillPatternType.NO_FILL);
        put(FillPattern.FINE_DOTS, org.apache.poi.ss.usermodel.FillPatternType.FINE_DOTS);
        put(FillPattern.SOLID_FOREGROUND, org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        put(FillPattern.THIN_HORZ_BANDS, org.apache.poi.ss.usermodel.FillPatternType.THIN_HORZ_BANDS);
        put(FillPattern.THIN_VERT_BANDS, org.apache.poi.ss.usermodel.FillPatternType.THIN_VERT_BANDS);
        put(FillPattern.THIN_BACKWARD_DIAG, org.apache.poi.ss.usermodel.FillPatternType.THIN_BACKWARD_DIAG);
        put(FillPattern.THIN_FORWARD_DIAG, org.apache.poi.ss.usermodel.FillPatternType.THIN_FORWARD_DIAG);
    }};

    private static HSSFPalette palette;

    private final FontCharset fontCharset;
    private final Map<LayoutStyle, CellStyle> layoutStyles = new HashMap<>();
    private final Map<TextStyle, CellStyle> textStyles = new HashMap<>();
    private final Map<LayoutTextStyle, CellStyle> layoutTextStyles = new HashMap<>();
    protected Map<Cell, LayoutStyle> needAdjustHeaderCells = new HashMap<>();
    private Workbook workbook;

    public ExcelStyleService(FontCharset fontCharset, DecimalFormat decimalFormat) {
        this.fontCharset = fontCharset;
        this.decimalFormat = decimalFormat;
    }

    public static ExcelStyleService create(FontCharset fontCharset, DecimalFormat decimalFormat) {
        return new ExcelStyleService(fontCharset, decimalFormat);
    }

    public static ExcelStyleService create(FontCharset fontCharset) {
        return create(fontCharset, null);
    }

    public static ExcelStyleService create() {
        return create(FontCharset.DEFAULT, null);
    }

    /**
     * Utility Methods
     **/
    public static short toExcelColor(Color color) {
        if (palette == null) {
            try (var hwb = new HSSFWorkbook()) {
                palette = hwb.getCustomPalette();
            } catch (IOException e) {
                throw new IllegalArgumentException("Can't create HSSFWorkbook for color palette.");
            }
        }

        final HSSFColor excelColor = palette.findSimilarColor(color.getRed(), color.getGreen(), color.getBlue());
        return excelColor.getIndex();

    }

    public static org.apache.poi.ss.usermodel.BorderStyle
    toExcelBorder(BorderWeight border) {
        if (borderMap.containsKey(border)) {
            return borderMap.get(border);
        } else {
            throw new IllegalArgumentException("Undefined BorderWeight type");
        }
    }

    public static org.apache.poi.ss.usermodel.VerticalAlignment
    toExcelVertAlignment(VertAlignment vertAlignment) {
        if (verticalAlignmentMap.containsKey(vertAlignment)) {
            return verticalAlignmentMap.get(vertAlignment);
        } else {
            throw new IllegalArgumentException("Undefined VerticalAlignment type");
        }
    }

    public static org.apache.poi.ss.usermodel.HorizontalAlignment
    toExcelHorAlignment(HorAlignment horAlignment) {
        if (horizontalAlignmentMap.containsKey(horAlignment)) {
            return horizontalAlignmentMap.get(horAlignment);
        } else {
            throw new IllegalArgumentException("Undefined HorizontalAlignment type");
        }
    }

    public static org.apache.poi.ss.usermodel.FillPatternType
    toExcelFillPattern(FillPattern fillPattern) {
        if (fillPatternMap.containsKey(fillPattern)) {
            return fillPatternMap.get(fillPattern);
        } else {
            throw new IllegalArgumentException("Undefined FillPattern type");
        }
    }

    public static void convertFit(CellStyle cellStyle, LayoutStyle layoutStyle) {
        cellStyle.setShrinkToFit(layoutStyle.isShrinkToFit());
    }

    public static void applyWidth(org.apache.poi.ss.usermodel.Cell cell, LayoutStyle layoutStyle) {
        if (layoutStyle.isAutoWidth()) {
            cell.getSheet().autoSizeColumn(cell.getColumnIndex());
        } else if (layoutStyle.getWidth() > 0) {
            cell.getSheet().setColumnWidth(cell.getColumnIndex(), layoutStyle.getWidth());
        }
    }

    public static void convertVerticalAlignment(
        org.apache.poi.ss.usermodel.CellStyle cellStyle,
        LayoutStyle layoutStyle
    ) {
        cellStyle.setVerticalAlignment(toExcelVertAlignment(layoutStyle.getVertAlignment()));
    }

    public static void convertHorizontalAlignment(
        org.apache.poi.ss.usermodel.CellStyle cellStyle,
        LayoutStyle layoutStyle
    ) {
        cellStyle.setAlignment(toExcelHorAlignment(layoutStyle.getHorAlignment()));
    }

    public static void convertBorders(
        org.apache.poi.ss.usermodel.CellStyle cellStyle,
        LayoutStyle layoutStyle
    ) {
        cellStyle.setBorderTop(toExcelBorder(layoutStyle.getBorderTop().getWeight()));
        cellStyle.setBorderLeft(toExcelBorder(layoutStyle.getBorderLeft().getWeight()));
        cellStyle.setBorderRight(toExcelBorder(layoutStyle.getBorderRight().getWeight()));
        cellStyle.setBorderBottom(toExcelBorder(layoutStyle.getBorderBottom().getWeight()));
    }

    public static void convertBorderColors(
        org.apache.poi.ss.usermodel.CellStyle cellStyle,
        LayoutStyle layoutStyle
    ) {
        cellStyle.setTopBorderColor(toExcelColor(layoutStyle.getBorderTop().getColor()));
        cellStyle.setLeftBorderColor(toExcelColor(layoutStyle.getBorderLeft().getColor()));
        cellStyle.setRightBorderColor(toExcelColor(layoutStyle.getBorderRight().getColor()));
        cellStyle.setBottomBorderColor(toExcelColor(layoutStyle.getBorderBottom().getColor()));
    }

    //See the difference between Foreground and Background colors, the order and conditions for their filling
    // https://stackoverflow.com/questions/38874115/
    // java-apache-poi-how-to-set-background-color-and-borders-at-same-time/46996790#46996790
    public static void convertGround(
        org.apache.poi.ss.usermodel.CellStyle cellStyle,
        LayoutStyle layoutStyle
    ) {
        cellStyle.setFillForegroundColor(toExcelColor(layoutStyle.getFillForegroundColor()));
        cellStyle.setFillPattern(toExcelFillPattern(layoutStyle.getFillPattern()));
        cellStyle.setFillBackgroundColor(toExcelColor(layoutStyle.getFillBackgroundColor()));
    }

    public void adjustHeaderCells() {
        needAdjustHeaderCells.forEach(ExcelStyleService::applyWidth);
    }

    @Override
    public void writeStyles(Object o) {
        final Workbook workbook = (Workbook) o;
        this.workbook = workbook;
        final var style = workbook.createCellStyle();
        final var font = workbook.createFont();
        style.setFont(font);
        font.setCharSet(fontCharset.getNativeId());
        textStyles.put(null, style);
        layoutStyles.put(null, style);
    }

    public void fillCellFromItem(org.apache.poi.ss.usermodel.Cell cellObj, TextItem<?> item)
        throws ParseException {
        final var cell = applyFontCharsetAndDecimalFormat(item, cellObj);
        final var style = extractStyleFor(item).orElse(item.getStyle());
        if (style instanceof TextStyle) {
            convertTextStyleToCell(cell, (TextStyle) style);
        } else if (style instanceof LayoutStyle) {
            convertLayoutStyleToCell(cell, (LayoutStyle) style);
        } else if (style instanceof LayoutTextStyle) {
            convertLayoutTextStyleToCell(cell, (LayoutTextStyle) style);
        }
    }

    /**
     * Generic method for styling cells in a table
     *
     * @param tableCustomCell TableCell or TableHeaderCell
     * @param cell            native cell
     * @throws ParseException when parsing a value in a cell
     */
    public void handleTableCustomCell(TextItem<?> tableCustomCell, org.apache.poi.ss.usermodel.Cell cell)
        throws Exception {
        applyFontCharsetAndDecimalFormat(tableCustomCell, cell);
        final var optStyle = extractStyleFor(tableCustomCell);
        var style = tableCustomCell.getStyle();

        if (optStyle.isPresent()) {
            if (style == null) {
                style = optStyle.get();
            } else {
                StyleUtils.joinWith(optStyle.get(), style);
            }
        }

        if (style instanceof TextStyle) {
            convertTextStyleToCell(cell, (TextStyle) style);
        } else if (style instanceof LayoutStyle) {
            convertLayoutStyleToCell(cell, (LayoutStyle) style);
        } else if (style instanceof LayoutTextStyle) {
            convertLayoutTextStyleToCell(cell, (LayoutTextStyle) style);
        }
    }

    public Cell applyFontCharsetAndDecimalFormat(
        TextItem<?> tableCustomCell,
        org.apache.poi.ss.usermodel.Cell cell
    )
        throws ParseException {
        workbook.getFontAt(cell.getCellStyle().getFontIndex()).setCharSet(fontCharset.getNativeId());

        if (StringUtils.hasText(tableCustomCell.getText())) {
            cell.setCellValue(applyDecimalFormat(tableCustomCell, decimalFormat));
        } else {
            cell.setCellValue("");
        }
        return cell;
    }

    public void convertTextStyleToCell(Cell cell, TextStyle textStyle) {
        final var wb = cell.getSheet().getWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        final Font font;
        if (textStyles.containsKey(textStyle)) {
            cellStyle = textStyles.get(textStyle);
        } else {
            font = createFontFromTextStyle(textStyle, wb);
            cellStyle.setFont(font);
        }
        cell.setCellStyle(cellStyle);
    }

    public void convertLayoutStyleToCell(Cell cell, LayoutStyle layoutStyle) {
        final CellStyle cellStyle;

        if (layoutStyles.containsKey(layoutStyle)) {
            cellStyle = layoutStyles.get(layoutStyle);
        } else {
            cellStyle = cell.getSheet().getWorkbook().createCellStyle();
            convertGround(cellStyle, layoutStyle);
            convertBorders(cellStyle, layoutStyle);
            convertBorderColors(cellStyle, layoutStyle);
            convertHorizontalAlignment(cellStyle, layoutStyle);
            convertVerticalAlignment(cellStyle, layoutStyle);
            convertFit(cellStyle, layoutStyle);
            layoutStyles.put(layoutStyle, cellStyle);
        }
        cell.setCellStyle(cellStyle);
        applyWidth(cell, layoutStyle);
    }

    public void convertLayoutTextStyleToCell(Cell cell, LayoutTextStyle layoutTextStyle) {
        final CellStyle cellStyle;
        final var wb = cell.getSheet().getWorkbook();
        final var textStyle = layoutTextStyle.getTextStyle();
        final var layoutStyle = layoutTextStyle.getLayoutStyle();

        if (layoutTextStyles.containsKey(layoutTextStyle)) {
            cellStyle = layoutTextStyles.get(layoutTextStyle);
        } else {
            cellStyle = wb.createCellStyle();
            final Font font = createFontFromTextStyle(textStyle, wb);
            cellStyle.setFont(font);

            convertGround(cellStyle, layoutStyle);
            convertBorders(cellStyle, layoutStyle);
            convertBorderColors(cellStyle, layoutStyle);
            convertHorizontalAlignment(cellStyle, layoutStyle);
            convertVerticalAlignment(cellStyle, layoutStyle);
            convertFit(cellStyle, layoutStyle);
            layoutTextStyles.put(layoutTextStyle, cellStyle);
        }
        cell.setCellStyle(cellStyle);
        applyWidth(cell, layoutStyle);
    }

    public Font createFontFromTextStyle(TextStyle textStyle, Workbook wb) {
        final Font font;

        font = wb.createFont();
        final var fontName = textStyle.getFontNameResource();
        if (StringUtils.hasText(fontName)) {
            font.setFontName(fontName);
        }
        font.setBold(textStyle.isBold());
        font.setColor(toExcelColor(textStyle.getColor()));
        font.setFontHeightInPoints(textStyle.getFontSize());
        font.setItalic(textStyle.isItalic());
        font.setUnderline(textStyle.getUnderline());

        return font;
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("workbook", workbook)
                .add("fontCharset", fontCharset)
                .add("layoutStyles", layoutStyles)
                .add("textStyles", textStyles)
                .add("layoutTextStyles", layoutTextStyles)
                .add("parent", super.toString())
                .toString();
    }

    public Map<Cell, LayoutStyle> getNeedAdjustHeaderCells() {
        return needAdjustHeaderCells;
    }

    public ExcelStyleService setNeedAdjustHeaderCells(Map<Cell, LayoutStyle> needAdjustHeaderCells) {
        this.needAdjustHeaderCells = needAdjustHeaderCells;
        return this;
    }
}
