package com.reporter.formatter;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.TimeZone;

public class BaseFormatterContext {
    public static final String encoding = "utf-8";
    public static final Locale locale = Locale.forLanguageTag("en");
    public static final TimeZone timeZone = TimeZone.getDefault();
    public static final DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
    public static final Character csvDelimiter = '|';

    public FormatterContext formatterContext;

    public void initFormatterContext() {
        formatterContext =
            FormatterContext.create(
                encoding,
                locale,
                timeZone,
                decimalFormat,
                csvDelimiter
            );
    }
}
