package com.reporter.formatter.html;

import com.google.common.base.Objects;
import com.reporter.domain.*;
import com.reporter.domain.styles.Style;
import com.reporter.domain.styles.StyleCondition;
import com.reporter.domain.styles.StyleService;
import com.reporter.domain.styles.TextStyle;
import com.reporter.domain.styles.constants.Color;
import com.reporter.domain.styles.constants.FillPattern;
import com.reporter.formatter.BaseDocument;
import com.reporter.formatter.DocumentHolder;
import com.reporter.formatter.html.styles.HtmlStyleService;
import com.reporter.formatter.html.tag.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileUrlResource;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;

public class HtmlFormatterTest extends BaseDocument {

    public static final String expected = "<!doctype html><html><head><meta charset=\"UTF-8\"><title>Test document" +
        "</title></head><body><h1>Title 1</h1><p style=\"background-color:#FFFFFF;border-bottom:#FFFFFF;" +
        "border-collapse:collapse;border-left:#FFFFFF;border-right:#FFFFFF;border-top:#FFFFFF;color:#000000;" +
        "font-family:courierNew,monospace;font-size:10pt;font-weight:bold;text-align:justify\">paragraph 1</p><table>" +
        "<tr><th style=\"background-color:#FFFFFF;border-bottom:double#000000;border-collapse:collapse;border-left:" +
        "double#000000;border-right:double#000000;border-top:double#000000;color:#000000;font-family:arial,monospace;" +
        "font-size:14pt;font-weight:bold;text-align:justify\">column1</th><th style=\"background-color:#FFFFFF;" +
        "border-bottom:double#000000;border-collapse:collapse;border-left:double#000000;border-right:double#000000;" +
        "border-top:double#000000;color:#000000;font-family:arial,monospace;font-size:14pt;font-weight:bold;" +
        "text-align:justify\">column2 (столбец2)</th></tr><tr><td style=\"color:#000000;font-family:arial,monospace;" +
        "font-size:14pt;font-weight:bold\">1,000</td><td style=\"color:#000000;font-family:arial,monospace;font-size:" +
        "14pt;font-weight:bold\">2,000</td></tr><tr><td style=\"color:#000000;font-family:arial,monospace;font-size:" +
        "14pt;font-weight:bold\">3,000</td><td style=\"color:#000000;font-family:arial,monospace;font-size:14pt;" +
        "font-weight:bold\">4,000</td></tr><tr><td style=\"color:#000000;font-family:arial,monospace;font-size:14pt;" +
        "font-weight:bold\">5,000</td><td style=\"color:#000000;font-family:arial,monospace;font-size:14pt;" +
        "font-weight:bold\">6,000</td></tr></table><h1 style=\"color:#000000;font-family:monospace;font-size:20pt\">" +
        "Test document v.1</h1>" +
        "<hr style=\"background-color:#FFFFFF;border-bottom:1px solid#008080;border-collapse:collapse;border-left:" +
        "#FFFFFF;border-right:#FFFFFF;border-top:#FFFFFF;text-align:justify\"><h1 style=\"background-color:#FFFFFF;" +
        "border-bottom:#FFFFFF;border-collapse:collapse;border-left:#FFFFFF;border-right:#FFFFFF;border-top:#FFFFFF;" +
        "color:#000000;font-family:courierNew,monospace;font-size:10pt;font-weight:bold;text-align:justify\">Chapter " +
        "1</h1><h2 style=\"background-color:#FFFFFF;border-bottom:#FFFFFF;border-collapse:collapse;border-left:" +
        "#FFFFFF;border-right:#FFFFFF;border-top:#FFFFFF;color:#000000;font-family:courierNew,monospace;font-size:" +
        "10pt;font-weight:bold;text-align:justify\">Chapter 1.1</h2><h3 style=\"background-color:#FFFFFF;" +
        "border-bottom:#FFFFFF;border-collapse:collapse;border-left:#FFFFFF;border-right:#FFFFFF;border-top:#FFFFFF;" +
        "color:#000000;font-family:courierNew,monospace;font-size:10pt;font-weight:bold;text-align:justify\">" +
        "Chapter 1.1.1</h3><p style=\"background-color:#FFFFFF;border-bottom:#FFFFFF;border-collapse:collapse;" +
        "border-left:#FFFFFF;border-right:#FFFFFF;border-top:#FFFFFF;color:#000000;font-family:courierNew,monospace;" +
        "font-size:10pt;font-weight:bold;text-align:justify\">This is an example of text in paragraph</p><table><tr>" +
        "<th style=\"background-color:#FFFFFF;border-bottom:double#000000;border-collapse:collapse;border-left:" +
        "double#000000;border-right:double#000000;border-top:double#000000;color:#000000;font-family:arial,monospace;" +
        "font-size:14pt;font-weight:bold;text-align:justify\">Column 1</th><th style=\"background-color:#FFFFFF;" +
        "border-bottom:double#000000;border-collapse:collapse;border-left:double#000000;border-right:double#000000;" +
        "border-top:double#000000;color:#000000;font-family:arial,monospace;font-size:14pt;font-weight:bold;" +
        "text-align:justify\">Column 2</th></tr><tr><td style=\"color:#000000;font-family:courierNew,monospace;" +
        "font-size:10pt;font-weight:bold\">Cell 1.1</td><td style=\"color:#000000;font-family:courierNew,monospace;" +
        "font-size:10pt;font-weight:bold\">Cell 1.2</td></tr><tr><td style=\"color:#000000;font-family:courierNew," +
        "monospace;font-size:10pt;font-weight:bold\">Cell 2.1</td><td style=\"color:#000000;font-family:courierNew," +
        "monospace;font-size:10pt;font-weight:bold\">Cell 2.2</td></tr><tr><td style=\"color:#000000;font-family:" +
        "courierNew,monospace;font-size:10pt;font-weight:bold\">Cell 3.1</td><td style=\"color:#000000;font-family:" +
        "courierNew,monospace;font-size:10pt;font-weight:bold\">Cell 3.2</td></tr><tr><td style=\"color:#000000;" +
        "font-family:courierNew,monospace;font-size:10pt;font-weight:bold\">Cell 4.1</td><td style=\"color:#000000;" +
        "font-family:courierNew,monospace;font-size:10pt;font-weight:bold\">Cell 4.2</td></tr></table><h1 style=" +
        "\"background-color:#FFFFFF;border-bottom:#FFFFFF;border-collapse:collapse;border-left:#FFFFFF;border-right:" +
        "#FFFFFF;border-top:#FFFFFF;color:#000000;font-family:courierNew,monospace;font-size:10pt;font-weight:bold;" +
        "text-align:justify\">Chapter 2</h1><h2 style=\"background-color:#FFFFFF;border-bottom:#FFFFFF;" +
        "border-collapse:collapse;border-left:#FFFFFF;border-right:#FFFFFF;border-top:#FFFFFF;color:#000000;" +
        "font-family:courierNew,monospace;font-size:10pt;font-weight:bold;text-align:justify\">Chapter 2.1</h2>" +
        "<h3 style=\"background-color:#FFFFFF;border-bottom:#FFFFFF;border-collapse:collapse;border-left:#FFFFFF;" +
        "border-right:#FFFFFF;border-top:#FFFFFF;color:#000000;font-family:courierNew,monospace;font-size:10pt;" +
        "font-weight:bold;text-align:justify\">Chapter 2.1.1</h3><p style=\"background-color:#FFFFFF;border-bottom:" +
        "#FFFFFF;border-collapse:collapse;border-left:#FFFFFF;border-right:#FFFFFF;border-top:#FFFFFF;color:#000000;" +
        "font-family:courierNew,monospace;font-size:10pt;font-weight:bold;text-align:justify\">This is an example" +
        " of text in paragraph 2</p><table><tr><th style=\"background-color:#FFFFFF;border-bottom:double#000000;" +
        "border-collapse:collapse;border-left:double#000000;border-right:double#000000;border-top:double#000000;" +
        "color:#000000;font-family:arial,monospace;font-size:14pt;font-weight:bold;text-align:justify\">Column 1</th>" +
        "<th style=\"background-color:#FFFFFF;border-bottom:double#000000;border-collapse:collapse;border-left:" +
        "double#000000;border-right:double#000000;border-top:double#000000;color:#000000;font-family:arial,monospace;" +
        "font-size:14pt;font-weight:bold;text-align:justify\">Column 2</th></tr><tr><td style=\"color:#000000;" +
        "font-family:courierNew,monospace;font-size:10pt;font-weight:bold\">Cell 1.1</td><td style=\"color:#000000;" +
        "font-family:courierNew,monospace;font-size:10pt;font-weight:bold\">Cell 1.2</td></tr><tr><td style=\"color:" +
        "#000000;font-family:courierNew,monospace;font-size:10pt;font-weight:bold\">Cell 2.1</td><td style=\"color:" +
        "#000000;font-family:courierNew,monospace;font-size:10pt;font-weight:bold\">Cell 2.2</td></tr></table><h1>" +
        "Title 1</h1><p>paragraph 1</p><h2>shifted heading</h2><table><tr><th>столбец1</th><th>column2</th></tr><tr>" +
        "<td style=\"color:#000000;font-family:courierNew,monospace;font-size:10pt;font-weight:bold\">1,000</td>" +
        "<td style=\"color:#000000;font-family:courierNew,monospace;font-size:10pt;font-weight:bold\">2,000</td>" +
        "</tr><tr><td style=\"color:#000000;font-family:courierNew,monospace;font-size:10pt;font-weight:bold\">3,000" +
        "</td><td style=\"color:#000000;font-family:courierNew,monospace;font-size:10pt;font-weight:bold\">4 and " +
        "some escape characters (символы) %;;;;;\\/</td></tr><tr><td style=\"color:#000000;font-family:courierNew," +
        "monospace;font-size:10pt;font-weight:bold\">5,000</td><td style=\"color:#000000;font-family:courierNew," +
        "monospace;font-size:10pt;font-weight:bold\">6,000</td></tr></table></body></html>";

