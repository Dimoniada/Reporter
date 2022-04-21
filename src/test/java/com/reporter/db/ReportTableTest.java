package com.reporter.db;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;
import com.reporter.db.repositories.h2.TestCDRPeriodStatisticByPartner;
import com.reporter.db.repositories.h2.TestChannelDailyRollupRepository;
import com.reporter.db.repositories.h2.TestPartner;
import com.reporter.db.repositories.h2.TestPartnerRepository;
import com.reporter.domain.*;
import com.reporter.domain.db.QueryTable;
import com.reporter.domain.styles.*;
import com.reporter.domain.styles.constants.*;
import com.reporter.domain.styles.constants.Color;
import com.reporter.formatter.FormatterContext;
import com.reporter.formatter.FormatterFactory;
import com.reporter.formatter.csv.CsvFormatter;
import com.reporter.formatter.excel.XlsFormatter;
import com.reporter.formatter.html.HtmlFormatter;
import com.reporter.formatter.html.styles.HtmlStyleService;
import com.reporter.formatter.pdf.PdfFormatter;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.supercsv.prefs.CsvPreference;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.sql.Types;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Sql("classpath:db/h2/schema_report_table_test.sql")
public class ReportTableTest extends BaseQueryDocument {

    @Autowired
    MessageSource messageSource;

    @Autowired
    public NamedParameterJdbcTemplate jdbcTemplateH2;

    @Autowired
    protected TestChannelDailyRollupRepository testCDRRepository;

    @Autowired
    protected TestPartnerRepository testPartnerRepository;

    @Autowired
    protected FormatterFactory formatterFactory;

    static Stream<Arguments> testArguments() {
        return Stream.of(
            Arguments.of("pdf", "pl", List.of("id", "name", "legalPersonName"), "UTF-8", ','),
            Arguments.of("html", "zh", Arrays.asList("id", "name", "legalPersonName"), "UTF-8", ','),
            Arguments.of("csv", "zh", Arrays.asList("id", "name", "legalPersonName"), "UTF-8", ';'),
            Arguments.of(
                    "docx",
                    "zh",
                    Arrays.asList("id", "comment", "filialId", "ownerPartnerId", "name", "legalPersonName"),
                    "UTF-8",
                    ';'
            )
        );
    }

