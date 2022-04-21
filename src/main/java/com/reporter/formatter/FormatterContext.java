package com.reporter.formatter;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.reporter.domain.FontService;
import com.reporter.domain.TableCell;
import com.reporter.domain.TableHeaderCell;
import com.reporter.domain.styles.BorderStyle;
import com.reporter.domain.styles.FontFamilyStyle;
import com.reporter.domain.styles.LayoutStyle;
import com.reporter.domain.styles.LayoutTextStyle;
import com.reporter.domain.styles.Style;
import com.reporter.domain.styles.StyleCondition;
import com.reporter.domain.styles.TextStyle;
import com.reporter.domain.styles.constants.BorderWeight;
import com.reporter.domain.styles.constants.Color;
import com.reporter.domain.styles.constants.FillPattern;
import com.reporter.domain.styles.constants.HorAlignment;
import com.reporter.domain.styles.constants.VertAlignment;
import org.supercsv.prefs.CsvPreference;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Predicate;

/**
 * The class stores the context of the report formatter
 */
public class FormatterContext {
    public static final char CSV_QUOTE = '"';
    public static final String CSV_END_OF_LINE = "\n";
    public static final Character CSV_DELIMITER = ';';
    public static final short STYLE_BIG_FONT_SIZE = 12;
    public static final short STYLE_NORMAL_FONT_SIZE = 10;
    public static final short STYLE_SMALL_FONT_SIZE = 8;
    protected String encoding;
    protected Locale locale;
    protected TimeZone timeZone;
    protected DecimalFormat decimalFormat;
    protected Character csvDelimiter;

    public FormatterContext() {
        /**/
    }

    public FormatterContext(
        String encoding,
        Locale locale,
        TimeZone timeZone,
        DecimalFormat decimalFormat,
        Character csvDelimiter
    ) {
        this.encoding = encoding;
        this.locale = locale;
        this.timeZone = timeZone;
        this.decimalFormat = decimalFormat;
        this.csvDelimiter = csvDelimiter;
    }

    public static FormatterContext create(
        String encoding,
        Locale locale,
        TimeZone timezone,
        DecimalFormat decimalFormat,
        Character csvDelimiter
    ) {
        return new FormatterContext(encoding, locale, timezone, decimalFormat, csvDelimiter);
    }

    /**
     * Returns the recording format settings for csv files
     *
     * @return CsvPreference
     */
    public CsvPreference createCsvPreference() {
        Character delimiter = csvDelimiter;
        if (delimiter == null) {
            delimiter = CSV_DELIMITER;
        }
        return
            new CsvPreference
                .Builder(
                    CSV_QUOTE,
                    delimiter,
                    CSV_END_OF_LINE
                )
                .build();
    }

    /**
     * Returns the current date and time in timeZone format, taking into account the locale
     *
     * @return datetime string
     */
    public String createFormattedZoneDateTime() {
        final var zonedDateTime = ZonedDateTime.now(timeZone.toZoneId());
        final var formatPattern = DateTimeFormatterBuilder
            .getLocalizedDateTimePattern(
                FormatStyle.LONG,
                FormatStyle.LONG,
                IsoChronology.INSTANCE,
                locale
            );
        final var formatter = DateTimeFormatter.ofPattern(formatPattern, locale);
        return zonedDateTime.format(formatter);
    }

    public FontService createFontService() throws IOException, FontFormatException {
        return FontService.create().initializeFonts();
    }

    public Style createHeaderCellStyle() {
        final var borderHeader = BorderStyle
            .create(Color.GREY_50_PERCENT, BorderWeight.DOUBLE);
        return LayoutTextStyle
            .create(
                TextStyle.create()
                    .setFontFamilyStyle(FontFamilyStyle.SANS_SERIF)
                    .setFontSize(STYLE_BIG_FONT_SIZE)
                    .setBold(true),
                LayoutStyle
                    .create()
                    .setBorderTop(borderHeader)
                    .setBorderLeft(borderHeader)
                    .setBorderRight(borderHeader)
                    .setBorderBottom(borderHeader)
                    .setHorAlignment(HorAlignment.LEFT)
                    .setVertAlignment(VertAlignment.CENTER)
                    .setAutoWidth(true)
                    .setFillPattern(FillPattern.SOLID_FOREGROUND)
            )
            .setCondition(
                StyleCondition
                    .create(
                        o -> o instanceof TableHeaderCell,
                        TableHeaderCell.class
                    )
            );
    }