    @Override
    @BeforeEach
    public void initDoc() throws Exception {
        super.initDoc();
    }

    /**
     * Test on uniq style, writing one in .html document
     *
     * @throws Throwable Exception/IOException
     */
    @Test
    public void testStyleWriting() throws Throwable {

        final Style titleStyle = TextStyle
            .create()
            .setFontNameResource("Brush Script MT")
            .setItalic(true)
            .setBold(true)
            .setFontSize((short) 35)
            .setColor(Color.GREEN)
            .setCondition(StyleCondition.create(o -> o instanceof Title, Title.class));

        final Style paragraphStyle = TextStyle
            .create()
            .setFontNameResource("Gill Sans")
            .setFontSize((short) 15)
            .setColor(Color.RED)
            .setCondition(StyleCondition.create(o -> o instanceof Paragraph, Paragraph.class));

        final StyleService styleService = HtmlStyleService.create()
            .addStyles(titleStyle, paragraphStyle);

        final var doc1 = Document.create().setLabel("doc1")
            .addParts(Heading.create(1).setText("testHeading"),
                Heading.create(2).setText("testHeading"),
                Title.create().setText("mainTitle"),
                Heading.create(3).setText("testHeading"),
                Paragraph.create().setText("testParagraph1"),
                Paragraph.create().setText("testParagraph2"),
                Paragraph.create().setText("testParagraph3"),
                Heading.create(2).setText("nextHeading"),
                Paragraph.create().setText("testParagraph4")
            );

        final HtmlFormatter htmlFormatter = HtmlFormatter.create().setStyleService(styleService);
        final var documentHolder = htmlFormatter.handle(doc1);
        final var text = Files.readString(documentHolder.getResource().getFile().toPath());
        Assertions.assertEquals(1, StringUtils.countOccurrencesOf(text,
            "{color:#00FF00;" +
                "font-family:Brush Script MT,monospace;" +
                "font-size:35pt;" +
                "font-style:italic;" +
                "font-weight:bold}"));
        Assertions.assertEquals(1, StringUtils.countOccurrencesOf(text,
            "{color:#FF0000;" +
                "font-family:Gill Sans,monospace;" +
                "font-size:15pt}"));
        documentHolder.close();
    }

