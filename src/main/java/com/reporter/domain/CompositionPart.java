package com.reporter.domain;

import com.reporter.domain.styles.Style;
import com.reporter.domain.styles.StyleCondition;
import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * The compositional part of the presentation of the document,
 * consists of Iterable(K extends {@link DocumentItem}) elements
 */
public abstract class CompositionPart<T extends CompositionPart<?, ?>, K extends DocumentItem> extends DocumentItem {
    protected Iterable<K> parts;

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("parts", parts)
                .add("parent", super.toString())
                .toString();
    }

    @SuppressWarnings("unchecked")
    public T setParts(Iterable<K> parts) {
        this.parts = parts;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T addPart(K docItem) {
        checkPartsForAppend();
        ((Collection<K>) this.parts).add(docItem);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T addParts(K... docItems) {
        checkPartsForAppend();
        ((Collection<K>) this.parts).addAll(Arrays.asList(docItems));
        return (T) this;
    }

    public Iterable<K> getParts() {
        if (parts == null) {
            parts = new ArrayList<>();
        }
        return parts;
    }

    /**
     * Method allows you to apply style to all nested parts of
     * {@link CompositionPart} with checking only clazz of {@link StyleCondition}
     *
     * @param style style that will be applied to parts
     * @return CompositionPart
     * @throws CloneNotSupportedException style is not Cloneable
     */
    @SuppressWarnings("unchecked")
    public T spreadStyleToParts(Style style) throws CloneNotSupportedException {
        return spreadStyleToParts(style, -1);
    }

    @SuppressWarnings("unchecked")
    public T spreadStyleToParts(Style style, int depth) throws CloneNotSupportedException {
        if (depth != 0 && style != null) {
            for (final var part : parts) {
                final var styleCondition = style.getCondition();
                if (part.getStyle() == null
                    && (styleCondition == null || part.getClass().isAssignableFrom(styleCondition.getClazz()))
                ) {
                    part.setStyle(style.clone());
                }
                if (part instanceof CompositionPart<?,?>) {
                    ((CompositionPart<?,?>) part).spreadStyleToParts(style, depth - 1);
                }
            }
        }
        return (T) this;
    }

    private void checkPartsForAppend() {
        if (this.parts == null) {
            this.parts = new ArrayList<>();
        } else if (!(this.parts instanceof Collection)) {
            throw new IllegalStateException("Parts is not a collection");
        }
    }
}