    @BeforeEach
    public void initDoc() throws Exception {
        super.initDoc();

        final var queryTable = QueryTable.create()
            .setNamedParameterJdbcTemplate(jdbcTemplateH2)
            .setTableHeaderRow(
                TableHeaderRow.create().addParts(
                    TableHeaderCell.create().setText("Login")
                        .setAliasName("login"),
                    TableHeaderCell.create().setText("Name of client")
                        .setAliasName("legal_person_name"),
                    TableHeaderCell.create().setText("2021-05-25")
                        .setAliasName("date1_amount"),
                    TableHeaderCell.create().setText("2021-05-26")
                        .setAliasName("date2_amount"),
                    TableHeaderCell.create().setText("Absolute change")
                        .setAliasName("abs_diff"),
                    TableHeaderCell.create().setText("Relative change")
                        .setAliasName("rel_diff_percent")
                )
            )
            .setMapSqlParameterSource(new MapSqlParameterSource()
                .addValue("date_from", "2021-05-25", Types.DATE)
                .addValue("date_to", "2021-05-26", Types.DATE)
                .addValue("date_now", LocalDate.now(), Types.DATE)
            )
            .setQuery("select \"rep_1\".\"login\"                 \"login\",\n" +
                "       \"rep_1\".\"legal_person_name\"     \"legal_person_name\",\n" +
                "       \"rep_1\".\"val1\"                  \"date1_amount\",\n" +
                "       \"rep_2\".\"val2\"                  \"date2_amount\",\n" +
                "       \"rep_2\".\"val2\" - \"rep_1\".\"val1\" \"abs_diff\",\n" +
                "       case\n" +
                "           when \"rep_1\".\"val1\" = 0 then '100%'\n" +
                "           else\n" +
                "               case\n" +
                "                   when \"rep_2\".\"val2\" = 0 then '-100%'\n" +
                "                   else\n" +
                "                       case\n" +
                "                           when \"rep_2\".\"val2\" % \"rep_1\".\"val1\" = 0 then\n" +
                "                               concat(round(((\"rep_2\".\"val2\" - \"rep_1\".\"val1\") / \"rep_1\".\"val1\" * 100), 0), '%')\n" +
                "                           else\n" +
                "                               concat(round(((\"rep_2\".\"val2\" - \"rep_1\".\"val1\") / \"rep_1\".\"val1\" * 100), 3), '%')\n" +
                "                           end\n" +
                "                   end\n" +
                "           end                         \"rel_diff_percent\"\n" +
                "from (select sum(\"value1\") \"val1\",\n" +
                "             \"rep1\".\"id\",\n" +
                "             \"rep1\".\"login\",\n" +
                "             \"rep1\".\"legal_person_name\"\n" +
                "      from (select \"p\".\"login\"                    \"login\",\n" +
                "                   \"p\".\"legal_person_name\",\n" +
                "                   \"p\".\"id\",\n" +
                "                   sum(\"cdr\".\"sms_send_count\") as \"value1\"\n" +
                "            from \"channel_daily_rollup\" as \"cdr\"\n" +
                "                     inner join \"partners\" as \"p\" on (\"cdr\".\"partner_id\" = \"p\".\"id\")\n" +
                "            where \"cdr\".\"date_cdr\" >= :date_from\n" +
                "              AND \"cdr\".\"date_cdr\" < :date_to\n" +
                "            group by \"p\".\"id\", \"cdr\".\"channel_id\", \"cdr\".\"alias_cdr\", coalesce(\"p\".\"owner_partner_id\", \"p\".\"id\")) \"rep1\"\n" +
                "      group by \"rep1\".\"id\") \"rep_1\"\n" +
                "         join\n" +
                "     (select sum(\"value2\") \"val2\", \"rep2\".\"id\"\n" +
                "      from (select \"p\".\"id\", sum(\"cdr\".\"sms_send_count\") as \"value2\"\n" +
                "            from \"channel_daily_rollup\" as \"cdr\"\n" +
                "                     inner join \"partners\" as \"p\" on (\"cdr\".\"partner_id\" = \"p\".\"id\")\n" +
                "            where \"cdr\".\"date_cdr\" >= :date_to\n" +
                "              AND \"cdr\".\"date_cdr\" < :date_now\n" +
                "            group by \"p\".\"id\", \"cdr\".\"channel_id\", \"cdr\".\"alias_cdr\", coalesce(\"p\".\"owner_partner_id\", \"p\".\"id\")) \"rep2\"\n" +
                "      group by \"rep2\".\"id\") \"rep_2\"\n" +
                "     on\n" +
                "         \"rep_1\".\"id\" = \"rep_2\".\"id\";");

        queryDoc.addPart(queryTable);
    }

