package com.reporter;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Locale;

/**
 * The class stores the parameters passed in the request
 */
public class ExportContext {
    private static final String FORMAT_PATTERN = "^[a-z0-9]{1,255}$";

    /**
     * Export format, now is ["pdf", "xls", "xlsx", "html", "csv"]
     */
    protected @Pattern(regexp = FORMAT_PATTERN) @NotEmpty String format;

    /**
     * Export locale, now is ["ru", "en", "zh", "in"]
     */
    protected Locale locale;

    /**
     * The name of the columns of the DB table for export
     */
    protected List<String> columns = List.of();

    /**
     * Document encoding, if applicable
     */
    protected String encoding = "UTF-8";

    /**
     * Separator for CSV [',' ';' '\t', e.t.c.]
     */
    protected Character csvDelimiter = ';';

    public String getFormat() {
        return format;
    }

    public ExportContext setFormat(String format) {
        this.format = format;
        return this;
    }

    public Locale getLocale() {
        return locale;
    }

    public ExportContext setLocale(String sLocale) {
        if (!StringUtils.hasText(sLocale)) {
            this.locale = LocaleContextHolder.getLocale();
        } else {
            this.locale = Locale.forLanguageTag(sLocale);
        }
        return this;
    }

    public List<String> getColumns() {
        return columns;
    }

    public ExportContext setColumns(List<String> columns) {
        this.columns = columns;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public ExportContext setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public Character getCsvDelimiter() {
        return csvDelimiter;
    }

    public ExportContext setCsvDelimiter(Character csvDelimiter) {
        this.csvDelimiter = csvDelimiter;
        return this;
    }
}
