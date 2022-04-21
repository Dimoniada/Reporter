package com.reporter;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;
import com.reporter.domain.*;
import com.reporter.domain.styles.BorderStyle;
import com.reporter.domain.styles.constants.BorderWeight;
import com.reporter.domain.styles.constants.Color;
import com.reporter.formatter.FormatterContext;
import com.reporter.formatter.FormatterFactory;
import com.reporter.formatter.csv.CsvFormatter;
import com.reporter.formatter.excel.XlsFormatter;
import com.reporter.formatter.excel.XlsxFormatter;
import com.reporter.formatter.html.HtmlFormatter;
import com.reporter.formatter.pdf.PdfFormatter;
import com.reporter.formatter.word.DocFormatter;
import com.reporter.formatter.word.DocxFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(classes = {
    FormatterFactory.class, PdfFormatter.class,                             //FormatterFactory and Formatter-s
    XlsFormatter.class, XlsxFormatter.class,
    CsvFormatter.class, HtmlFormatter.class,
    DocFormatter.class, DocxFormatter.class
})
public class TutorialTest {

    @Autowired
    protected FormatterFactory formatterFactory;

    static Stream<Arguments> testArguments() {
        return Stream.of(
            Arguments.of(
                "PDF test",
                new ExportContext()                                             //For export to pdf
                .setFormat("pdf")
                .setLocale("en")
                .setColumns(Arrays.asList("id", "comment", "name"))
                .setEncoding("UTF-8")
                .setCsvDelimiter(';')
            ),
            Arguments.of(
                "XLSX test",
                new ExportContext()                                             //For export to xlsx
                .setFormat("xlsx")
                .setLocale("en")
                .setColumns(Arrays.asList("id", "comment", "name"))
                .setEncoding("UTF-8")
                .setCsvDelimiter(';')
            ),
            Arguments.of(
                "HTML test",
                new ExportContext()                                             //For export to html
                .setFormat("html")
                .setLocale("en")
                .setColumns(Arrays.asList("id", "comment", "name"))
                .setEncoding("UTF-8")
                .setCsvDelimiter(';')
            ),
            Arguments.of(
                "DOCX test",
                new ExportContext()                                             //For export to docx
                .setFormat("docx")
                .setLocale("en")
                .setColumns(Arrays.asList("id", "comment", "name"))
                .setEncoding("UTF-8")
                .setCsvDelimiter(';')
            )
        );
    }