    @ParameterizedTest(name = "{index}: {0} test")
    @MethodSource("testArguments")
    public void testReportTableProviders(
        String format,
        String locale,
        List<String> columns,
        String encoding,
        Character csvDelimiter
    ) throws Throwable {
        final var formatterContext =
            FormatterContext.create(
                encoding,
                Locale.forLanguageTag(locale),
                null,
                null,
                csvDelimiter
            );

        final List<TestPartner> list = testPartnerRepository.findAllPartners();

        final var fieldsNames = Arrays
            .stream(TestPartner.class.getDeclaredFields())
            .map(Field::getName)
            .collect(Collectors.toList());
        final var wrongColumnName = columns
            .stream()
            .filter(c -> !fieldsNames.contains(c))
            .findFirst();
        if (wrongColumnName.isPresent()) {
            throw new IllegalArgumentException(String.format("Wrong column name \"%s\"", wrongColumnName.get()));
        }

        final var table = ReportTable.create();
        final var tableHeaderRow = TableHeaderRow.create();
        columns.forEach(c ->
            tableHeaderRow
                .addPart(TableHeaderCell
                    .create()
                    .setText(
                        messageSource.getMessage("entity.partner." + c, null, Locale.forLanguageTag(locale))
                    )
                    .setAliasName(c)
                )
        );
        table.setTableHeaderRow(tableHeaderRow);
        table.addDataList(list);
        final var document =
            Document
                .create()
                .setLabel(
                    messageSource.getMessage("entity.partner", null, Locale.forLanguageTag(locale))
                )
                .addPart(table);

        if (format.equals("pdf") && locale.equals("zh")) {
            redefineStylesChinese();
        } else if (format.equals("pdf") && locale.equals("pl")) {
            redefineStylesPolish();
        } else {
            redefineStyles();
        }

        final var formatter = formatterFactory
            .createFormatter(
                format,
                formatterContext
            );

        if (formatter instanceof CsvFormatter) {
            ((CsvFormatter) formatter)
                .setCsvPreference(
                    new CsvPreference
                        .Builder('"',
                        csvDelimiter,
                        "\n")
                        .build()
                );
        } else if (formatter instanceof HtmlFormatter) {
            ((HtmlStyleService) formatter.getStyleService())
                .setUseHtml4Tags(false); //or try true here
        }

        final StyleService styleService = formatter.getStyleService();
        if (styleService != null) {
            styleService.addStyles(
                headerCellStyle,
                rowStyleInterlinear,
                rowStyleNormal);
            styleService.setFontService(fontService);
        }
        final var documentHolder = formatter.handle(document);
        documentHolder.close();
    }

    private void redefineStylesPolish() {
        final var locale = Locale.forLanguageTag("pl");
        List.of(headerCellStyle, rowStyleInterlinear, rowStyleNormal)
            .forEach(s -> s
                .getTextStyle()
                .setFontNameResource("tahoma_SansSerif_(en-pl Aller).ttf")
                .setFontFamilyStyle(FontFamilyStyle.SANS_SERIF)
                .setFontLocale(locale)
            );
    }

    private void redefineStylesChinese() {
        final var locale = Locale.forLanguageTag("zh");
        List.of(headerCellStyle, rowStyleInterlinear, rowStyleNormal)
            .forEach(s -> s
                .getTextStyle()
                .setFontNameResource("xiaolai_Monospaced_(en-zh).ttf")
                .setFontFamilyStyle(FontFamilyStyle.MONOSPACED)
                .setFontLocale(locale)
            );
    }

    private void redefineStyles() throws CloneNotSupportedException {
        final var textStyle = (TextStyle) TextStyle
            .create()
            .setFontFamilyStyle(FontFamilyStyle.SERIF)
            .setFontSize((short) 12);

        textTitleStyle = textStyle
            .clone()
            .setFontFamilyStyle(FontFamilyStyle.SANS_SERIF)
            .setFontSize((short) 14)
            .setBold(true)
            .setCondition(StyleCondition.create(o -> o instanceof Title, Title.class));

        final var borderHeaderCell = BorderStyle.create(Color.GREY_50_PERCENT, BorderWeight.DOUBLE);
        final var borderCell = BorderStyle.create(Color.GREY_50_PERCENT, BorderWeight.THIN);

        final var layoutStyleHeader = LayoutTextStyle
            .create(
                textTitleStyle,
                LayoutStyle
                    .create()
                    .setBorderTop(borderHeaderCell)
                    .setBorderLeft(borderHeaderCell)
                    .setBorderRight(borderHeaderCell)
                    .setBorderBottom(borderHeaderCell)
                    .setAutoWidth(true)
                    .setFillPattern(FillPattern.SOLID_FOREGROUND)
                    .setHorAlignment(HorAlignment.CENTER)
                    .setVertAlignment(VertAlignment.CENTER)
            );

        final var layoutStyleNormal = LayoutStyle
            .create()
            .setBorderTop(borderCell)
            .setBorderLeft(borderCell)
            .setBorderRight(borderCell)
            .setBorderBottom(borderCell)
            .setAutoWidth(true)
            .setFillPattern(FillPattern.SOLID_FOREGROUND)
            .setHorAlignment(HorAlignment.LEFT);

        headerCellStyle = layoutStyleHeader
            .setCondition(StyleCondition.create(o -> o instanceof TableHeaderCell, TableHeaderCell.class));

        final Predicate<Object> isTableRow = o -> o instanceof TableRow;
        final Predicate<Object> isInterlinear = o -> ((TableRow) o).getRowIndex() % 2 == 0;
        final var styleCondition = StyleCondition
            .create(
                isTableRow.and(isInterlinear),
                TableRow.class
            );

        rowStyleNormal = LayoutTextStyle
            .create(textStyle, layoutStyleNormal)
            .setCondition(styleCondition);

        final var layoutStyleInterlinear = layoutStyleNormal
            .clone()
            .setFillBackgroundColor(Color.GREEN_LIGHT)
            .setFillForegroundColor(Color.GREEN_LIGHT);

        rowStyleInterlinear = LayoutTextStyle
            .create(textStyle, layoutStyleInterlinear)
            .setCondition(
                StyleCondition
                    .create(
                        isTableRow.and(isInterlinear.negate()),
                        TableRow.class
                    )
            );
    }

