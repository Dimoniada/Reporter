package com.reporter.formatter.html.tag;

import com.reporter.domain.DocumentItem;
import com.reporter.domain.styles.Style;
import com.reporter.formatter.html.attribute.HtmlAttribute;
import com.google.common.base.Objects;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class HtmlTag extends HtmlStyledTag {
    /**
     * Gets the unique style name for the entry
     *
     * @param style Style class of {@link DocumentItem}
     * @return unique name
     */
    public static String htmlStyleId(Style style) {
        return "_" + Integer.toHexString(Objects.hashCode(style));
    }

    /**
     * @return tag name
     */
    public abstract String getTagName();

    public String toLower(Locale locale) {
        return getTagName().toLowerCase(locale);
    }

    public String toLower() {
        return getTagName().toLowerCase(Locale.ENGLISH);
    }

    public String toUpper(Locale locale) {
        return getTagName().toUpperCase(locale);
    }

    public String toUpper() {
        return getTagName().toUpperCase(Locale.ENGLISH);
    }

    public String attributesToHtmlString(Boolean isHtml4) {
        final var res =
            getAvailableAttributes()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .filter(s -> s.getValue().getAttributeValue() != null)
                .map(e -> itemMapping(e, isHtml4))
                .collect(
                    Collectors
                        .joining(
                            isHtml4
                                ? HtmlAttribute.DELIMITER_PATTERN_HTML4
                                : HtmlAttribute.DELIMITER_PATTERN_HTML,
                            " ",
                            ""
                        )
                );

        return
            res.length() > 1
                ? res
                : "";
    }

    /**
     * Writes an opening tag
     *
     * @return this
     */
    public String open() {
        return String.format("<%s>", getTagName());
    }

    /**
     * Writes a closing tag
     *
     * @return this
     * @throws IOException write error
     */
    public String close() throws IOException {
        return String.format("</%s>", getTagName());
    }

    @Override
    public String toString() {
        return getTagName();
    }

    private String itemMapping(Map.Entry<String, HtmlAttribute> e, Boolean isHtml4) {
        final var key = e.getKey();
        final var attribute = e.getValue();
        final var processedValue = attribute.produceDefaultStringAttribute(attribute.getAttributeValue());
        return
                String.format(
                        attribute.getAssignmentPattern(isHtml4),
                        key,
                        processedValue
                );
    }
}