    /**
     * Test on {@link HtmlFormatter#handle handle} call
     * and proper saving result in "fileName"
     *
     * @throws Throwable Exception/IOException
     */
    @Test
    public void testSaveToFile() throws Throwable {
        final var htmlFormatter = HtmlFormatter.create();
        final var documentHolder = htmlFormatter.handle(doc);
        final var text = Files.readString(documentHolder.getResource().getFile().toPath());
        documentHolder.close();

        final var styleCode = "_" + Integer.toHexString(Objects.hashCode(textStyleCell));
        Assertions.assertEquals(MessageFormat.format(expected, styleCode), text);
    }

    /**
     * Test on {@link HtmlFormatter#handle handle} call
     * and proper saving result in {@link FileUrlResource "resource"}
     *
     * @throws Throwable Exception/IOException
     */
    @Test
    public void testSaveToResource() throws Throwable {
        final var resource = new FileUrlResource("testFile.txt");
        final var htmlFormatter = HtmlFormatter.create().setResource(resource);
        final var documentHolder = htmlFormatter.handle(doc);
        final var text = Files.readString(documentHolder.getResource().getFile().toPath());

        documentHolder.close();

        final var styleCode = "_" + Integer.toHexString(Objects.hashCode(textStyleCell));
        Assertions.assertEquals(MessageFormat.format(expected, styleCode), text);
    }