    @Test
    public void testReportTableChannelDailyRollupToPdf() throws Throwable {

        final List<TestCDRPeriodStatisticByPartner> list = testCDRRepository.getTrafficStatistics2ForPeriod(
            LocalDate.parse("2021-05-25"),
            LocalDate.parse("2021-05-26"));

        final var mapNameAlias = List.of(
            new AbstractMap.SimpleEntry<>("ИД канала", "channelId"),
            new AbstractMap.SimpleEntry<>("Отправитель", "sender"),
            new AbstractMap.SimpleEntry<>("Псевдоним", "alias_cdr"),
            new AbstractMap.SimpleEntry<>("ИД партнёра", "partnerId"),
            new AbstractMap.SimpleEntry<>("Отправлено смс", "smsSendCount"),
            new AbstractMap.SimpleEntry<>("Доставлено смс", "smsDeliveredCount"),
            new AbstractMap.SimpleEntry<>("Отправлено сообщений", "msgSendCount"),
            new AbstractMap.SimpleEntry<>("Доставлено сообщений", "msgDeliveredCount")
        );

        final var table = ReportTable
            .create(
                TableHeaderRow
                    .create()
                    .addParts(
                        mapNameAlias
                            .stream()
                            .map
                                (e ->
                                    TableHeaderCell
                                        .create()
                                        .setText(e.getKey())
                                        .setAliasName(e.getValue())
                                )
                            .toArray(TableHeaderCell[]::new)
                    )
            )
            .addDataList(list);
        final var document = Document.create().setLabel("Мониторинг трафика 2021-05-26").addPart(table);

        final PdfFormatter formatter = PdfFormatter.create("Cp1251");
        formatter.getStyleService().addStyles(
            textTitleStyle,
            headerCellStyle,
            rowStyleAlert,
            rowStyleInterlinear,
            rowStyleNormal,
            cellStyle);

        final var documentHolder = formatter.handle(document);

        if (Files.exists(documentHolder.getResource().getFile().toPath())) {
            final PdfReader pdfReader = new PdfReader(documentHolder.getResource().getFile());
            final PdfDocument doc1 = new PdfDocument(pdfReader);
            final ITextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
            final var currentText = PdfTextExtractor
                .getTextFromPage(doc1.getPage(1), strategy)
                .replaceAll("\\n", "");
            pdfReader.close();
            documentHolder.close();
            mapNameAlias.forEach(e -> Assertions.assertTrue(currentText.contains(e.getKey())));
        }
    }

