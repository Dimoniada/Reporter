package com.reporter.formatter;

import com.reporter.domain.TableCell;
import com.reporter.domain.TableHeaderCell;
import com.reporter.domain.styles.BorderStyle;
import com.reporter.domain.styles.FontFamilyStyle;
import com.reporter.domain.styles.LayoutTextStyle;
import com.reporter.domain.styles.TextStyle;
import com.reporter.domain.styles.constants.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Objects;

class FormatterContextTest extends BaseFormatterContext {

    @BeforeEach
    public void init() {
        super.initFormatterContext();
    }

    @Test
    void testCreateFormatterContext() {
        Assertions.assertEquals(encoding, formatterContext.getEncoding());
        Assertions.assertEquals(locale, formatterContext.getLocale());
        Assertions.assertEquals(timeZone, formatterContext.getTimeZone());
        Assertions.assertEquals(decimalFormat, formatterContext.getDecimalFormat());
        Assertions.assertEquals(csvDelimiter, formatterContext.getCsvDelimiter());
    }

    @Test
    void testCreateCsvPreference() {
        final var csvPreference = formatterContext.createCsvPreference();
        Assertions.assertEquals(csvDelimiter.charValue(), csvPreference.getDelimiterChar());
        Assertions.assertEquals(FormatterContext.CSV_QUOTE, csvPreference.getQuoteChar());
        Assertions.assertEquals(FormatterContext.CSV_END_OF_LINE, csvPreference.getEndOfLineSymbols());
    }

    @Test
    void testCreateFormattedZoneDateTime() {
        final var formattedZoneDateTime = formatterContext.createFormattedZoneDateTime();
        final var formatPattern = DateTimeFormatterBuilder
            .getLocalizedDateTimePattern(
                FormatStyle.LONG,
                FormatStyle.LONG,
                IsoChronology.INSTANCE,
                locale
            );
        final var formatter = DateTimeFormatter.ofPattern(formatPattern, locale);
        Assertions.assertEquals(
            ZonedDateTime.now(timeZone.toZoneId()).getMinute(),
            ZonedDateTime.parse(formattedZoneDateTime, formatter).getMinute()
        );
    }

    @Test
    void testCreateFontService() {
        Assertions.assertDoesNotThrow(() -> {
            final var fontService = formatterContext.createFontService();
            Assertions.assertTrue(
                fontService
                    .getFonts()
                    .values()
                    .stream()
                    .anyMatch(Objects::nonNull)
            );
        });
    }

    @Test
    void testCreateHeaderCellStyle() {
        final var style = formatterContext.createHeaderCellStyle();
        Assertions.assertEquals(LayoutTextStyle.class, style.getClass());

        final var condition = style.getCondition();
        Assertions.assertEquals(TableHeaderCell.class, condition.getClazz());

        final var textStyle = ((LayoutTextStyle) style).getTextStyle();
        Assertions.assertEquals(FontFamilyStyle.SANS_SERIF, textStyle.getFontFamilyStyle());
        Assertions.assertEquals(FormatterContext.STYLE_BIG_FONT_SIZE, textStyle.getFontSize());
        Assertions.assertTrue(textStyle.isBold());

        final var layoutStyle = ((LayoutTextStyle) style).getLayoutStyle();
        final var borderHeader =
            BorderStyle.create(Color.GREY_50_PERCENT, BorderWeight.DOUBLE);
        Assertions.assertEquals(HorAlignment.LEFT, layoutStyle.getHorAlignment());
        Assertions.assertEquals(VertAlignment.CENTER, layoutStyle.getVertAlignment());
        Assertions.assertEquals(FillPattern.SOLID_FOREGROUND, layoutStyle.getFillPattern());
        Assertions.assertEquals(borderHeader, layoutStyle.getBorderTop());
        Assertions.assertEquals(borderHeader, layoutStyle.getBorderLeft());
        Assertions.assertEquals(borderHeader, layoutStyle.getBorderRight());
        Assertions.assertEquals(borderHeader, layoutStyle.getBorderBottom());
    }

    @Test
    void testCreateRowStyleInterlinear() {
        final var style = formatterContext.createRowStyleInterlinear();
        Assertions.assertEquals(LayoutTextStyle.class, style.getClass());

        final var condition = style.getCondition();
        Assertions.assertEquals(TableCell.class, condition.getClazz());

        final var textStyle = ((LayoutTextStyle) style).getTextStyle();
        Assertions.assertEquals(FontFamilyStyle.SERIF, textStyle.getFontFamilyStyle());
        Assertions.assertEquals(FormatterContext.STYLE_NORMAL_FONT_SIZE, textStyle.getFontSize());

        final var layoutStyle = ((LayoutTextStyle) style).getLayoutStyle();
        final var borderHeader =
            BorderStyle.create(Color.GREY_50_PERCENT, BorderWeight.THIN);
        Assertions.assertEquals(HorAlignment.LEFT, layoutStyle.getHorAlignment());
        Assertions.assertEquals(VertAlignment.CENTER, layoutStyle.getVertAlignment());
        Assertions.assertEquals(FillPattern.SOLID_FOREGROUND, layoutStyle.getFillPattern());
        Assertions.assertEquals(Color.GREEN_LIGHT, layoutStyle.getFillForegroundColor());
        Assertions.assertEquals(Color.GREEN_LIGHT, layoutStyle.getFillBackgroundColor());
        Assertions.assertEquals(borderHeader, layoutStyle.getBorderTop());
        Assertions.assertEquals(borderHeader, layoutStyle.getBorderLeft());
        Assertions.assertEquals(borderHeader, layoutStyle.getBorderRight());
        Assertions.assertEquals(borderHeader, layoutStyle.getBorderBottom());
    }

    @Test
    void testCreateRowStyleNormal() {
        final var style = formatterContext.createRowStyleNormal();
        Assertions.assertEquals(LayoutTextStyle.class, style.getClass());

        final var condition = style.getCondition();
        Assertions.assertEquals(TableCell.class, condition.getClazz());

        final var textStyle = ((LayoutTextStyle) style).getTextStyle();
        Assertions.assertEquals(FontFamilyStyle.SERIF, textStyle.getFontFamilyStyle());
        Assertions.assertEquals(FormatterContext.STYLE_NORMAL_FONT_SIZE, textStyle.getFontSize());

        final var layoutStyle = ((LayoutTextStyle) style).getLayoutStyle();
        final var borderHeader =
            BorderStyle.create(Color.GREY_50_PERCENT, BorderWeight.THIN);
        Assertions.assertEquals(HorAlignment.LEFT, layoutStyle.getHorAlignment());
        Assertions.assertEquals(FillPattern.SOLID_FOREGROUND, layoutStyle.getFillPattern());
        Assertions.assertEquals(borderHeader, layoutStyle.getBorderTop());
        Assertions.assertEquals(borderHeader, layoutStyle.getBorderLeft());
        Assertions.assertEquals(borderHeader, layoutStyle.getBorderRight());
        Assertions.assertEquals(borderHeader, layoutStyle.getBorderBottom());
    }

    @Test
    void testCreateStyleFooter() {
        final var style = formatterContext.createStyleFooter();
        Assertions.assertEquals(TextStyle.class, style.getClass());

        final var textStyle = (TextStyle) style;
        Assertions.assertEquals(FontFamilyStyle.SERIF, textStyle.getFontFamilyStyle());
        Assertions.assertEquals(FormatterContext.STYLE_SMALL_FONT_SIZE, textStyle.getFontSize());
    }
}