    /**
     * Test on {@link HtmlFormatter#handle handle} call
     * and proper saving result to "temporary file"
     * with "Cp1251" encoding
     *
     * @throws Throwable Exception
     */
    @Test
    public void testSaveToTmpFile() throws Throwable {
        final var htmlFormatter = (HtmlFormatter) HtmlFormatter.create().setEncoding("UTF-8");
        final var documentHolder = htmlFormatter.handle(doc);
        final var text = Files.readString(documentHolder.getResource().getFile().toPath(), StandardCharsets.UTF_8);
        documentHolder.close();

        final var styleCode = "_" + Integer.toHexString(Objects.hashCode(textStyleCell));
        Assertions.assertEquals(MessageFormat.format(expected, styleCode), text);
    }

    /**
     * Test on {@link HtmlFormatter#handle handle} call
     * and proper saving result to "outputStream"
     *
     * @throws Throwable IOException/Exception
     */
    @Test
    public void testSaveToStream() throws Throwable {
        final var os = new ByteArrayOutputStream();
        final var htmlFormatter = HtmlFormatter.create().setOutputStream(os);
        final var documentHolder = htmlFormatter.handle(doc);
        final var text = os.toString(StandardCharsets.UTF_8);
        documentHolder.close();

        final var styleCode = "_" + Integer.toHexString(Objects.hashCode(textStyleCell));
        Assertions.assertEquals(MessageFormat.format(expected, styleCode), text);
    }

    /**
     * Test on exception throwing when tag is malformed (h7)
     *
     * @throws Throwable IOException
     */
    @Test
    public void checkTags() throws Throwable {
        final var doc = Document.create()
            .addPart(Heading.create(1).setDepth((short) 7).setText("test"));
        final var htmlFormatter = HtmlFormatter.create();
        final Exception e = Assertions.assertThrows(Exception.class, () -> htmlFormatter.handle(doc));
        Files.deleteIfExists(htmlFormatter.getResource().getFile().toPath());
        Assertions.assertTrue("Malformed tag: h7".contains(e.getMessage()));
    }

