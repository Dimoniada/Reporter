package com.reporter.formatter;

import com.reporter.formatter.csv.CsvFormatter;
import com.reporter.formatter.excel.XlsFormatter;
import com.reporter.formatter.excel.XlsxFormatter;
import com.reporter.formatter.html.HtmlFormatter;
import com.reporter.formatter.pdf.PdfFormatter;
import com.reporter.formatter.word.DocFormatter;
import com.reporter.formatter.word.DocxFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = {
    FormatterFactory.class, PdfFormatter.class,                             //FormatterFactory and Formatter-s
    XlsFormatter.class, XlsxFormatter.class,
    CsvFormatter.class, HtmlFormatter.class,
    DocFormatter.class, DocxFormatter.class
})
class FormatterFactoryTest extends BaseFormatterContext {

    public List<Class<? extends Formatter>> formattersClassList;

    final FormatterFactory formatterFactory;

    @Autowired
    FormatterFactoryTest(FormatterFactory formatterFactory, List<Formatter> formatters) {
        this.formatterFactory = formatterFactory;
        this.formattersClassList = formatters.stream().map(Formatter::getClass).collect(Collectors.toList());
    }

    @BeforeEach
    public void init() {
        super.initFormatterContext();
    }

    @Test
    void testCreateFormatterFactory() {
        Assertions.assertTrue(
            formattersClassList
                .containsAll(
                    formatterFactory
                        .getFormatterClassMap()
                        .values()
                )
        );

    }

    @Test
    void testCreateFormatter() {
        final var formatter = formatterFactory.createFormatter("doc", formatterContext);
        Assertions.assertEquals(DocFormatter.class, formatter.getClass());
    }
}