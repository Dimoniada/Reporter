package com.reporter.domain.styles.constants;

/**
 * Excel uses only ~40 colors (backward compatibility for .xls format),
 * passing Color to ExcelFormatter will lead it to find the most similar
 * native color (palette.findSimilarColor()).
 */
public enum Color {
    BLACK((short) 0x00, (short) 0x00, (short) 0x00),
    WHITE((short) 0xFF, (short) 0xFF, (short) 0xFF),
    RED_LIGHT((short) 0xFF, (short) 0x80, (short) 0x80),
    RED((short) 0xFF, (short) 0x00, (short) 0x00),
    RED_DARK((short) 0x80, (short) 0x00, (short) 0x00),
    GREEN_LIGHT((short) 0xCC, (short) 0xFF, (short) 0xCC),
    GREEN((short) 0x00, (short) 0xFF, (short) 0x00),
    GREEN_DARK((short) 0x00, (short) 0x33, (short) 0x00),
    BLUE((short) 0x00, (short) 0x00, (short) 0xFF),
    YELLOW((short) 0xFF, (short) 0xFF, (short) 0x00),
    ORANGE((short) 0xFF, (short) 0x99, (short) 0x00),
    PINK((short) 0xFF, (short) 0xAF, (short) 0xAF),
    VIOLET((short) 0xEE, (short) 0x82, (short) 0xEE),
    TEAL((short) 0x00, (short) 0x80, (short) 0x80),
    GREY((short) 0x80, (short) 0x80, (short) 0x80),
    GREY_25_PERCENT((short) 0xC0, (short) 0xC0, (short) 0xC0),
    GREY_50_PERCENT((short) 0x80, (short) 0x80, (short) 0x80),
    GREY_80_PERCENT((short) 0x33, (short) 0x33, (short) 0x33),
    LIGHT_TURQUOISE((short) 0xCC, (short) 0xFF, (short) 0xFF);

    private final short r;
    private final short g;
    private final short b;

    Color(short r, short g, short b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public String buildColorString() {
        return
            String.format("%02X", getRed()) +
            String.format("%02X", getGreen()) +
            String.format("%02X", getBlue());
    }

    public short getRed() {
        return r;
    }

    public short getGreen() {
        return g;
    }

    public short getBlue() {
        return b;
    }
}
