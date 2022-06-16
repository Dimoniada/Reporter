package com.reporter.domain;

import com.reporter.formatter.FormatterVisitor;
import com.google.common.base.MoreObjects;

/**
 * Document header class
 * {@link Heading#depth} - heading level
 */
public class Heading extends TextItem<Heading> {
    /**
     * header level,
     * is always greater than or equal to 0.
     * For html - no more than 6.
     */
    protected int depth;

    public Heading(int depth) {
        this.depth = depth;
    }

    public static Heading create(String text, int depth) {
        return new Heading(depth).setText(text);
    }

    public static Heading create(int depth) {
        return new Heading(depth);
    }

    public Heading accept(FormatterVisitor visitor) throws Exception {
        visitor.visitHeading(this);
        return this;
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("depth", depth)
                .add("parent", super.toString())
                .toString();
    }

    public int getDepth() {
        return depth;
    }

    public Heading setDepth(short depth) {
        this.depth = depth;
        return this;
    }
}