    /**
     * @param exportContext could appear from client call for example
     */
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testArguments")
    void createReportForWeb(String testName, ExportContext exportContext) {
        //Required parameters for report creation
        final var format = exportContext.getFormat();
        final var locale = exportContext.getLocale();
        final var columns = exportContext.getColumns(); //(DB-)Table column names for export
        final var encoding = exportContext.getEncoding();
        final var csvDelimiter = exportContext.getCsvDelimiter(); //If format == "csv"

        final List<?> data = List.of(                   //Getting data here from db for instance
            new Person(1, "Name1", "Comment1"),
            new Person(2, "Имя2 с !;№% номерами", "абракадабра с \\ЭЭ последовательностью"),
            new Person(3, "Name3", "Comment3"),
            new Person(4, "Name4", "Comment4")
        );
        /*Starting to prepare table in final report*/
        final var table = ReportTable.create();
        if (columns.isEmpty()) {                        //If we don't know columns names in db
            table.setTableHeaderRowFromData(true);      //we can get them automatically from db
        } else {                                        //If we know columns names in db
            final var tableHeaderRow = TableHeaderRow.create();
            columns.forEach(c ->                        //then we might customize visual names in report
                tableHeaderRow
                    .addPart(TableHeaderCell
                        .create()
                        .setText(c)                     //Choice of arbitrary table column names in report
                        .setAliasName(c)                //to be match real column names from db
                    )
            );
            table.setTableHeaderRow(tableHeaderRow);
        }
        table.addDataList(data);                        //Filling table with data
        /*Preparing a formatter that forms report*/
        final var timeZone = LocaleContextHolder.getTimeZone();
        final var decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        final var formatterContext =   //FormatterContext needs to create Formatter
            FormatterContext.create(
                encoding,                               //Final file's encoding with report
                locale,                                 //Glyphs locale in report
                timeZone,                               //TimeZone needs only for footer with time in some reports
                decimalFormat,                          //Number format in report
                csvDelimiter                            //Only for *.csv files (delimiter)
            );
        final var formatter = formatterFactory.createFormatter(format, formatterContext);

        final var creationDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        final var documentName = "testReportTable"      //Forming full file name of report
            + "_"
            + creationDate
            + "."
            + formatter.getExtension();
        final var formattedDateTime = formatterContext.createFormattedZoneDateTime();

        final var document =                   //Creating all document in report
            Document
                .create()
                .setLabel(documentName)                 //File or document name
                .addParts(
                    table,                              //Put our table here as a part of document
                    Paragraph.create(),
                    Separator.create(
                        BorderStyle
                            .create(Color.TEAL, BorderWeight.THIN)
                    ),
                    Paragraph
                        .create()
                        .setText(formattedDateTime)
                        .setStyle(formatterContext.createStyleFooter())
                );
        /*Time to stylize our report =)*/

        //We can do it in 3 ways (from simplest to hardest):

        //1) By assigning a Style to a separate DocumentItem, like item.setStyle(Style)
        //2) By spreading a Style to all parts of some DocumentItem, like item.spreadStyleToParts(Style)
        //3) By adding Style-s to Formatter's StyleService, like shown below
        //   styles from StyleService will be applied to elements by the rules of styles (look Style.class)
        try {
            final var styleService = formatter.getStyleService(); //If we need styled report
            if (styleService != null) {
                styleService.addStyles(
                    formatterContext.createHeaderCellStyle(),
                    formatterContext.createRowStyleInterlinear(),
                    formatterContext.createRowStyleNormal()
                );

//                ((HtmlStyleService) styleService).setUseHtml4Tags(true);
            }

            final StreamingResponseBody responseBody =                      //Write report to StreamingResponseBody
                out -> {
                    formatter.setOutputStream(out);
                    try {
                        formatter.handle(document).close();
                    } catch (Throwable ignored) {
                    }
                };
            final var contentDisposition =
                ContentDisposition
                    .builder("attachment")
                    .filename(
                        URLEncoder.encode(
                            documentName,
                            Charset.isSupported(encoding)
                                ? Charset.forName(encoding)
                                : StandardCharsets.UTF_8
                        )
                    )
                    .build();
            final var headers = new HttpHeaders();
            headers.setContentDisposition(contentDisposition);
            var res = ResponseEntity
                .ok()
                .headers(headers)
                .contentType(formatter.getContentMediaType())
                .body(responseBody);


            final var testedBody = res.getBody();
            assert testedBody != null;
            final var os = new ByteArrayOutputStream();
            testedBody.writeTo(os);
            if (os.size() != 0) {
                switch (format) {
                    case "pdf":
                        final PdfReader pdfReader = new PdfReader(new ByteArrayInputStream(os.toByteArray()));
                        final PdfDocument doc1 = new PdfDocument(pdfReader);
                        final ITextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
                        final var currentText = PdfTextExtractor.getTextFromPage(doc1.getPage(1), strategy);
                        pdfReader.close();
                        Assertions.assertTrue(
                            currentText.contains("id comment name\n" +
                                "1 Comment1 Name1\n" +
                                "2 абракадабра с \\ЭЭ последовательностью Имя2 с !;№% номерами\n" +
                                "3 Comment3 Name3\n" +
                                "4 Comment4 Name4\n")
                        );
                        break;
                    case "xlsx":
                        final Workbook wb = WorkbookFactory.create(new ByteArrayInputStream(os.toByteArray()));
                        final Sheet sheet = wb.getSheetAt(0);
                        final String check = sheet.getRow(3).getCell(2).getStringCellValue();
                        wb.close();
                        Assertions.assertEquals("Name3", check);
                        break;
                    case "html":
                        final var text = os.toString(StandardCharsets.UTF_8);
                        Assertions.assertTrue(text.contains("Comment4"));
                        break;
                    case "docx":
                        XWPFDocument docx = new XWPFDocument(new ByteArrayInputStream(os.toByteArray()));
                        Assertions.assertEquals(
                            "Comment3",
                            docx.getTableArray(0).getRow(3).getCell(1).getText()
                        );
                }
            }
        } catch (Throwable throwable) {
            throw new IllegalStateException(throwable.getMessage(), throwable);
        }
    }

    static class Person {
        Serializable id;
        String name;
        String comment;

        public Person(Serializable id, String name, String comment) {
            this.id = id;
            this.name = name;
            this.comment = comment;
        }

        public Serializable getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getComment() {
            return comment;
        }
    }
}
