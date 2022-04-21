package com.reporter.domain.styles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessorFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Allows you to combine the styles of objects of inherited classes
 */
public final class StyleUtils {
    private static final Logger log = LoggerFactory.getLogger(StyleUtils.class);

    /**
     * Returns the style difference.
     * Accepts 2 styles. The first style is an empty instance,
     * the second is an instance with changed fields.
     * Returns a map(name, value) of the changed fields of the second style.
     *
     * @param style1 style 1 (base)
     * @param style2 compared style 2 (modified)
     * @return map(name, value) distinct properties of style 2 relative to style 1
     */
    public static Map<String, Object> compare(Style style1, Style style2) {
        log.debug("Comparing styles {} and {}", style1, style2);
        final var map = new HashMap<String, Object>();
        if (style1.getClass() == style2.getClass()) {
            final var propAcc1 = PropertyAccessorFactory.forDirectFieldAccess(style1);
            final var propAcc2 = PropertyAccessorFactory.forDirectFieldAccess(style2);
            Arrays.stream(style2.getClass().getDeclaredFields())
                .map(Field::getName)
                .forEach(
                    name -> {
                        final Object value1 = propAcc1.getPropertyValue(name);
                        final Object value2 = propAcc2.getPropertyValue(name);
                        if (!Objects.equals(value1, value2)) {
                            map.put(name, value2);
                        }
                    }
                );
        }
        log.debug("Comparison result map is {}", map);
        return map;
    }

    /**
     * Overlays styleFrom style properties over styleTo style properties, replacing them.
     * Overlays only those properties that differ from
     * default properties for a new class instance of type styleFrom (see {@link StyleUtils#compare(Style, Style)}).
     *
     * @param styleFrom style whose fields are transferred to styleTo,
     * if they are different from the fields of an empty Style() instance
     * @param styleTo style that accepts styleFrom fields
     */
    public static void joinWith(Style styleFrom, Style styleTo) throws Exception {
        log.debug("Style {} will unite with style {} ", styleFrom, styleTo);
        if (styleFrom != null && styleTo != null) {
            if (styleTo.equals(styleFrom)) {
                return;
            }
            if (styleFrom instanceof LayoutTextStyle && styleTo instanceof LayoutTextStyle) {
                StyleUtils.joinWith(
                    ((LayoutTextStyle) styleFrom).getLayoutStyle(),
                    ((LayoutTextStyle) styleTo).getLayoutStyle()
                );
                StyleUtils.joinWith(
                    ((LayoutTextStyle) styleFrom).getTextStyle(),
                    ((LayoutTextStyle) styleTo).getTextStyle()
                );
            }
            final Style model = styleFrom.getClass().getDeclaredConstructor().newInstance();
            if (styleTo.getClass() == styleFrom.getClass()) {
                final var diff = compare(model, styleFrom);
                final var fields = styleTo.getClass().getDeclaredFields();
                final var propAcc = PropertyAccessorFactory.forDirectFieldAccess(styleTo);
                Arrays.stream(fields).map(Field::getName).forEach(name -> {
                    if (diff.containsKey(name)) {
                        propAcc.setPropertyValue(name, diff.get(name));
                    }
                });
            }
            log.debug("Style {} was merged into result style {} ", styleFrom, styleTo);
        }
    }

    @Override
    public StyleUtils clone() throws CloneNotSupportedException {
        return (StyleUtils) super.clone();
    }
}
