package com.reporter.formatter.html;

import com.reporter.domain.DocumentItem;
import com.reporter.domain.TextItem;
import com.reporter.domain.styles.Style;
import com.reporter.formatter.html.styles.CssStyle;
import com.reporter.formatter.html.styles.HtmlStyleService;
import com.reporter.formatter.html.tag.HtmlTable;
import com.reporter.formatter.html.tag.HtmlTag;
import com.google.common.base.Objects;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.ParseException;

import static com.reporter.utils.LocalizedNumberUtils.applyDecimalFormat;

public class TagCreator {
    protected OutputStreamWriter outputStreamWriter;
    protected DecimalFormat decimalFormat;
    protected DocumentItem item;

    public TagCreator(OutputStreamWriter outputStreamWriter, DecimalFormat decimalFormat) {
        this.outputStreamWriter = outputStreamWriter;
        this.decimalFormat = decimalFormat;
    }

    public static String htmlStyleId(Style style) {
        return "_" + Integer.toHexString(Objects.hashCode(style));
    }

    public TagCreator write(String text) throws IOException {
        outputStreamWriter.write(text);
        return this;
    }

    public TagCreator writeTag(
        HtmlTag tag, Style style, Boolean styleInHeader, Boolean useHtml4Tags, Boolean needCloseTag
    ) throws IOException, ParseException
    {
        final var cssStyle = new CssStyle();
        final var isHtmlTable = tag instanceof HtmlTable;
        if (useHtml4Tags) {
            final var layoutStyle = HtmlStyleService.extractLayoutStyle(style);
            HtmlStyleService.fillHtml4StyleTagsFromStyle(tag, layoutStyle, isHtmlTable);
            final var textStyle = HtmlStyleService.extractTextStyle(style);
            write(String.format("<%s%s>", tag.getTagName(), tag.attributesToHtmlString(true)));
            if (item instanceof TextItem<?>) {
                if (textStyle != null) {
                    final var html4Font = HtmlStyleService.convertHtml4Font(textStyle);
                    write(
                        String
                            .format(
                                "<%s%s>",
                                html4Font.getTagName(),
                                html4Font.attributesToHtmlString(true)
                            )
                            + HtmlStyleService.escapeHtml(applyDecimalFormat((TextItem<?>) item, decimalFormat))
                            + html4Font.close()
                    );
                } else {
                    write(HtmlStyleService.escapeHtml(applyDecimalFormat((TextItem<?>) item, decimalFormat)));
                }
            }
        } else {
            if (styleInHeader) {
                tag.setClass(htmlStyleId(style));
            } else if (item != null && item.getStyle() != null) {
                HtmlStyleService.fillCssStyleFromStyle(cssStyle, style, isHtmlTable, false);
                tag.setStyle(cssStyle);
            }
            write(String.format("<%s%s>", tag.getTagName(), tag.attributesToHtmlString(false)));
            if (item instanceof TextItem<?>) {
                write(HtmlStyleService.escapeHtml(applyDecimalFormat((TextItem<?>) item, decimalFormat)));
            }
        }
        if (needCloseTag) {
            write(tag.close());
        }
        return this;
    }

    public OutputStreamWriter getOutputStreamWriter() {
        return outputStreamWriter;
    }

    public TagCreator setOutputStreamWriter(OutputStreamWriter outputStreamWriter) {
        this.outputStreamWriter = outputStreamWriter;
        return this;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public TagCreator setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
        return this;
    }

    public DocumentItem getItem() {
        return item;
    }

    public TagCreator setItem(DocumentItem item) {
        this.item = item;
        return this;
    }
}