    /**
     * Test on styles writing with old tags
     */
    @Test
    public void checkHtml4StyleTags() throws Throwable {
        final var htmlStyleService = HtmlStyleService.create().setUseHtml4Tags(true);
        final var interlinearStyle = layoutStyle1.clone().setFillPattern(FillPattern.SOLID_FOREGROUND);
        interlinearStyle
            .setCondition(
                StyleCondition.create(o -> {
                        if (o instanceof Table) {
                            return true;
                        }
                        if (o instanceof TableRow) {
                            final var row = (TableRow) o;
                            final var cells = (ArrayList<TableCell>) row.getParts();
                            interlinearStyle.setFillForegroundColor(
                                row.getRowIndex() % 2 == 0 ? Color.GREY_25_PERCENT : Color.WHITE);
                            interlinearStyle.setFillBackgroundColor(
                                row.getRowIndex() % 2 == 0 ? Color.GREY_25_PERCENT : Color.WHITE);
                            final Style cellStyle;
                            try {
                                cellStyle = interlinearStyle.clone().setCondition(null);
                                cells.forEach(cell -> cell.setStyle(cellStyle));
                            } catch (CloneNotSupportedException ignored) {
                            }
                            return true;
                        }
                        return false;
                    },
                    TableRow.class
                )
            );
        htmlStyleService.addStyles(interlinearStyle);

        final String expected = "<!doctype html><html><head><meta charset=\"Cp1251\"><title>Test document</title>" +
            "</head><body><h1>Title 1</h1><p align=\"left\" bgcolor=\"#FFFFFF\"><font color=\"#000000\" face=" +
            "\"monospace\" size=\"10\">paragraph 1</font></p><table bgcolor=\"#FFFFFF\" border=\"1\" cellspacing=" +
            "\"0\"><tr><th align=\"left\" bgcolor=\"#FFFFFF\"><font color=\"#000000\" face=\"sans-serif\" " +
            "size=\"14\">column1</font></th><th align=\"left\" bgcolor=\"#FFFFFF\"><font color=\"#000000\" face=" +
            "\"sans-serif\" size=\"14\">column2 (столбец2)</font></th></tr><tr><td align=\"left\" bgcolor=" +
            "\"#C0C0C0\">1</td><td align=\"left\" bgcolor=\"#C0C0C0\">2</td></tr><tr><td align=\"left\" bgcolor=" +
            "\"#FFFFFF\">3</td><td align=\"left\" bgcolor=\"#FFFFFF\">4</td></tr><tr><td align=\"left\" bgcolor=" +
            "\"#C0C0C0\">5</td><td align=\"left\" bgcolor=\"#C0C0C0\">6</td></tr></table><h1><font color=\"#000000\"" +
            " face=\"serif\" size=\"20\">Test document v.1</font></h1>" +
            "<hr align=\"left\" bgcolor=\"#FFFFFF\"><h1 align=\"left\" bgcolor=\"#FFFFFF\">" +
            "<font color=\"#000000\" face=\"monospace\" size=\"10\">Chapter 1</font></h1>" +
            "<h2 align=\"left\" bgcolor=\"#FFFFFF\"><font color=\"#000000\" face=\"monospace\" size=\"10\">" +
            "Chapter 1.1</font></h2><h3 align=\"left\" bgcolor=\"#FFFFFF\">" +
            "<font color=\"#000000\" face=\"monospace\" size=\"10\">Chapter 1.1.1</font></h3>" +
            "<p align=\"left\" bgcolor=\"#FFFFFF\"><font color=\"#000000\" face=\"monospace\" size=\"10\">" +
            "This is an example of text in paragraph</font></p><table bgcolor=\"#FFFFFF\" border=\"1\" " +
            "cellspacing=\"0\"><tr><th align=\"left\" bgcolor=\"#FFFFFF\"><font color=\"#000000\" " +
            "face=\"sans-serif\" size=\"14\">Column 1</font></th><th align=\"left\" bgcolor=\"#FFFFFF\">" +
            "<font color=\"#000000\" face=\"sans-serif\" size=\"14\">Column 2</font></th></tr><tr><td " +
            "align=\"left\" bgcolor=\"#C0C0C0\">Cell 1.1</td><td align=\"left\" bgcolor=\"#C0C0C0\">" +
            "Cell 1.2</td></tr><tr><td align=\"left\" bgcolor=\"#FFFFFF\">Cell 2.1</td>" +
            "<td align=\"left\" bgcolor=\"#FFFFFF\">Cell 2.2</td></tr><tr><td align=\"left\" bgcolor=" +
            "\"#C0C0C0\">Cell 3.1</td><td align=\"left\" bgcolor=\"#C0C0C0\">Cell 3.2</td></tr><tr><td align=" +
            "\"left\" bgcolor=\"#FFFFFF\">Cell 4.1</td><td align=\"left\" bgcolor=\"#FFFFFF\">Cell 4.2</td>" +
            "</tr></table><h1 align=\"left\" bgcolor=\"#FFFFFF\"><font color=\"#000000\" face=" +
            "\"monospace\" size=\"10\">Chapter 2</font></h1><h2 align=\"left\" bgcolor=\"#FFFFFF\"><font color=" +
            "\"#000000\" face=\"monospace\" size=\"10\">Chapter 2.1</font></h2><h3 align=\"left\" bgcolor=" +
            "\"#FFFFFF\"><font color=\"#000000\" face=\"monospace\" size=\"10\">Chapter 2.1.1</font></h3>" +
            "<p align=\"left\" bgcolor=\"#FFFFFF\"><font color=\"#000000\" face=\"monospace\" size=\"10\">" +
            "This is an example of text in paragraph 2</font></p><table bgcolor=\"#FFFFFF\" border=\"1\" " +
            "cellspacing=\"0\"><tr><th align=\"left\" bgcolor=\"#FFFFFF\"><font color=\"#000000\" face=" +
            "\"sans-serif\" size=\"14\">Column 1</font></th><th align=\"left\" bgcolor=\"#FFFFFF\"><font color=" +
            "\"#000000\" face=\"sans-serif\" size=\"14\">Column 2</font></th></tr><tr><td align=\"left\" " +
            "bgcolor=\"#C0C0C0\">Cell 1.1</td><td align=\"left\" bgcolor=\"#C0C0C0\">Cell 1.2</td></tr><tr>" +
            "<td align=\"left\" bgcolor=\"#FFFFFF\">Cell 2.1</td><td align=\"left\" bgcolor=\"#FFFFFF\">" +
            "Cell 2.2</td></tr></table><h1>Title 1</h1><p>paragraph 1</p><h2>shifted heading</h2><table bgcolor=" +
            "\"#FFFFFF\" border=\"1\" cellspacing=\"0\"><tr><th>столбец1</th><th>column2</th></tr><tr><td align=" +
            "\"left\" bgcolor=\"#C0C0C0\">1</td><td align=\"left\" bgcolor=\"#C0C0C0\">2</td></tr><tr><td align=" +
            "\"left\" bgcolor=\"#FFFFFF\">3</td><td align=\"left\" bgcolor=\"#FFFFFF\">" +
            "4 and some escape characters (символы) %;;;;;\\/</td></tr><tr><td align=\"left\" bgcolor=" +
            "\"#C0C0C0\">5</td><td align=\"left\" bgcolor=\"#C0C0C0\">6</td></tr></table></body></html>";
        final HtmlFormatter htmlFormatter = HtmlFormatter
            .create()
            .setEncoding("Cp1251")
            .setStyleService(htmlStyleService);
        final var documentHolder = htmlFormatter.handle(doc);
        final var text = Files.readString(
            documentHolder.getResource().getFile().toPath(),
            Charset.forName("Cp1251")
        );
        documentHolder.close();
        Assertions.assertEquals(expected, text);
    }