    /**
     * Creates style for cells on rows with odd index
     *
     * @return Style
     */
    public Style createRowStyleInterlinear() {
        final var borderHeader = BorderStyle
            .create(Color.GREY_50_PERCENT, BorderWeight.THIN);
        final Predicate<Object> isTableCell = o -> o instanceof TableCell;
        final Predicate<Object> isInterlinear = o -> ((TableCell) o).getCustomIndex() % 2 == 1;
        return LayoutTextStyle
            .create(
                TextStyle.create()
                    .setFontFamilyStyle(FontFamilyStyle.SERIF)
                    .setFontSize(STYLE_NORMAL_FONT_SIZE),
                LayoutStyle
                    .create()
                    .setBorderTop(borderHeader)
                    .setBorderLeft(borderHeader)
                    .setBorderRight(borderHeader)
                    .setBorderBottom(borderHeader)
                    .setHorAlignment(HorAlignment.LEFT)
                    .setVertAlignment(VertAlignment.CENTER)
                    .setAutoWidth(true)
                    .setFillPattern(FillPattern.SOLID_FOREGROUND)
                    .setFillBackgroundColor(Color.GREEN_LIGHT)
                    .setFillForegroundColor(Color.GREEN_LIGHT)
            )
            .setCondition(
                StyleCondition
                    .create(
                        isTableCell.and(isInterlinear),
                        TableCell.class
                    )
            );
    }

    /**
     * Creates style for cells on rows with even index
     *
     * @return Style
     */
    public Style createRowStyleNormal() {
        final var borderNormal = BorderStyle
            .create(Color.GREY_50_PERCENT, BorderWeight.THIN);
        final Predicate<Object> isTableCell = o -> o instanceof TableCell;
        final Predicate<Object> isInterlinear = o -> ((TableCell) o).getCustomIndex() % 2 == 0;
        return LayoutTextStyle.create(
                TextStyle
                    .create()
                    .setFontFamilyStyle(FontFamilyStyle.SERIF)
                    .setFontSize(STYLE_NORMAL_FONT_SIZE),
                LayoutStyle
                    .create()
                    .setBorderTop(borderNormal)
                    .setBorderLeft(borderNormal)
                    .setBorderRight(borderNormal)
                    .setBorderBottom(borderNormal)
                    .setAutoWidth(true)
                    .setFillPattern(FillPattern.SOLID_FOREGROUND)
                    .setHorAlignment(HorAlignment.LEFT)
            )
            .setCondition(StyleCondition
                .create(
                    isTableCell.and(isInterlinear),
                    TableCell.class
                )
            );
    }

    public Style createStyleFooter() {
        return TextStyle.create()
            .setFontFamilyStyle(FontFamilyStyle.SERIF)
            .setFontSize(STYLE_SMALL_FONT_SIZE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final FormatterContext that = (FormatterContext) o;

        return Objects.equal(this.encoding, that.encoding) &&
            Objects.equal(this.locale, that.locale) &&
            Objects.equal(this.timeZone, that.timeZone) &&
            Objects.equal(this.decimalFormat, that.decimalFormat) &&
            Objects.equal(this.csvDelimiter, that.csvDelimiter);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(encoding, locale, timeZone, decimalFormat, csvDelimiter);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("encoding", encoding)
            .add("locale", locale)
            .add("timezone", timeZone)
            .add("decimalFormat", decimalFormat)
            .add("csvDelimiter", csvDelimiter)
            .toString();
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    public Character getCsvDelimiter() {
        return csvDelimiter;
    }

    public void setCsvDelimiter(Character csvDelimiter) {
        this.csvDelimiter = csvDelimiter;
    }
}
