package com.reporter.domain.styles;

import com.google.common.base.MoreObjects;
import com.reporter.domain.Document;
import com.reporter.domain.DocumentItem;
import com.reporter.domain.FontService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The class stores styles.
 * {@link StyleService#extractStyleFor} returns the first style
 * which can be applied to the DocumentItem object.
 * <p>
 * By default, for html-document registered styles are added to head,
 * contra those that are given in the {@link Document} structure -
 * they will be added into the tag via style="...".
 */

public abstract class StyleService implements StyleApplier {
    private static final Logger log = LoggerFactory.getLogger(StyleService.class);
    /**
     * List of registered styles
     */
    protected final List<Style> styles = new ArrayList<>();
    /**
     * Number representation format
     */
    protected DecimalFormat decimalFormat;

    protected FontService fontService;

    /**
     * Returns first matching style from List<Style> for the item object,
     * checking style's {@link StyleCondition}.
     * A style with {@link StyleCondition} equals null is returned as appropriate.
     *
     * @param item the object on which to test the conditions of styles.
     * @return first style with condition that item matches
     */
    public Optional<Style> extractStyleFor(DocumentItem item) {
        return styles
            .stream()
            .filter(s -> {
                if (s.getCondition() != null) {
                    final var passCondition = s.getCondition().test(item);
                    final var itemClass = item.getClass();
                    final var conditionClass = s.getCondition().getClazz();
                    return passCondition && itemClass.isAssignableFrom(conditionClass);
                }
                return true;
            })
            .findFirst();
    }

    public Boolean contains(Style style) {
        return styles.contains(style);
    }

    /**
     * Adds styles to styles
     */
    @SuppressWarnings("unchecked")
    public <T extends StyleService> T addStyles(Style... styles) {
        log.debug("styles before addStyles - {}", this.styles);
        this.styles.addAll(Arrays.asList(styles));
        log.debug("addStyles called for styles - {}", Arrays.toString(styles));
        return (T) this;
    }

    /**
     * Remove styles from styles
     */
    @SuppressWarnings("unchecked")
    public <T extends StyleService> T removeStyles(Style... styles) {
        final var sList = new ArrayList<>(Arrays.asList(styles));
        log.debug("styles before removeStyles - {}", this.styles);
        this.styles.removeIf(sList::contains);
        log.debug("removeStyles called for styles - {}", Arrays.toString(styles));
        return (T) this;
    }

    public List<Style> getStyles() {
        return styles;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("styles", styles)
            .add("decimalFormat", decimalFormat)
            .add("fontService", fontService)
            .toString();
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public StyleService setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
        return this;
    }

    public FontService getFontService() {
        return fontService;
    }

    public StyleService setFontService(FontService fontService) {
        this.fontService = fontService;
        return this;
    }
}
