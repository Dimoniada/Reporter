package com.reporter.domain.styles;

import com.reporter.domain.styles.constants.BorderWeight;
import com.reporter.domain.styles.constants.Color;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Describes the line of the border, separator line
 */
public class BorderStyle implements Cloneable {
    /**
     * Line color
     */
    protected Color color = Color.WHITE;
    /**
     * Thickness or line style
     */
    protected BorderWeight weight = BorderWeight.NONE;

    public static BorderStyle create() {
        return new BorderStyle();
    }

    public static BorderStyle create(Color color, BorderWeight weight) {
        return new BorderStyle().setColor(color).setWeight(weight);
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("color", color)
                .add("weight", weight)
                .toString();
    }

    public Color getColor() {
        return color;
    }

    public BorderStyle setColor(Color color) {
        this.color = color;
        return this;
    }

    public BorderWeight getWeight() {
        return weight;
    }

    public BorderStyle setWeight(BorderWeight weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final BorderStyle that = (BorderStyle) o;

        return
            Objects.equal(this.color, that.color)
                && Objects.equal(this.weight, that.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(color, weight);
    }

    @Override
    public BorderStyle clone() throws CloneNotSupportedException  {
        return (BorderStyle) super.clone();
    }
}
