package com.reporter.formatter.word;

import com.reporter.domain.*;
import com.reporter.formatter.BaseDocument;
import com.reporter.formatter.DocumentHolder;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeaderFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class WordFormatterTest extends BaseDocument {

    public static final String expectedTextParagraphs = "Title 1;paragraph 1;\n;Test document v.1;\n" +
        ";Chapter 1;Chapter 1.1;Chapter 1.1.1;This is an example of text in paragraph;Chapter 2;Chapter 2.1;" +
        "Chapter 2.1.1;This is an example of text in paragraph 2;Title 1;paragraph 1;shifted heading";

    public static final String expectedTextTables = "column1\tcolumn2 (столбец2)\n1,000\t2,000\n" +
        "3,000\t4,000\n5,000\t6,000\n;Column 1\tColumn 2\nCell 1.1\tCell 1.2\nCell 2.1\tCell 2.2\n" +
        "Cell 3.1\tCell 3.2\nCell 4.1\tCell 4.2\n;Column 1\tColumn 2\nCell 1.1\tCell 1.2\nCell 2.1\tCell 2.2\n" +
        ";столбец1\tcolumn2\n1,000\t2,000\n3,000\t4 and some escape characters (символы) %;;;;;\\/\n5,000\t6,000\n";

    @BeforeEach
    public void initStyledDoc() throws Exception {
        super.initDoc();
    }


    /**
     * Test {@link DocxFormatter#handle handle} call
     * and proper saving texts in "Test document.docx"
     *
     * @throws Throwable Exception/IOException
     */
    @Test
    public void testSaveTextToDocxFile() throws Throwable {
        final WordFormatter docxFormatter = DocxFormatter.create();
        final DocumentHolder documentHolder = docxFormatter.handle(doc);

        final var docx = new XWPFDocument(documentHolder.getResource().getInputStream());
        final var paragraphs = docx.getParagraphs();

        final var actualParagraphs =
            paragraphs
                .stream()
                .map(XWPFParagraph::getText)
                .collect(Collectors.joining(";"));

        Assertions.assertEquals(expectedTextParagraphs, actualParagraphs);

        final var tables = docx.getTables();

        final var actualTables =
            tables
                .stream()
                .map(XWPFTable::getText)
                .collect(Collectors.joining(";"));

        Assertions.assertEquals(expectedTextTables, actualTables);

        documentHolder.close();
    }

    @Test
    public void testFooter() throws Throwable {
        final var docxFormatter = (DocxFormatter) DocxFormatter.create();
        docxFormatter.setFileName("test_file");

        ((List<DocumentItem>) doc.getParts())
            .add(0, Footer.create("simple footer"));

        final var documentHolder = docxFormatter.handle(doc);

        Assertions.assertDoesNotThrow(() -> {
            final var file = documentHolder.getResource().getFile();
            if (Files.exists(file.toPath())) {
                Assertions.assertEquals("test_file.docx", file.getName());

                final var docx = new XWPFDocument(documentHolder.getResource().getInputStream());
                final var actual =
                    docx.getFooterList()
                        .stream()
                        .map(XWPFHeaderFooter::getText)
                        .collect(Collectors.joining(";"));

                Assertions.assertTrue(actual.endsWith("simple footer\n"));
            }
        });

        documentHolder.close();
    }

    /**
     * Test {@link DocFormatter#handle handle} call
     * and proper saving texts in "Test document.doc"
     *
     * @throws Throwable Exception/IOException
     */
//    @Test
    public void testSaveTextToDocFile() throws Throwable {
        final WordFormatter docFormatter = DocFormatter.create();
        final DocumentHolder documentHolder = docFormatter.handle(doc);
        documentHolder.close();
    }
}
