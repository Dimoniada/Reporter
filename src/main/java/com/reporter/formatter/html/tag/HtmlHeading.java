package com.reporter.formatter.html.tag;

public class HtmlHeading extends HtmlTag {
    public static final String TAG_NAME = "h*";

    protected final int depth;

    public HtmlHeading(int depth) {
        this.depth = depth;
    }

    private void checkTag(int i) {
        if (i < 0 || i > 6) {
            throw new IllegalArgumentException("Malformed tag: " + TAG_NAME.replace("*", String.valueOf(i)));
        }
    }

    @Override
    public String getTagName() {
        checkTag(depth);
        return TAG_NAME.replace("*", String.valueOf(depth));
    }

    public int getDepth() {
        return depth;
    }
}
