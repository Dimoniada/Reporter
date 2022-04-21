package com.reporter.formatter.word.styles;

import com.google.common.base.MoreObjects;
import com.reporter.domain.Heading;
import com.reporter.domain.TextItem;
import com.reporter.domain.styles.*;
import com.reporter.domain.styles.constants.BorderWeight;
import com.reporter.domain.styles.constants.Color;
import com.reporter.domain.styles.constants.FillPattern;
import com.reporter.domain.styles.constants.HorAlignment;
import com.reporter.domain.styles.constants.VertAlignment;
import com.reporter.utils.LocalizedNumberUtils;
import org.apache.poi.common.usermodel.fonts.FontCharset;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class WordStyleService extends StyleService {
    /**
     * Reverse twip (1/567) constant for cm
     */
    private static final int XLSX_HEADING_CONST = 567;

    /**
     * Map of native xwpf border types.
     * Key - type BorderWeight, value - Border
     */
    private static final Map<BorderWeight, Borders>
        borderMap = new HashMap<>() {{
        put(BorderWeight.NONE, Borders.NONE);
        put(BorderWeight.THIN, Borders.THIN_THICK_MEDIUM_GAP);
        put(BorderWeight.MEDIUM, Borders.THICK_THIN_MEDIUM_GAP);
        put(BorderWeight.THICK, Borders.THICK);
        put(BorderWeight.DOUBLE, Borders.DOUBLE);
        put(BorderWeight.DOTTED, Borders.DOTTED);
        put(BorderWeight.DASHED, Borders.DASHED);
    }};

    /**
     * Map of native xwpf horizontal paragraph layout types
     * Key - HorAlignment type, value - ParagraphAlignment
     */
    private static final Map<HorAlignment, ParagraphAlignment>
        horizontalAlignmentMap = new HashMap<>() {{
        put(HorAlignment.GENERAL, ParagraphAlignment.BOTH);
        put(HorAlignment.LEFT, ParagraphAlignment.LEFT);
        put(HorAlignment.CENTER, ParagraphAlignment.CENTER);
        put(HorAlignment.RIGHT, ParagraphAlignment.RIGHT);
    }};

    /**
     * Map of native xwpf vertical text layout types
     * Key - type VertAlignment, value - TextAlignment
     */
    private static final Map<VertAlignment, TextAlignment>
        verticalAlignmentMap = new HashMap<>() {{
        put(VertAlignment.TOP, TextAlignment.TOP);
        put(VertAlignment.CENTER, TextAlignment.CENTER);
        put(VertAlignment.BOTTOM, TextAlignment.BOTTOM);
    }};

    /**
     * Map of native xwpf vertical text layout types
     * Key - type VertAlignment, value - XWPFTableCell.XWPFVertAlign
     */
    private static final Map<VertAlignment, XWPFTableCell.XWPFVertAlign>
        verticalAlignmentCellMap = new HashMap<>() {{
        put(VertAlignment.TOP, XWPFTableCell.XWPFVertAlign.TOP);
        put(VertAlignment.CENTER, XWPFTableCell.XWPFVertAlign.CENTER);
        put(VertAlignment.BOTTOM, XWPFTableCell.XWPFVertAlign.BOTTOM);
    }};

    private final FontCharset fontCharset;

    public WordStyleService(FontCharset fontCharset, DecimalFormat decimalFormat) {
        this.fontCharset = fontCharset;
        this.decimalFormat = decimalFormat;
    }

    public static StyleService create(FontCharset fontCharset, DecimalFormat decimalFormat) {
        return new WordStyleService(fontCharset, decimalFormat);
    }

    /**
     * Creates a stylized run in paragraph of docx elements:
     * <p>
     * text style applied to {@link org.apache.poi.xwpf.usermodel.XWPFRun}
     * layout style applied to {@link org.apache.poi.xwpf.usermodel.XWPFParagraph}
     *
     * @param item    text element
     * @param element docx object: XWPFParagraph or XWPFTableCell
     * @throws Exception on bad decimalFormat or font can't be found error
     */
    public void handleCustomText(TextItem<?> item, Object element) throws Exception {
        final var style = extractStyleFor(item).orElse(item.getStyle());
        final var text = LocalizedNumberUtils.applyDecimalFormat(item, decimalFormat);
        if (element instanceof XWPFParagraph) {
            final var paragraph = (XWPFParagraph) element;
            final var run = paragraph.createRun();
            run.setText(text);

            convertStyleToElement(style, run, paragraph);
            if (item instanceof Heading) {
                paragraph.setIndentationHanging(((Heading) item).getDepth() * XLSX_HEADING_CONST);
            }
        } else if (element instanceof XWPFTableCell) {
            final var cell = (XWPFTableCell) element;
            final var paragraph = cell.getParagraphs().get(0);
            final var run = paragraph.createRun();
            run.setText(text);
            if (style instanceof TextStyle) {
                convertStyleToElement(style, run, paragraph);
            } else if (style instanceof LayoutStyle) {
                final var layoutStyle = (LayoutStyle) style;
                convertLayoutStyleToCell(cell, layoutStyle);
            } else if (style instanceof LayoutTextStyle) {
                final var layoutTextStyle = (LayoutTextStyle) style;
                final var textStyle = layoutTextStyle.getTextStyle();
                final var layoutStyle = layoutTextStyle.getLayoutStyle();
                convertStyleToElement(textStyle, run, paragraph);
                convertLayoutStyleToCell(cell, layoutStyle);
            }
        }
    }

    /**
     * Converts style to native text style or element style
     *
     * @param style        input style
     * @param innerElement text element
     * @param outerElement element with text element
     */
    public void convertStyleToElement(Style style, XWPFRun innerElement, XWPFParagraph outerElement) {
        if (style instanceof TextStyle) {
            final var textStyle = (TextStyle) style;
            innerElement.setFontSize(textStyle.getFontSize());
            innerElement.setColor(toWordColor(textStyle.getColor()));
            if (StringUtils.hasText(textStyle.getFontNameResource())) {
                innerElement.setFontFamily(textStyle.getFontNameResource());
            }
            innerElement.setBold(textStyle.isBold());
            innerElement.setItalic(textStyle.isItalic());
            if (textStyle.getUnderline() != 0) {
                innerElement.setUnderline(UnderlinePatterns.SINGLE);
            }
        } else if (style instanceof LayoutStyle) {
            convertLayoutStyleToElement(outerElement, (LayoutStyle) style);
        } else if (style instanceof LayoutTextStyle) {
            final var layoutTextStyle = (LayoutTextStyle) style;
            final var textStyle = layoutTextStyle.getTextStyle();
            final var layoutStyle = layoutTextStyle.getLayoutStyle();
            convertStyleToElement(textStyle, innerElement, outerElement);
            convertStyleToElement(layoutStyle, innerElement, outerElement);
        }
    }

    /**
     * Decorates the native XWPFParagraph with a LayoutStyle
     *
     * @param element     decoration element
     * @param layoutStyle input LayoutStyle style
     */
    public void convertLayoutStyleToElement(XWPFParagraph element, LayoutStyle layoutStyle) {
        convertGroundColor(element, layoutStyle);
        convertBorders(element, layoutStyle);
        convertHorizontalAlignment(element, layoutStyle);
        convertVerticalAlignment(element, layoutStyle);
    }

    public void convertLayoutStyleToCell(XWPFTableCell cell, LayoutStyle layoutStyle) {
        cell.setColor(toWordColor(layoutStyle.getFillBackgroundColor()));
        convertCellBorders(cell, layoutStyle);
        final var paragraph = cell.getParagraphs().get(0);
        convertHorizontalAlignment(paragraph, layoutStyle);
        convertVerticalAlignmentCell(cell, layoutStyle);
        if (layoutStyle.getWidth() > 0) {
            final var width = layoutStyle.getWidth();
            cell.setWidthType(TableWidthType.DXA);
            cell.setWidth(String.valueOf(width));
        }
    }

    private static XWPFStyle createNewStyle(XWPFStyles styles, STStyleType.Enum styleType, String styleId) {
        if (styles == null || styleId == null) {
            return null;
        }
        var style = styles.getStyle(styleId);
        if (style == null) {
            final var ctStyle = CTStyle.Factory.newInstance();
            ctStyle.addNewName().setVal(styleId);
            ctStyle.setCustomStyle(STOnOff.GREATER_THAN);
            style = new XWPFStyle(ctStyle, styles);
            style.setType(styleType);
            style.setStyleId(styleId);
            styles.addStyle(style);
        }
        return style;
    }

    /**
     * Applies the layoutStyle backfill setting to the XWPFParagraph element
     *
     * @param element     XWPFParagraph element
     * @param layoutStyle input style
     */
    public static void convertGroundColor(XWPFParagraph element, LayoutStyle layoutStyle) {
        if (element.getCTP().getPPr() == null) {
            element.getCTP().addNewPPr();
        }
        final var ppr = element.getCTP().getPPr();
        if (ppr.getShd() != null) {
            ppr.unsetShd();
        }
        ppr.addNewShd();
        final var shd = ppr.getShd();
        shd.setVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd.CLEAR);
        shd.setColor("auto");
        shd.setFill(toWordColor(layoutStyle.getFillBackgroundColor()));
    }

    /**
     * Sets borders type and color for XWPFParagraph element
     *
     * @param paragraph  docx XWPFParagraph
     * @param layoutStyle input layout style
     */
    public static void convertBorders(XWPFParagraph paragraph, LayoutStyle layoutStyle) {
        final var borderTop = layoutStyle.getBorderTop();
        final var borderLeft = layoutStyle.getBorderLeft();
        final var borderRight = layoutStyle.getBorderRight();
        final var borderBottom = layoutStyle.getBorderBottom();
        final var ctp = paragraph.getCTP();
        if (ctp.getPPr() == null) {
            ctp.addNewPPr();
        }
        final var ppr = ctp.getPPr();
        if (ppr.getPBdr() == null) {
            ppr.addNewPBdr();
        }
        final var pbd = ppr.getPBdr();
        convertBorderTop(borderTop, paragraph, pbd);
        convertBorderLeft(borderLeft, paragraph, pbd);
        convertBorderRight(borderRight, paragraph, pbd);
        convertBorderBottom(borderBottom, paragraph, pbd);
    }

    /**
     * Sets borders type and color for XWPFTableCell element
     *
     * @param cell  docx XWPFTableCell
     * @param layoutStyle input layout style
     */
    public void convertCellBorders(XWPFTableCell cell, LayoutStyle layoutStyle) {
        final var borderTop = layoutStyle.getBorderTop();
        final var borderLeft = layoutStyle.getBorderLeft();
        final var borderRight = layoutStyle.getBorderRight();
        final var borderBottom = layoutStyle.getBorderBottom();

        final var ctTc = cell.getCTTc();
        final var tcPr = ctTc.addNewTcPr();
        final var border = tcPr.addNewTcBorders();

        final var top = border.addNewTop();
        final var left = border.addNewLeft();
        final var right = border.addNewRight();
        final var bottom = border.addNewBottom();

        top.setColor(toWordColor(borderTop.getColor()));
        top.setVal(STBorder.Enum.forInt(toWordBorder(borderTop.getWeight()).getValue()));
        left.setColor(toWordColor(borderLeft.getColor()));
        left.setVal(STBorder.Enum.forInt(toWordBorder(borderLeft.getWeight()).getValue()));
        right.setColor(toWordColor(borderRight.getColor()));
        right.setVal(STBorder.Enum.forInt(toWordBorder(borderRight.getWeight()).getValue()));
        bottom.setColor(toWordColor(borderBottom.getColor()));
        bottom.setVal(STBorder.Enum.forInt(toWordBorder(borderBottom.getWeight()).getValue()));
    }

    public static void convertBorderTop(BorderStyle borderStyle, XWPFParagraph element, CTPBdr pbd) {
        if (pbd.getTop() == null) {
            pbd.addNewTop();
        }
        pbd.getTop().setColor(toWordColor(borderStyle.getColor()));
        final var border = toWordBorder(borderStyle.getWeight());
        element.setBorderTop(border);
    }

    public static void convertBorderLeft(BorderStyle borderStyle, XWPFParagraph element, CTPBdr pbd) {
        if (pbd.getLeft() == null) {
            pbd.addNewLeft();
        }
        pbd.getLeft().setColor(toWordColor(borderStyle.getColor()));
        final var border = toWordBorder(borderStyle.getWeight());
        element.setBorderLeft(border);
    }

    public static void convertBorderRight(BorderStyle borderStyle, XWPFParagraph element, CTPBdr pbd) {
        if (pbd.getRight() == null) {
            pbd.addNewRight();
        }
        pbd.getRight().setColor(toWordColor(borderStyle.getColor()));
        final var border = toWordBorder(borderStyle.getWeight());
        element.setBorderRight(border);
    }

    public static void convertBorderBottom(BorderStyle borderStyle, XWPFParagraph element, CTPBdr pbd) {
        if (pbd.getBottom() == null) {
            pbd.addNewBottom();
        }
        pbd.getBottom().setColor(toWordColor(borderStyle.getColor()));
        final var border = toWordBorder(borderStyle.getWeight());
        element.setBorderBottom(border);
    }

    public static void convertHorizontalAlignment(XWPFParagraph element, LayoutStyle layoutStyle) {
        final var horAlignment = toWordHorAlignment(layoutStyle.getHorAlignment());
        element.setAlignment(horAlignment);
    }

    public static void convertVerticalAlignment(XWPFParagraph element, LayoutStyle layoutStyle) {
        final var vertAlignment = toWordVertAlignment(layoutStyle.getVertAlignment());
        element.setVerticalAlignment(vertAlignment);
    }

    public static void convertVerticalAlignmentCell(XWPFTableCell cell, LayoutStyle layoutStyle) {
        final var vertAlignmentCell = toWordVertAlignmentCell(layoutStyle.getVertAlignment());
        cell.setVerticalAlignment(vertAlignmentCell);
    }

    /**
     * Utility Methods
     **/
    public static Borders toWordBorder(BorderWeight border) {
        if (borderMap.containsKey(border)) {
            return borderMap.get(border);
        } else {
            throw new IllegalArgumentException("Undefined BorderWeight type");
        }
    }

    public static String toWordColor(Color color) {
        return color.buildColorString();
    }

    public static TextAlignment toWordVertAlignment(VertAlignment vertAlignment) {
        if (verticalAlignmentMap.containsKey(vertAlignment)) {
            return verticalAlignmentMap.get(vertAlignment);
        } else {
            throw new IllegalArgumentException("Undefined VerticalAlignment type");
        }
    }

    public static XWPFTableCell.XWPFVertAlign toWordVertAlignmentCell(VertAlignment vertAlignment) {
        if (verticalAlignmentCellMap.containsKey(vertAlignment)) {
            return verticalAlignmentCellMap.get(vertAlignment);
        } else {
            throw new IllegalArgumentException("Undefined VerticalAlignmentCell type");
        }
    }

    public static ParagraphAlignment toWordHorAlignment(HorAlignment horAlignment) {
        if (horizontalAlignmentMap.containsKey(horAlignment)) {
            return horizontalAlignmentMap.get(horAlignment);
        } else {
            throw new IllegalArgumentException("Undefined HorizontalAlignment type");
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("fontCharset", fontCharset)
            .add("parent", super.toString())
            .toString();
    }

    @Override
    public void writeStyles(Object o) {
        /*https://stackoverflow.com/questions/61251082/apache-poi-for-word-create-custom-style-with-textalignment*/
    }
}