    @Test
    public void testHtmlParagraph() throws Throwable {
        final Paragraph p = Paragraph.create("test_text");
        final var htmlParagraph = new HtmlParagraph();
        testHtmlTag(p, htmlParagraph, Paragraph.class);
    }

    @Test
    public void testHtmlTitle() throws Throwable {
        final Title t = Title.create("test_text");
        final var htmlTitle = new HtmlH1();
        testHtmlTag(t, htmlTitle, Title.class);
    }

    @Test
    public void testHtmlFooter() throws Throwable {
        final Footer f = Footer.create("test_text");
        final var htmlFooter = new HtmlFooter();
        testHtmlTag(f, htmlFooter, Footer.class);
    }

    @Test
    public void testHtmlHeading() throws Throwable {
        final Heading h = Heading.create("test_text", 1);
        final var htmlHeading = new HtmlHeading(1);
        testHtmlTag(h, htmlHeading, Heading.class);
    }

    @Test
    public void testHtmlTableCell() throws Throwable {
        final TableCell tc = TableCell.create("test_text");
        final var htmlTableCell = new HtmlTableCell();
        testHtmlTag(tc, htmlTableCell, TableCell.class);
    }

    @Test
    public void testHtmlTableHeaderCell() throws Throwable {
        final TableHeaderCell thc = TableHeaderCell.create("test_text");
        final var htmlTableHeaderCell = new HtmlTableHeaderCell();
        testHtmlTag(thc, htmlTableHeaderCell, TableHeaderCell.class);
    }

    @Test
    public void testHtmlTableRow() throws Throwable {
        final var tc = TableCell.create("test_text");
        final var tr = TableRow.create(tc);
// Case when tag is without parameters
        final var htmlFormatterVisitor = new HtmlFormatterVisitor();
        final var styleService = HtmlStyleService.create();
        htmlFormatterVisitor.setStyleService(styleService);
        htmlFormatterVisitor.setOutputStreamWriter(writer);

        htmlFormatterVisitor.visitTableRow(tr);

        writer.flush();
        Assertions.assertEquals("<tr><td>test_text</td></tr>", os.toString());
        os.reset();

// Case of layout style
        tc.setStyle(layoutStyle1);
        htmlFormatterVisitor.visitTableRow(tr);
        writer.flush();

        Assertions.assertEquals(
            "<tr><td style=\"background-color:#FFFFFF;" +
                "border-bottom:double#000000;" +
                "border-collapse:collapse;" +
                "border-left:double#000000;" +
                "border-right:double#000000;" +
                "border-top:double#000000;" +
                "text-align:justify\">test_text</td></tr>", os.toString());
        os.reset();

// Case of the named style class
        tc.setStyle(null);         // remove inner style

        layoutStyle1.setCondition(StyleCondition.create(o -> o instanceof TableCell, TableCell.class));
        styleService.addStyles(layoutStyle1);

        doc.addPart(tr);

        final HtmlFormatter htmlFormatter = HtmlFormatter.create().setStyleService(styleService);
        htmlFormatter.setOutputStream(os);
        final DocumentHolder documentHolder = htmlFormatter.handle(doc);

        Assertions.assertTrue(StringUtils.endsWithIgnoreCase(os.toString(),
            "<tr><td class=\"_" + Integer.toHexString(Objects.hashCode(layoutStyle1)) + "\">test_text</td></tr>" +
                "</body></html>"));
        documentHolder.close();
    }

