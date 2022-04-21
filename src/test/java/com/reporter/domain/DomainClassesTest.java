package com.reporter.domain;

import com.reporter.formatter.DocumentHolder;
import com.reporter.formatter.excel.XlsFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.FileUrlResource;

import java.nio.file.Files;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class DomainClassesTest {
    /**
     * Custom forward-iterable sequence of TableCell-s
     */
    static class CustomIterableTableCell implements Iterable<TableCell> {
        @Override
        public Iterator<TableCell> iterator() {
            return new Iterator<>() {
                private int count = 10;

                @Override
                public boolean hasNext() {
                    return count-- > 0;
                }

                @Override
                public TableCell next() {
                    return TableCell.create().setText("textCell");
                }
            };
        }

        @Override
        public void forEach(Consumer<? super TableCell> action) {
        }

        @Override
        public Spliterator<TableCell> spliterator() {
            return null;
        }
    }

    /**
     * Checks failure call addPart() after adding
     * not a Collection {@link CustomIterableTableCell}
     * as a part of {@link CompositionPart}
     */
    @Test
    public void testCustomCompositionPartsFailure() {
        final var item2 = TableCell.create().setText("cell2");
        final var cells = new CustomIterableTableCell();
        final Exception e = Assertions.assertThrows(IllegalStateException.class, () ->
                TableRow.create().addPart(item2).setParts(cells).addPart(item2)
        );
        Assertions.assertEquals("Parts is not a collection", e.getMessage());
    }

    /**
     * Example how to encapsulate and avoid failure call addPart() after adding
     * a non-Collection {@link CustomIterableTableCell} via setParts()
     */
    @Test
    public void testCustomCompositionPartsOk() throws Throwable {
        final var cells = new CustomIterableTableCell();

        final var table = Table
                .create()
                .setTableHeaderRow(
                        TableHeaderRow
                                .create()
                                .addParts(TableHeaderCell.create().setText("Column1"),
                                        TableHeaderCell.create().setText("Column2")
                                ))
                .addPart(TableRow.create().addPart(TableCell.create()).addPart(TableCell.create()))
                .addPart(TableRow.create().setParts(cells))
                .addPart(TableRow.create().addPart(TableCell.create()).addPart(TableCell.create()));

        final var doc = Document
                .create()
                .setLabel("doc1")
                .addPart(table);

        final var xlsFormatter = spy(XlsFormatter.class);
        xlsFormatter.handle(doc).close();
        Mockito.verify(xlsFormatter, times(2 + 10 + 2)).visitTableCell(any());
    }

    /**
     * Test on {@link DocumentHolder file-holder} creation,
     * closing and removing file.
     *
     * @throws Throwable MalformedURLException/Exception/IOException
     */
    @Test
    public void testDocumentHolder() throws Throwable {
        final var file = new FileUrlResource("testFile");
        final var documentHolder = new DocumentHolder(file);
        Assertions.assertTrue(AutoCloseable.class.isAssignableFrom(DocumentHolder.class));
        documentHolder.close();
        Assertions.assertTrue(Files.notExists(documentHolder.getResource().getFile().toPath()));
    }
}
