package com.reporter.domain.styles;

import com.reporter.domain.TextItem;
import com.reporter.domain.styles.constants.Color;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Display text style for class {@link TextItem}
 */
public class TextStyle extends Style {
    /**
     * Font name of font/resource:
     * 1) the name of the font registered in the system (for 'excel', 'word')
     * 2) full font name from built-in resource (for 'pdf')
     * 3) font name (for html format)
     */
    protected String fontNameResource = "";
    /**
     * Font class style,
     * default FontFamilyStyle.SERIF;
     */
    protected FontFamilyStyle fontFamilyStyle = FontFamilyStyle.SERIF;
    /**
     * Font locale (for 'pdf')
     */
    protected Locale fontLocale;
    /**
     * Number styling format
     */
    protected DecimalFormat decimalFormat;
    /**
     * Font size
     * (poorly, because units of measure vary from format to format - "pt", "px", "twips", e.t.c)
     */
    protected short fontSize = 10;
    /**
     * Font thickness flag
     */
    protected boolean bold;
    /**
     * Font slant flag
     */
    protected boolean italic;
    /**
     * Font underline flag
     */
    protected byte underline;
    /**
     * The flag sets the logic:
     * use the font strictly with the specified parameters from the resource,
     * or find a suitable font by name {@link TextStyle#fontNameResource}
     * and change it programmatically to match current values of TextStyle
     * (bold, italic, underline)
     */
    protected boolean useTtfFontAttributes;
    /**
     * Font color
     */
    protected Color color = Color.BLACK;

    public static TextStyle create() {
        return new TextStyle();
    }

    public static TextStyle create(String fontNameResource) {
        return new TextStyle().setFontNameResource(fontNameResource);
    }

    @Override
    public TextStyle clone() throws CloneNotSupportedException {
        return (TextStyle) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextStyle)) return false;
        final TextStyle textStyle = (TextStyle) o;
        return fontSize == textStyle.fontSize
            && bold == textStyle.bold
            && italic == textStyle.italic
            && underline == textStyle.underline
            && useTtfFontAttributes == textStyle.useTtfFontAttributes
            && Objects.equal(fontNameResource, textStyle.fontNameResource)
            && fontFamilyStyle == textStyle.fontFamilyStyle
            && Objects.equal(fontLocale, textStyle.fontLocale)
            && Objects.equal(decimalFormat, textStyle.decimalFormat)
            && color == textStyle.color;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
            fontNameResource,
            fontFamilyStyle,
            fontLocale,
            decimalFormat,
            fontSize,
            bold,
            italic,
            underline,
            useTtfFontAttributes,
            color
        );
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("fontNameResource", fontNameResource)
            .add("fontFamilyStyle", fontFamilyStyle)
            .add("fontLocale", fontLocale)
            .add("decimalFormat", decimalFormat)
            .add("fontSize", fontSize)
            .add("bold", bold)
            .add("italic", italic)
            .add("underline", underline)
            .add("useTtfFontAttributes", useTtfFontAttributes)
            .add("color", color)
            .add("parent", super.toString())
            .toString();
    }

    public String getFontNameResource() {
        return fontNameResource;
    }

    public TextStyle setFontNameResource(String fontNameResource) {
        this.fontNameResource = fontNameResource;
        return this;
    }

    public FontFamilyStyle getFontFamilyStyle() {
        return fontFamilyStyle;
    }

    public TextStyle setFontFamilyStyle(FontFamilyStyle fontFamilyStyle) {
        this.fontFamilyStyle = fontFamilyStyle;
        return this;
    }

    public Locale getFontLocale() {
        return fontLocale;
    }

    public TextStyle setFontLocale(Locale fontLocale) {
        this.fontLocale = fontLocale;
        return this;
    }

    public short getFontSize() {
        return fontSize;
    }

    public TextStyle setFontSize(short fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public TextStyle setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
        return this;
    }

    public boolean isBold() {
        return bold;
    }

    public TextStyle setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public boolean isItalic() {
        return italic;
    }

    public TextStyle setItalic(boolean italic) {
        this.italic = italic;
        return this;
    }

    public byte getUnderline() {
        return underline;
    }

    public TextStyle setUnderline(byte underline) {
        this.underline = underline;
        return this;
    }

    public boolean isUseTtfFontAttributes() {
        return useTtfFontAttributes;
    }

    public TextStyle setUseTtfFontAttributes(boolean useTtfFontAttributes) {
        this.useTtfFontAttributes = useTtfFontAttributes;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public TextStyle setColor(Color color) {
        this.color = color;
        return this;
    }
}