    @Test
    public void testHtmlTableHeaderRow() throws Throwable {
        final var thc = TableHeaderCell.create("test_text");
        final var thr = TableHeaderRow.create(thc);
// Case when tag is without parameters
        final var htmlFormatterVisitor = new HtmlFormatterVisitor();
        final var styleService = HtmlStyleService.create();
        htmlFormatterVisitor.setStyleService(styleService);
        htmlFormatterVisitor.setOutputStreamWriter(writer);

        htmlFormatterVisitor.visitTableHeaderRow(thr);

        writer.flush();

        Assertions.assertEquals("<tr><th>test_text</th></tr>", os.toString());
        os.reset();
// Case of the layout style
        thc.setStyle(layoutStyle1);
        htmlFormatterVisitor.visitTableHeaderRow(thr);
        writer.flush();

        Assertions.assertEquals("<tr><th style=\"background-color:#FFFFFF;" +
            "border-bottom:double#000000;" +
            "border-collapse:collapse;" +
            "border-left:double#000000;" +
            "border-right:double#000000;" +
            "border-top:double#000000;" +
            "text-align:justify\">test_text</th></tr>", os.toString());
        os.reset();

// Case of the named class style
        thc.setStyle(null);          // remove inner style
        layoutStyle1.setCondition(StyleCondition.create(o -> o instanceof TableHeaderCell, TableHeaderCell.class));
        styleService.addStyles(layoutStyle1);

        doc.addPart(thr);

        final HtmlFormatter htmlFormatter = HtmlFormatter.create().setStyleService(styleService);
        htmlFormatter.setOutputStream(os);
        final DocumentHolder documentHolder = htmlFormatter.handle(doc);

        Assertions.assertTrue(StringUtils.endsWithIgnoreCase(os.toString(),
            "<tr><th class=\"_" + Integer.toHexString(Objects.hashCode(layoutStyle1)) + "\">test_text</th></tr>" +
                "</body></html>"));
        documentHolder.close();
    }

    @Test
    public void testHtmlTable() throws Throwable {
        final var thc = TableHeaderCell.create("test_text1");
        final var thr = TableHeaderRow.create(thc);
        final var tc = TableCell.create("test_text2");
        final var tr = TableRow.create(tc);

        final var t = Table.create(thr).addParts(tr);

// Case when tag is without parameters
        final var htmlFormatterVisitor = new HtmlFormatterVisitor();
        htmlFormatterVisitor.setOutputStreamWriter(writer);

        final var styleService = HtmlStyleService.create();
        htmlFormatterVisitor.setStyleService(styleService);

        htmlFormatterVisitor.visitTable(t);

        writer.flush();

        Assertions.assertEquals(
            "<table><tr><th>test_text1</th></tr><tr><td>test_text2</td></tr></table>",
            os.toString()
        );
        os.reset();
// Case of the layout style
        thc.setStyle(layoutStyle1);

        htmlFormatterVisitor.visitTable(t);

        writer.flush();

        Assertions.assertEquals("<table><tr><th style=\"background-color:#FFFFFF;" +
            "border-bottom:double#000000;" +
            "border-collapse:collapse;" +
            "border-left:double#000000;" +
            "border-right:double#000000;" +
            "border-top:double#000000;" +
            "text-align:justify\">test_text1</th></tr><tr><td>test_text2</td></tr></table>", os.toString());
        os.reset();

// Case of the named class style
        thc.setStyle(null);         // remove inner style
        layoutStyle1.setCondition(StyleCondition.create(o -> o instanceof TableHeaderCell, TableHeaderCell.class));
        styleService.addStyles(layoutStyle1);

        doc.addPart(thr);

        final HtmlFormatter htmlFormatter = HtmlFormatter.create().setStyleService(styleService);
        htmlFormatter.setOutputStream(os);
        final DocumentHolder documentHolder = htmlFormatter.handle(doc);

        Assertions.assertTrue(
            StringUtils.endsWithIgnoreCase(
                os.toString(),
                "<tr><th class=\"_" +
                    Integer.toHexString(Objects.hashCode(layoutStyle1)) +
                    "\">test_text1</th></tr></body></html>"
            )
        );
        documentHolder.close();
    }

