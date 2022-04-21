package com.reporter.domain.styles;

import com.reporter.domain.styles.constants.Color;
import com.reporter.domain.styles.constants.FillPattern;
import com.reporter.domain.styles.constants.HorAlignment;
import com.reporter.domain.styles.constants.VertAlignment;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * The style is used to set:
 * the location of the content in the element,
 * element width, border, color and fill type of nearby environment
 */
public class LayoutStyle extends Style {
    /**
     * Content width (currently for cells in excel and pdf)
     * FIXME: Bad. This class should be independent of format and implementation details.
     */
    protected int width;
    /**
     * Content auto alignment
     */
    protected boolean autoWidth;
    /**
     * Stretch text content to fit width
     */
    protected boolean shrinkToFit;
    /**
     * Type of top line limiting content
     */
    protected BorderStyle borderTop = BorderStyle.create();
    /**
     * Type of left line limiting content
     */
    protected BorderStyle borderLeft = BorderStyle.create();
    /**
     * Type of right line limiting content
     */
    protected BorderStyle borderRight = BorderStyle.create();
    /**
     * Type of bottom line limiting content
     */
    protected BorderStyle borderBottom = BorderStyle.create();
    /**
     * Back color of all content
     */
    protected Color fillBackgroundColor = Color.WHITE;
    /**
     * Rear content text color
     */
    protected Color fillForegroundColor = Color.WHITE;
    /**
     * The way to fill the back color of the content
     */
    protected FillPattern fillPattern = FillPattern.NO_FILL;
    /**
     * Content horizontal alignment type
     */
    protected HorAlignment horAlignment = HorAlignment.GENERAL;
    /**
     * Content vertical alignment type
     */
    protected VertAlignment vertAlignment = VertAlignment.CENTER;

    public static LayoutStyle create() {
        return new LayoutStyle();
    }

    @Override
    public LayoutStyle clone() throws CloneNotSupportedException {
        return
            ((LayoutStyle) super
                .clone())
                .setBorderTop(borderTop.clone())
                .setBorderLeft(borderLeft.clone())
                .setBorderRight(borderRight.clone())
                .setBorderBottom(borderBottom.clone());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final LayoutStyle that = (LayoutStyle) o;

        return
            Objects.equal(this.width, that.width)
                && Objects.equal(this.autoWidth, that.autoWidth)
                && Objects.equal(this.shrinkToFit, that.shrinkToFit)
                && Objects.equal(this.borderTop, that.borderTop)
                && Objects.equal(this.borderLeft, that.borderLeft)
                && Objects.equal(this.borderRight, that.borderRight)
                && Objects.equal(this.borderBottom, that.borderBottom)
                && Objects.equal(this.fillBackgroundColor, that.fillBackgroundColor)
                && Objects.equal(this.fillForegroundColor, that.fillForegroundColor)
                && Objects.equal(this.fillPattern, that.fillPattern)
                && Objects.equal(this.horAlignment, that.horAlignment)
                && Objects.equal(this.vertAlignment, that.vertAlignment);
    }

    @Override
    public int hashCode() {
        return
            Objects
                .hashCode(
                    width,
                    autoWidth,
                    shrinkToFit,
                    borderTop,
                    borderLeft,
                    borderRight,
                    borderBottom,
                    fillBackgroundColor,
                    fillForegroundColor,
                    fillPattern,
                    horAlignment,
                    vertAlignment
                );
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("width", getWidth())
                .add("autoWidth", isAutoWidth())
                .add("shrinkToFit", isShrinkToFit())
                .add("borderTop", getBorderTop())
                .add("borderLeft", getBorderLeft())
                .add("borderRight", getBorderRight())
                .add("borderBottom", getBorderBottom())
                .add("fillBackgroundColor", getFillBackgroundColor())
                .add("fillForegroundColor", getFillForegroundColor())
                .add("fillPattern", getFillPattern())
                .add("horAlignment", getHorAlignment())
                .add("vertAlignment", getVertAlignment())
                .add("parent", super.toString())
                .toString();
    }

    public int getWidth() {
        return width;
    }

    public LayoutStyle setWidth(int width) {
        this.width = width;
        return this;
    }

    public boolean isAutoWidth() {
        return autoWidth;
    }

    public LayoutStyle setAutoWidth(boolean autoWidth) {
        this.autoWidth = autoWidth;
        return this;
    }

    public boolean isShrinkToFit() {
        return shrinkToFit;
    }

    public LayoutStyle setShrinkToFit(boolean shrinkToFit) {
        this.shrinkToFit = shrinkToFit;
        return this;
    }

    public BorderStyle getBorderTop() {
        return borderTop;
    }

    public LayoutStyle setBorderTop(BorderStyle borderTop) {
        this.borderTop = borderTop;
        return this;
    }

    public BorderStyle getBorderLeft() {
        return borderLeft;
    }

    public LayoutStyle setBorderLeft(BorderStyle borderLeft) {
        this.borderLeft = borderLeft;
        return this;
    }

    public BorderStyle getBorderRight() {
        return borderRight;
    }

    public LayoutStyle setBorderRight(BorderStyle borderRight) {
        this.borderRight = borderRight;
        return this;
    }

    public BorderStyle getBorderBottom() {
        return borderBottom;
    }

    public LayoutStyle setBorderBottom(BorderStyle borderBottom) {
        this.borderBottom = borderBottom;
        return this;
    }

    public Color getFillBackgroundColor() {
        return fillBackgroundColor;
    }

    public LayoutStyle setFillBackgroundColor(Color fillBackgroundColor) {
        this.fillBackgroundColor = fillBackgroundColor;
        return this;
    }

    public Color getFillForegroundColor() {
        return fillForegroundColor;
    }

    public LayoutStyle setFillForegroundColor(Color fillForegroundColor) {
        this.fillForegroundColor = fillForegroundColor;
        return this;
    }

    public FillPattern getFillPattern() {
        return fillPattern;
    }

    public LayoutStyle setFillPattern(FillPattern fillPattern) {
        this.fillPattern = fillPattern;
        return this;
    }

    public HorAlignment getHorAlignment() {
        return horAlignment;
    }

    public LayoutStyle setHorAlignment(HorAlignment horAlignment) {
        this.horAlignment = horAlignment;
        return this;
    }

    public VertAlignment getVertAlignment() {
        return vertAlignment;
    }

    public LayoutStyle setVertAlignment(VertAlignment vertAlignment) {
        this.vertAlignment = vertAlignment;
        return this;
    }
}
