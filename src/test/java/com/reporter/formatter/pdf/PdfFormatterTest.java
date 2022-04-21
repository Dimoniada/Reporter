package com.reporter.formatter.pdf;

import com.reporter.domain.Document;
import com.reporter.domain.DocumentItem;
import com.reporter.domain.Footer;
import com.reporter.domain.Heading;
import com.reporter.domain.Paragraph;
import com.reporter.domain.Separator;
import com.reporter.domain.Table;
import com.reporter.domain.TableCell;
import com.reporter.domain.TableHeaderCell;
import com.reporter.domain.TableHeaderRow;
import com.reporter.domain.TableRow;
import com.reporter.domain.Title;
import com.reporter.domain.styles.BorderStyle;
import com.reporter.domain.styles.FontFamilyStyle;
import com.reporter.domain.styles.LayoutStyle;
import com.reporter.domain.styles.LayoutTextStyle;
import com.reporter.domain.styles.StyleService;
import com.reporter.domain.styles.TextStyle;
import com.reporter.domain.styles.constants.BorderWeight;
import com.reporter.domain.styles.constants.Color;
import com.reporter.domain.styles.constants.FillPattern;
import com.reporter.formatter.BaseDocument;
import com.reporter.formatter.pdf.styles.PdfStyleService;
import com.reporter.utils.LocalizedNumberUtils;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutputStream;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileUrlResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

class PdfFormatterTest extends BaseDocument {
    public static final String expected = "Title 1\n" +
        "paragraph 1\n" +
        "column1 column2 (столбец2)\n" +
        "1,000 2,000\n" +
        "3,000 4,000\n" +
        "5,000 6,000\n" +
        "Test document v.1\n" +
        "Chapter 1\n" +
        "Chapter 1.1\n" +
        "Chapter 1.1.1\n" +
        "This is an example of text in paragraph\n" +
        "Column 1 Column 2\n" +
        "Cell 1.1 Cell 1.2\n" +
        "Cell 2.1 Cell 2.2\n" +
        "Cell 3.1 Cell 3.2\n" +
        "Cell 4.1 Cell 4.2\n" +
        "Chapter 2\n" +
        "Chapter 2.1\n" +
        "Chapter 2.1.1\n" +
        "This is an example of text in paragraph 2\n" +
        "Column 1 Column 2\n" +
        "Cell 1.1 Cell 1.2\n" +
        "Cell 2.1 Cell 2.2\n" +
        "Title 1\n" +
        "paragraph 1\n" +
        "shifted heading\n" +
        "1 column2\n" +
        "1,000 2,000\n" +
        "3,000 4 and some escape characters (символы) %;;;;;\\/\n" +
        "5,000 6,000";

    public StyleService styleService;
    public Document pdfDoc;