    public void testHtmlTag(
        DocumentItem item,
        HtmlTag baseTag,
        Class<? extends DocumentItem> itemClass
    ) throws Throwable {
// Case when tag is without parameters

        final var htmlFormatterVisitor = new HtmlFormatterVisitor();
        final var styleService = HtmlStyleService.create();
        htmlFormatterVisitor.setStyleService(styleService);
        htmlFormatterVisitor.setOutputStreamWriter(writer);
        handleItem(item, htmlFormatterVisitor);
        writer.flush();

        Assertions.assertEquals("<" + baseTag + ">test_text</" + baseTag + ">", os.toString());
        os.reset();
// Case of the layout style
        item.setStyle(layoutStyle1);
        handleItem(item, htmlFormatterVisitor);
        writer.flush();

        Assertions.assertEquals(
            "<" + baseTag + " style=\"background-color:#FFFFFF;" +
                "border-bottom:double#000000;" +
                "border-collapse:collapse;" +
                "border-left:double#000000;" +
                "border-right:double#000000;" +
                "border-top:double#000000;" +
                "text-align:justify\">test_text</" + baseTag + ">", os.toString());
        os.reset();
// Case of the text style
        item.setStyle(textStyle1);
        handleItem(item, htmlFormatterVisitor);
        writer.flush();

        Assertions.assertEquals(
            "<" + baseTag + " style=\"color:#000000;" +
                "font-family:" + textStyle1.getFontNameResource() + ",monospace;" +
                "font-size:14pt;" +
                "font-weight:bold\">test_text</" + baseTag + ">", os.toString());
        os.reset();
// Case of the named style class
        item.setStyle(null);         // remove inner style
        layoutStyle1.setCondition(StyleCondition.create(o -> o.getClass().equals(itemClass), itemClass));
        styleService.addStyles(layoutStyle1);

        doc.addPart(item);


        final var osRes = new ByteArrayOutputStream();

        final HtmlFormatter htmlFormatter = HtmlFormatter.create().setStyleService(styleService);
        htmlFormatter.setOutputStream(osRes);
        final DocumentHolder documentHolder = htmlFormatter.handle(doc);

        Assertions.assertTrue(StringUtils.endsWithIgnoreCase(osRes.toString(),
            "<" + baseTag + " class=\"_" +
                Integer.toHexString(Objects.hashCode(layoutStyle1)) +
                "\">test_text</" + baseTag + ">" +
                "</body></html>"));
        documentHolder.close();
    }

    public void handleItem(DocumentItem item, HtmlFormatterVisitor htmlFormatterVisitor) throws Throwable {
        if (item instanceof Paragraph) {
            htmlFormatterVisitor.visitParagraph((Paragraph) item);
        } else if (item instanceof Title) {
            htmlFormatterVisitor.visitTitle((Title) item);
        } else if (item instanceof Heading) {
            htmlFormatterVisitor.visitHeading((Heading) item);
        } else if (item instanceof Table) {
            htmlFormatterVisitor.visitTable((Table) item);
        } else if (item instanceof TableHeaderCell) {
            htmlFormatterVisitor.visitTableHeaderCell((TableHeaderCell) item);
        } else if (item instanceof TableHeaderRow) {
            htmlFormatterVisitor.visitTableHeaderRow((TableHeaderRow) item);
        } else if (item instanceof TableCell) {
            htmlFormatterVisitor.visitTableCell((TableCell) item);
        } else if (item instanceof TableRow) {
            htmlFormatterVisitor.visitTableRow((TableRow) item);
        } else if (item instanceof Footer) {
            htmlFormatterVisitor.visitFooter((Footer) item);
        }
    }

}
