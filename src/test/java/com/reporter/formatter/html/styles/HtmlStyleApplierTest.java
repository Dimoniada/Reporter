package com.reporter.formatter.html.styles;

import com.reporter.domain.styles.constants.Color;
import com.reporter.formatter.html.tag.Html4Font;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HtmlStyleApplierTest {
    private CssStyle cssStyle;

    @BeforeEach
    public void init() {
        cssStyle = new CssStyle();
    }

    @Test
    public void testHtml4FontAttributes() {
        final var font = new Html4Font();

        font.setSize(13)
            .setColor("#" + Color.GREEN.buildColorString())
            .setFace("monospace");

        Assertions.assertEquals(
            "color=\"#00FF00\" face=\"monospace\" size=\"13\"",
            font.attributesToHtmlString(true).trim()
        );
    }

    @Test
    public void testConvertCssPropertyFontSize() {

        cssStyle.setFontSize(12);

        Assertions.assertEquals("font-size:12pt", cssStyle.toCssStyleString());
    }

    @Test
    public void testConvertCssPropertyFontWeight() {

        cssStyle.setFontWeight("bold");

        Assertions.assertEquals("font-weight:bold", cssStyle.toCssStyleString());
    }

    @Test
    public void testConvertCssPropertyFontStyle() {

        cssStyle.setFontStyle("italic");

        Assertions.assertEquals("font-style:italic", cssStyle.toCssStyleString());
    }

    @Test
    public void testConvertCssPropertyFontColor() {

        cssStyle.setFontColor("#F0C35E");

        Assertions.assertEquals("color:#F0C35E", cssStyle.toCssStyleString());
    }

    @Test
    public void testConvertCssPropertyFontFamily() {

        cssStyle.setFontFamily("Tahoma");

        Assertions.assertEquals("font-family:Tahoma,monospace", cssStyle.toCssStyleString());
    }

    @Test
    public void testConvertCssPropertyTextAlign() {

        cssStyle.setTextAlign("right");

        Assertions.assertEquals("text-align:right", cssStyle.toCssStyleString());
    }

    @Test
    public void testConvertCssPropertyBorderCollapse() {

        cssStyle.setBorderCollapse("separate");

        Assertions.assertEquals("border-collapse:separate", cssStyle.toCssStyleString());
    }

    @Test
    public void testConvertCssPropertyBorderTop() {

        cssStyle.setBorderTop("2px solid");

        Assertions.assertEquals("border-top:2px solid", cssStyle.toCssStyleString());
    }

    @Test
    public void testConvertCssPropertyBorderLeft() {

        cssStyle.setBorderLeft("1px solid");

        Assertions.assertEquals("border-left:1px solid", cssStyle.toCssStyleString());
    }

    @Test
    public void testConvertCssPropertyBorderRight() {

        cssStyle.setBorderRight("3px solid");

        Assertions.assertEquals("border-right:3px solid", cssStyle.toCssStyleString());
    }

    @Test
    public void testConvertCssPropertyBorderBottom() {

        cssStyle.setBorderBottom("double");

        Assertions.assertEquals("border-bottom:double", cssStyle.toCssStyleString());
    }

    @Test
    public void testConvertCssPropertyBackgroundColor() {

        cssStyle.setBackgroundColor("#A36BCD");

        Assertions.assertEquals("background-color:#A36BCD", cssStyle.toCssStyleString());
    }

    @Test
    public void testConvertPropertyBorderHtml4() {

        cssStyle.setBorderHtml4(1);

        Assertions.assertEquals("border=\"1\"", cssStyle.toHtml4StyleString());
    }

    @Test
    public void testConvertPropertyCellspacingHtml4() {

        cssStyle.setCellspacingHtml4(5);

        Assertions.assertEquals("cellspacing=\"5\"", cssStyle.toHtml4StyleString());
    }

    @Test
    public void testConvertPropertyBgcolorHtml4() {

        cssStyle.setBgcolorHtml4("#123456");

        Assertions.assertEquals("bgcolor=\"#123456\"", cssStyle.toHtml4StyleString());
    }

    @Test
    public void testConvertPropertyBgcolorAndCellspacingHtml4() {

        cssStyle.setCellspacingHtml4(5);
        cssStyle.setBgcolorHtml4("#123456");

        Assertions.assertEquals("bgcolor=\"#123456\" cellspacing=\"5\"", cssStyle.toHtml4StyleString());
    }

    @Test
    public void testConvertPropertyAlignHtml4() {

        cssStyle.setAlignHtml4("left");

        Assertions.assertEquals("align=\"left\"", cssStyle.toHtml4StyleString());
    }

    @Test
    public void testConvertProperties() {

        cssStyle.setFontSize(12);
        cssStyle.setFontWeight("bold");
        cssStyle.setFontStyle("italic");
        cssStyle.setFontColor("#F0C35E");
        cssStyle.setFontFamily("Tahoma");

        cssStyle.setTextAlign("right");
        cssStyle.setBorderCollapse("separate");
        cssStyle.setBorderTop("2px solid");
        cssStyle.setBorderLeft("1px solid");
        cssStyle.setBorderRight("3px solid");
        cssStyle.setBorderBottom("double");
        cssStyle.setBackgroundColor("#A36BCD");

        cssStyle.setBorderHtml4(1);
        cssStyle.setCellspacingHtml4(5);
        cssStyle.setBgcolorHtml4("#123456");
        cssStyle.setAlignHtml4("left");

        Assertions.assertEquals("background-color:#A36BCD;" +
            "border-bottom:double;" +
            "border-collapse:separate;" +
            "border-left:1px solid;" +
            "border-right:3px solid;" +
            "border-top:2px solid;" +
            "color:#F0C35E;" +
            "font-family:Tahoma,monospace;" +
            "font-size:12pt;" +
            "font-style:italic;" +
            "font-weight:bold;" +
            "text-align:right", cssStyle.toCssStyleString());

        Assertions.assertEquals("align=\"left\" " +
            "bgcolor=\"#123456\" " +
            "border=\"1\" " +
            "cellspacing=\"5\"", cssStyle.toHtml4StyleString());
    }

}