    @BeforeEach
    public void initDoc() throws Exception {
        super.initDoc();
        styleService = PdfStyleService.create("Cp1251");

        final TextStyle titleStyle1 =
            TextStyle.create()
                .setColor(Color.TEAL)
                .setBold(true)
                .setFontSize((short) 14)
                .setFontFamilyStyle(FontFamilyStyle.SANS_SERIF)
                .setFontNameResource("arial");
        final TextStyle paragraphStyle1 =
            TextStyle.create()
                .setColor(Color.BLUE)
                .setItalic(true)
                .setFontSize((short) 10)
                .setFontFamilyStyle(FontFamilyStyle.MONOSPACED);

        final TextStyle titleStyle2 =
            TextStyle.create()
                .setColor(Color.GREEN)
                .setBold(true)
                .setFontSize((short) 16)
                .setFontFamilyStyle(FontFamilyStyle.SANS_SERIF)
                .setFontNameResource("helvetica");
        final TextStyle paragraphStyle2 =
            TextStyle.create()
                .setColor(Color.GREY_25_PERCENT)
                .setItalic(true)
                .setFontSize((short) 12)
                .setFontFamilyStyle(FontFamilyStyle.SANS_SERIF)
                .setFontNameResource("helvetica");

        final TextStyle tableLabelStyle3 =
            TextStyle.create()
                .setColor(Color.GREEN)
                .setBold(true)
                .setFontSize((short) 12)
                .setFontFamilyStyle(FontFamilyStyle.SANS_SERIF)
                .setFontNameResource("tahoma");
        final TextStyle tableCellsStyle3 =
            TextStyle.create()
                .setColor(Color.ORANGE)
                .setFontSize((short) 8)
                .setFontFamilyStyle(FontFamilyStyle.SERIF)
                .setFontNameResource("times");
        final TextStyle tableHeaderCellStyle4 =
            TextStyle.create()
                .setColor(Color.RED_DARK)
                .setFontSize((short) 13)
                .setFontFamilyStyle(FontFamilyStyle.SANS_SERIF)
                .setFontNameResource("verdana");
        final TextStyle tableHeaderCellStyle5 =
            TextStyle.create()
                .setColor(Color.RED)
                .setFontSize((short) 14)
                .setFontFamilyStyle(FontFamilyStyle.SANS_SERIF)
                .setFontNameResource("verdana");

        final LayoutTextStyle cellStyle6 =
            LayoutTextStyle.create(
                TextStyle.create()
                    .setColor(Color.ORANGE)
                    .setFontSize((short) 8),
                LayoutStyle.create()
                    .setFillBackgroundColor(Color.GREY_25_PERCENT)
                    .setFillPattern(FillPattern.SOLID_FOREGROUND)
            );

        pdfDoc = Document.create()
            .setLabel("Тестовый pdf документ")
            .setAuthor("A1 Systems")
            .setDescription("описание темы/содержания файла")
            .addParts(
                Title.create()
                    .setText("Смысл сайта (шрифт arial,14)").setStyle(titleStyle1),
                Separator.create(BorderStyle.create(Color.TEAL, BorderWeight.THIN)),
                Heading.create(0)
                    .setText("Шрифт courierNew,10:").setStyle(paragraphStyle1),
                Paragraph.create()
                    .setText("Сайт рыбатекст поможет дизайнеру, верстальщику, вебмастеру сгенерировать" +
                        "несколько абзацев более менее осмысленного текста рыбы на русском языке, а начинающему " +
                        "оратору отточить навык публичных выступлений в домашних условиях. При создании генератора " +
                        "мы использовали небезизвестный универсальный код речей. Текст генерируется абзацами " +
                        "случайным образом от двух до десяти предложений в абзаце, что позволяет сделать текст более" +
                        " привлекательным и живым для визуально-слухового восприятия. По своей сути рыбатекст " +
                        "является альтернативой традиционному lorem ipsum, который вызывает у некторых людей " +
                        "недоумение при попытках прочитать рыбу текст. В отличии от lorem ipsum, текст рыба на " +
                        "русском языке наполнит любой макет непонятным смыслом и придаст неповторимый колорит " +
                        "советских времен. ").setStyle(paragraphStyle1),
                Separator.create(
                    BorderStyle.create(Color.GREEN_DARK, BorderWeight.THIN)
                ),
                Heading.create(1)
                    .setText("Подзаголовок (шрифт helvetica,16):").setStyle(titleStyle2),
                Paragraph.create()
                    .setText("helvetica,16,italic: Lorem ipsum dolor sit amet, consectetur adipiscing " +
                        "elit. Morbi " +
                        "sollicitudin lacus augue, ut tristique justo feugiat vel. Quisque id vestibulum enim," +
                        "eget convallis dolor. Vivamus dapibus hendrerit sodales. Fusce nisi orci, ultrices eget " +
                        "finibus ut, aliquam in dui. Etiam tincidunt enim in mi interdum tristique nec ut sem. Nunc " +
                        "gravida feugiat massa a sodales. Integer viverra molestie tellus. Praesent fermentum libero " +
                        "ut ultricies posuere. Sed ultrices pellentesque orci nec imperdiet. Praesent placerat " +
                        "sollicitudin semper. Praesent id mauris porta, eleifend mi eget, ultrices sapien. " +
                        "Suspendisse ultrices mi a volutpat ultricies. Nunc fringilla, velit vitae porttitor euismod," +
                        " ante erat pulvinar magna, ut tempor orci lacus quis lacus. Fusce suscipit lorem sed orci" +
                        " porttitor tempor. Morbi porta ante nulla, nec faucibus nunc volutpat quis. Nunc et nisi " +
                        "blandit, euismod felis id, malesuada mauris. ").setStyle(paragraphStyle2),
                Table.create()
                    .setLabel("Таблица (шрифт этой строки - tahoma,12):")
                    .setTableHeaderRow(
                        TableHeaderRow
                            .create().addParts(
                                TableHeaderCell.create().setText("verdana,13.").setStyle(tableHeaderCellStyle4),
                                TableHeaderCell.create().setText("verdana,14.").setStyle(tableHeaderCellStyle5)
                            )
                    )
                    .addParts(
                        TableRow.create()
                            .addParts(
                                TableCell.create().setText("1"),
                                TableCell.create().setText("2123123")
                            ),
                        TableRow.create()
                            .addParts(
                                TableCell.create().setText("3.14"),
                                TableCell.create().setText("4")
                            ),
                        TableRow.create()
                            .addParts(
                                TableCell.create().setText("all cells with times,8 font").setStyle(cellStyle6),
                                TableCell.create().setText("6")
                            )
                    )
                    .spreadStyleToParts(tableCellsStyle3)
                    .setStyle(tableLabelStyle3)
            );
    }

    @Test
    public void testPdfDocumentCreation() throws Throwable {
        LocalizedNumberUtils.localizeNumber("123", null, null);
        final var pdfFormatter = (PdfFormatter) PdfFormatter.create()
            .setEncoding("Cp1251");
        pdfFormatter.setFileName("pdfDocument");

        pdfFormatter.getStyleService().setFontService(fontService);

        Assertions.assertDoesNotThrow(() -> {
            final var documentHolder = pdfFormatter.handle(pdfDoc);

            documentHolder.close();
        });
    }

