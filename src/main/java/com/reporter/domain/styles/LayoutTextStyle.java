package com.reporter.domain.styles;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Text display style, its location,
 * width, border, color and fill type of the environment
 */
public class LayoutTextStyle extends Style {

    protected LayoutStyle layoutStyle = new LayoutStyle();
    protected TextStyle textStyle = new TextStyle();

    public static LayoutTextStyle create() {
        return new LayoutTextStyle();
    }

    public static LayoutTextStyle create(TextStyle textStyle, LayoutStyle layoutStyle) {
        final var style = new LayoutTextStyle();
        style.setTextStyle(textStyle);
        style.setLayoutStyle(layoutStyle);
        return style;
    }

    @Override
    public LayoutTextStyle clone() throws CloneNotSupportedException {
        return ((LayoutTextStyle) super.clone())
            .setTextStyle(textStyle != null ? textStyle.clone() : null)
            .setLayoutStyle(layoutStyle != null ? layoutStyle.clone() : null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final LayoutTextStyle that = (LayoutTextStyle) o;

        return
            Objects.equal(this.layoutStyle, that.layoutStyle)
            && Objects.equal(this.textStyle, that.textStyle);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(layoutStyle, textStyle);
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("layoutStyle", layoutStyle)
                .add("textStyle", textStyle)
                .add("parent", super.toString())
                .toString();
    }

    public LayoutStyle getLayoutStyle() {
        return layoutStyle;
    }

    public TextStyle getTextStyle() {
        return textStyle;
    }

    public LayoutTextStyle setLayoutStyle(LayoutStyle layoutStyle) {
        this.layoutStyle = layoutStyle;
        return this;
    }

    public LayoutTextStyle setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
        return this;
    }
}
