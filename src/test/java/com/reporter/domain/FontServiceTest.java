package com.reporter.domain;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.reporter.domain.styles.FontFamilyStyle;
import com.reporter.domain.styles.TextStyle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

class FontServiceTest {
    public final Locale localeEn = Locale.ENGLISH;
    public final Locale localeZh = Locale.forLanguageTag("zh");
    public final Locale localeRu = Locale.forLanguageTag("ru");
    public FontService fontService;

    @BeforeEach
    public void initFontService() throws IOException, FontFormatException {
        fontService = FontService.create().initializeFonts();
    }

    @Test
    void testCreateFontService() {
        final var localeList = fontService.getLocaleSet();
        Assertions.assertTrue(
            localeList.contains(localeEn)
            && localeList.contains(localeZh)
            && localeList.contains(localeRu)
        );
    }

    @Test
    void testToPdfFontFamily() {
        Assertions.assertEquals("SansSerif", FontService.toPdfFontFamily(FontFamilyStyle.SANS_SERIF));
        Assertions.assertEquals("Serif", FontService.toPdfFontFamily(FontFamilyStyle.SERIF));
        Assertions.assertEquals("Monospaced", FontService.toPdfFontFamily(FontFamilyStyle.MONOSPACED));
    }

    @Test
    void testInitializeFonts() {
        Assertions.assertDoesNotThrow(
            () -> fontService.initializeFonts()
        );
        Assertions.assertTrue(
            fontService
                .getFonts()
                .values()
                .stream()
                .anyMatch(Objects::nonNull)
        );
    }

    @Test
    void testCheckAvailableFontsLocale() {
        Assertions.assertTrue(
            fontService.checkAvailableFontsLocale(Locale.ENGLISH)
        );
        Assertions.assertTrue(
            fontService.checkAvailableFontsLocale(Locale.forLanguageTag("zh"))
        );
        Assertions.assertTrue(
            fontService.checkAvailableFontsLocale(Locale.forLanguageTag("ru"))
        );
    }

    @Test
    void testGetFontResource() {
        final var testStyle = TextStyle.create("helvetica_SansSerif_(en-ru OpenSans).ttf");
        var fontResource = new AtomicReference<byte[]>();
        Assertions.assertDoesNotThrow(() -> {
            fontService.initializeFonts();
            fontResource.set(fontService.getFontResource(testStyle, Locale.forLanguageTag("en")));
        });
        Assertions.assertDoesNotThrow(() ->
            PdfFontFactory.createFont(
                fontResource.get(),
                PdfEncodings.IDENTITY_H,
                PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED
            )
        );
    }
}