    @Test
    public void testChannelDailyRollupToPdfAutoColumnsGenerated() throws Throwable {
        final var list =
            testCDRRepository
                .getTrafficStatistics2ForPeriod(
                    LocalDate.parse("2021-05-25"),
                    LocalDate.parse("2021-05-26")
                );

        final var mapNameAlias = List.of(
            new AbstractMap.SimpleEntry<>("channelId", "channelId"),
            new AbstractMap.SimpleEntry<>("sender", "sender"),
            new AbstractMap.SimpleEntry<>("aliasCdr", "aliasCdr"),
            new AbstractMap.SimpleEntry<>("partnerId", "partnerId"),
            new AbstractMap.SimpleEntry<>("smsSendCount", "smsSendCount"),
            new AbstractMap.SimpleEntry<>("smsDeliveredCount", "smsDeliveredCount"),
            new AbstractMap.SimpleEntry<>("msgSendCount", "msgSendCount"),
            new AbstractMap.SimpleEntry<>("msgDeliveredCount", "msgDeliveredCount")
        );

        final var table = ReportTable
            .create()
            .setTableHeaderRowFromData(true)
            .addDataList(list);
        final var document = Document.create().setLabel("Мониторинг трафика 2021-05-26").addPart(table);

        final PdfFormatter formatter = PdfFormatter.create("Cp1251");
        formatter.getStyleService().addStyles(
            TextStyle
                .create()
                .setFontSize((short) 9)
                .setCondition(
                    StyleCondition.create(null, TableHeaderCell.class)
                )
        );

        final var documentHolder = formatter.handle(document);

        if (Files.exists(documentHolder.getResource().getFile().toPath())) {
            final PdfReader pdfReader = new PdfReader(documentHolder.getResource().getFile());
            final PdfDocument doc1 = new PdfDocument(pdfReader);
            final ITextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
            final var currentText = PdfTextExtractor
                .getTextFromPage(doc1.getPage(1), strategy)
                .replaceAll("\\n", "");
            pdfReader.close();
            documentHolder.close();
            mapNameAlias.forEach(e -> Assertions.assertTrue(currentText.contains(e.getKey())));
        }
    }

    @Test
    public void testChannelDailyRollupHtml() throws Throwable {
        textTitleStyle.setFontNameResource("Arial");
        headerCellStyle.getTextStyle().setFontNameResource("Arial");
        cellStyle.getTextStyle().setFontNameResource("Arial");

        final HtmlFormatter formatter = HtmlFormatter.create();
        formatter.getStyleService().addStyles(
            textTitleStyle,
            headerCellStyle,
            rowStyleAlert,
            rowStyleInterlinear,
            rowStyleNormal,
            cellStyle);

        final var documentHolder = formatter.handle(queryDoc);

        final var check = Files.readString(documentHolder.getResource().getFile().toPath());

        documentHolder.close();

        Assertions.assertTrue(check.contains("79777797620"));
    }

    @Test
    public void testChannelDailyRollupPdf() throws Throwable {

        final PdfFormatter formatter = PdfFormatter.create("Cp1251");
        formatter
            .getStyleService()
            .setFontService(fontService)
            .addStyles(
                textTitleStyle,
                headerCellStyle,
                rowStyleAlert,
                rowStyleInterlinear,
                rowStyleNormal,
                cellStyle
            );

        final var documentHolder = formatter.handle(queryDoc);

        if (Files.exists(documentHolder.getResource().getFile().toPath())) {
            final var pdfReader = new PdfReader(documentHolder.getResource().getFile());
            final var doc1 = new PdfDocument(pdfReader);
            final var strategy = new SimpleTextExtractionStrategy();
            final var currentText = PdfTextExtractor.getTextFromPage(doc1.getPage(1), strategy);
            pdfReader.close();
            documentHolder.close();
            Assertions.assertTrue(currentText.contains("Мониторинг"));
            Assertions.assertTrue(currentText.contains("трафика"));
        }
    }

    @Test
    public void testChannelDailyRollupExcel() throws Throwable {

        final var formatter = XlsFormatter.create();
        formatter.getStyleService().addStyles(
            textTitleStyle,
            headerCellStyle,
            rowStyleAlert,
            rowStyleInterlinear,
            rowStyleNormal,
            cellStyle);

        final var documentHolder = formatter.handle(queryDoc);

        final var wb = WorkbookFactory.create(documentHolder.getResource().getFile());
        final var sheet = wb.getSheetAt(0);
        final var check = sheet.getRow(3).getCell(1).getStringCellValue();
        wb.close();

        documentHolder.close();

        Assertions.assertEquals("legal_person_name10", check);
    }

}


