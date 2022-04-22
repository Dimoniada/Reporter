package com.reporter.db;

import com.reporter.domain.*;
import com.reporter.domain.styles.*;
import com.reporter.domain.styles.constants.*;
import com.reporter.utils.LocalizedNumberUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

@SpringBootTest
public class BaseQueryDocument {
    public Document queryDoc;
    public FontService fontService;
    public TextStyle textTitleStyle;
    public LayoutTextStyle headerCellStyle;
    public LayoutTextStyle cellStyle;
    public LayoutTextStyle rowStyleNormal;
    public LayoutTextStyle rowStyleAlert;
    public LayoutTextStyle rowStyleInterlinear;

    public void initDoc() throws Exception {
        fontService = FontService
            .create()
            .initializeFonts();

        final var textStyle = (TextStyle) TextStyle
            .create()
            .setFontFamilyStyle(FontFamilyStyle.SERIF)
            .setFontSize((short) 10);

        textTitleStyle = textStyle
            .clone()
            .setFontFamilyStyle(FontFamilyStyle.SANS_SERIF)
            .setFontSize((short) 16)
            .setBold(true)
            .setCondition(StyleCondition.create(Title.class, o -> o instanceof Title));

        final var border = BorderStyle.create(Color.GREY_50_PERCENT, BorderWeight.DOUBLE);

        final var layoutStyleNormal = LayoutStyle
            .create()
            .setBorderTop(border)
            .setBorderLeft(border)
            .setBorderRight(border)
            .setBorderBottom(border)
            .setAutoWidth(true)
            .setFillPattern(FillPattern.SOLID_FOREGROUND)
            .setHorAlignment(HorAlignment.LEFT);

        final var layoutStyleInterlinear = layoutStyleNormal
            .clone()
            .setFillBackgroundColor(Color.GREEN_LIGHT)
            .setFillForegroundColor(Color.GREEN_LIGHT);

        final var layoutStyleAlert = layoutStyleNormal
            .clone()
            .setFillBackgroundColor(Color.RED_LIGHT)
            .setFillForegroundColor(Color.RED_LIGHT);

        headerCellStyle = LayoutTextStyle
            .create(textStyle
                    .clone()
                    .setFontSize((short) 11).setBold(true),
                layoutStyleNormal
                    .clone()
                    .setHorAlignment(HorAlignment.CENTER)
                    .setVertAlignment(VertAlignment.CENTER)
            )
            .setCondition(StyleCondition.create(TableHeaderCell.class, o -> o instanceof TableHeaderCell));


        rowStyleNormal = LayoutTextStyle
            .create(textStyle, layoutStyleNormal)
            .setCondition(
                StyleCondition.create(
                    TableRow.class, o -> o instanceof TableRow
                        && ((TableRow) o).getRowIndex() % 2 == 0
                )
            );

        rowStyleInterlinear = LayoutTextStyle
            .create(textStyle, layoutStyleInterlinear)
            .setCondition(
                StyleCondition.create(
                    TableRow.class, o -> o instanceof TableRow
                        && ((TableRow) o).getRowIndex() % 2 != 0
                )
            );

        rowStyleAlert = LayoutTextStyle
            .create(textStyle, layoutStyleAlert)
            .setCondition(StyleCondition.create(TableRow.class, o -> {
                if (o instanceof TableRow) {
                    final var cells = (ArrayList<TableCell>) ((TableRow) o).getParts();
                    if (cells.size() > 4) {
                        final var cell1 = cells.get(2);
                        final var cell2 = cells.get(3);
                        return StringUtils.hasText(cell1.getText())
                            && StringUtils.hasText(cell2.getText())
                            && LocalizedNumberUtils.isNumber(cell1.getText())
                            && LocalizedNumberUtils.isNumber(cell2.getText())
                            && Integer.parseInt(cell1.getText()) > Integer.parseInt(cell2.getText());
                    }
                }
                return false;
            }));

        cellStyle = LayoutTextStyle
            .create(textStyle, layoutStyleNormal.clone().setHorAlignment(HorAlignment.RIGHT))
            .setCondition(StyleCondition.create(TableCell.class, o -> {
                if (o instanceof TableCell) {
                    final var cell = (TableCell) o;
                    if (StringUtils.hasText(cell.getText())) {
                        final var text = cell.getText();
                        return LocalizedNumberUtils.isNumber(text) && text.length() != 11 || text.endsWith("%");
                    }
                }
                return false;
            }));

        queryDoc = Document
            .create()
            .setLabel("doc2")
            .addPart(Title
                .create("Мониторинг трафика 2021-05-26")
                .setStyle(textTitleStyle)
            );
    }

}