    /**
     * Tests {@link PdfFormatter#handle handle} call
     * and proper saving table in "doc1.pdf",
     * checks saved table as a text
     *
     * @throws Throwable Exception/IOException
     */
    @Test
    public void testSaveTableToNewFile() throws Throwable {
        final var pdfFormatter = (PdfFormatter) PdfFormatter.create()
            .setEncoding("Cp1251")
            .setStyleService(styleService);
        pdfFormatter.setFileName("test_file");
        Assertions.assertEquals("test_file", pdfFormatter.getFileName());

        final var documentHolder = pdfFormatter.handle(doc);

        if (Files.exists(documentHolder.getResource().getFile().toPath())) {
            final PdfReader pdfReader = new PdfReader(documentHolder.getResource().getFile());
            final PdfDocument doc1 = new PdfDocument(pdfReader);
            final ITextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
            final var currentText = PdfTextExtractor.getTextFromPage(doc1.getPage(2), strategy);
            pdfReader.close();
            documentHolder.close();
            Assertions.assertEquals(expected, currentText);
        }
    }

    @Test
    public void testSaveTableToResource() throws Throwable {
        final var pdfFormatter = (PdfFormatter) PdfFormatter.create()
            .setEncoding("Cp1251").setStyleService(styleService);
        final var resource = new FileUrlResource("testFile");
        pdfFormatter.setResource(resource);

        Assertions.assertEquals(resource, pdfFormatter.getResource());

        final var documentHolder = pdfFormatter.handle(doc);

        if (Files.exists(documentHolder.getResource().getFile().toPath())) {
            final PdfReader pdfReader = new PdfReader(documentHolder.getResource().getFile());
            final PdfDocument doc1 = new PdfDocument(pdfReader);
            final ITextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
            final var currentText = PdfTextExtractor.getTextFromPage(doc1.getPage(2), strategy);
            pdfReader.close();
            documentHolder.close();
            Assertions.assertEquals(expected, currentText);
        }
    }

    @Test
    public void testSaveTableToStream() throws Throwable {
        final var pdfFormatter = (PdfFormatter) PdfFormatter.create()
            .setEncoding("Cp1251").setStyleService(styleService);
        final var os = new ByteArrayOutputStream();
        pdfFormatter.setOutputStream(os);

        Assertions.assertEquals(os, pdfFormatter.getOutputStream());

        final var documentHolder = pdfFormatter.handle(doc);

        if (os.size() != 0) {
            final PdfReader pdfReader = new PdfReader(new ByteArrayInputStream(os.toByteArray()));
            final PdfDocument doc1 = new PdfDocument(pdfReader);
            final ITextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
            final var currentText = PdfTextExtractor.getTextFromPage(doc1.getPage(2), strategy);
            pdfReader.close();
            documentHolder.close();
            Assertions.assertEquals(expected, currentText);
        }
    }

    @Test
    public void testFormatterProperties() throws IOException {
        final var os = new ByteArrayOutputStream();
        final PdfWriter writer = new PdfWriter(os);
        final var pdfFormatter = (PdfFormatter) PdfFormatter.create()
            .setStyleService(styleService);

        final DecimalFormat df = new DecimalFormat("\u203000");

        final var pdfDoc = new PdfDocument(new PdfWriter(new PdfOutputStream(os)));
        final com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDoc);

        pdfFormatter
            .setWriter(writer)
            .setPdf(pdfDoc)
            .setEncoding("ISO-8859-1")
            .setDecimalFormat(df)
            .setDocument(document)
            .setOutputStream(os);
        Assertions.assertEquals(os, pdfFormatter.getOutputStream());
        Assertions.assertEquals(writer, pdfFormatter.getWriter());
        Assertions.assertEquals(pdfDoc, pdfFormatter.getPdf());
        Assertions.assertEquals("ISO-8859-1", pdfFormatter.getEncoding());
        Assertions.assertEquals(df, pdfFormatter.getDecimalFormat());
        Assertions.assertEquals(document, pdfFormatter.getDocument());
    }

    /**
     * Footer необходимо регистрирвать до добавления информации в документ
     *
     * @throws Throwable - handle()
     */
    @Test
    public void testFooter() throws Throwable {
        final var pdfFormatter = (PdfFormatter) PdfFormatter.create()
            .setEncoding("Cp1251");
        pdfFormatter.setFileName("test_file");
        Assertions.assertEquals("test_file", pdfFormatter.getFileName());

        ((List<DocumentItem>) doc.getParts())
            .add(0, Footer.create("simple footer"));

        final var documentHolder = pdfFormatter.handle(doc);

        if (Files.exists(documentHolder.getResource().getFile().toPath())) {
            final PdfReader pdfReader = new PdfReader(documentHolder.getResource().getFile());
            final PdfDocument doc1 = new PdfDocument(pdfReader);
            final ITextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
            final var currentText = PdfTextExtractor.getTextFromPage(doc1.getPage(2), strategy);
            pdfReader.close();
            documentHolder.close();
            Assertions.assertTrue(currentText.endsWith("simple footer"));
        }
    }
}
