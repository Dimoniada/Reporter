package com.reporter.utils;

import com.reporter.domain.TextItem;
import com.reporter.domain.styles.TextStyle;
import org.springframework.util.StringUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * A set of methods for representing numbers in different {@link DecimalFormat} locales
 */
public final class LocalizedNumberUtils {
    /**
     * Number pattern per line
     */
    private static final String floatRegex = "[-+]?[0-9]*[,.]?[0-9]+";
    /**
     * Separator of the integer and fractional part of the number
     */
    private static final char DEFAULT_DECIMAL_SEPARATOR = ',';
    /**
     * The number of digits of the fractional part of the number
     */
    private static final int DEFAULT_DECIMAL_FRACTION_DIGITS = 3;

    private LocalizedNumberUtils() {
        /**/
    }

    public static String applyDecimalFormat(TextItem<?> textItem, DecimalFormat decimalFormat) throws ParseException {
        final var text = textItem.getText();
        final var style = textItem.getStyle();
        if (style instanceof TextStyle && StringUtils.hasText(text) && isNumber(text)) {
            final var textStyle = (TextStyle) style;
            final var format =
                decimalFormat != null
                    ? decimalFormat
                    : textStyle.getDecimalFormat();
            return localizeNumber(text, format, textStyle.getFontLocale());
        }
        return StringUtils.hasText(text) ? text : "";
    }

    public static Boolean isNumber(String text) {
        return Pattern.matches(floatRegex, text);
    }

    /**
     * @param number string representation of a number
     * @param format {@link DecimalFormat} number format
     * @return - formatted text
     * @throws ParseException number in string could not be resolved
     */
    public static String localizeNumber(String number, DecimalFormat format, Locale locale) throws ParseException {
        if (format != null) {
            return format.format(format.parse(number).floatValue());
        } else {
            final var localFormat = (DecimalFormat) NumberFormat.getInstance(locale != null ? locale : Locale.ENGLISH);
            localFormat.setRoundingMode(RoundingMode.DOWN);
            localFormat.setMinimumFractionDigits(DEFAULT_DECIMAL_FRACTION_DIGITS);
            localFormat.setMaximumFractionDigits(DEFAULT_DECIMAL_FRACTION_DIGITS);
            final var symbols = localFormat.getDecimalFormatSymbols();
            symbols.setDecimalSeparator(DEFAULT_DECIMAL_SEPARATOR);
            localFormat.setDecimalFormatSymbols(symbols);
            return localFormat.format(localFormat.parse(number).floatValue());
        }
    }
}
