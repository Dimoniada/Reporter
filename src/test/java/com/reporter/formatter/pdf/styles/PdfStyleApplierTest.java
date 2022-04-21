package com.reporter.formatter.pdf.styles;

import com.reporter.domain.styles.BorderStyle;
import com.reporter.domain.styles.LayoutStyle;
import com.reporter.domain.styles.constants.BorderWeight;
import com.reporter.domain.styles.constants.Color;
import com.reporter.domain.styles.constants.HorAlignment;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.Background;
import com.itextpdf.layout.properties.Property;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PdfStyleApplierTest {
    private final Text textItem = new Text("");
    private final SolidBorder border = new SolidBorder(1);

    private final LayoutStyle layoutStyle = LayoutStyle.create()
        .setBorderTop(BorderStyle.create(Color.BLACK, BorderWeight.DOUBLE))
        .setBorderLeft(BorderStyle.create(Color.WHITE, BorderWeight.MEDIUM))
        .setBorderRight(BorderStyle.create(Color.RED, BorderWeight.THICK))
        .setBorderBottom(BorderStyle.create(Color.PINK, BorderWeight.THIN))
        .setFillBackgroundColor(Color.TEAL)
        .setHorAlignment(HorAlignment.RIGHT);


    @Test
    public void testConvertGroundColor() {
        PdfStyleService.convertGroundColor(textItem, layoutStyle);

        Assertions.assertEquals(PdfStyleService.toPdfColor(Color.TEAL),
                ((Background) textItem.getProperty(Property.BACKGROUND)).getColor());
    }

    @Test
    public void testConvertHorizontalAlignment() {
        PdfStyleService.convertHorizontalAlignment(textItem, layoutStyle);

        Assertions.assertEquals(PdfStyleService.toPdfHorAlignment(HorAlignment.RIGHT),
                textItem.getProperty(Property.TEXT_ALIGNMENT));
    }

    @Test
    public void testConvertBorderColor() {
        PdfStyleService.convertBorderColor(border, Color.PINK);

        Assertions.assertEquals(PdfStyleService.toPdfColor(Color.PINK), border.getColor());
    }

    @Test
    public void testConvertBorder() {
        PdfStyleService.convertBorder(layoutStyle.getBorderLeft(), textItem::setBorderLeft, false);

        Assertions.assertEquals(PdfStyleService.toPdfBorder(BorderWeight.MEDIUM),
                textItem.getProperty(Property.BORDER_LEFT));
    }

}
