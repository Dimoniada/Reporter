package com.reporter.domain;

import com.reporter.formatter.FormatterVisitor;
import com.google.common.base.MoreObjects;
import org.springframework.util.StringUtils;

/**
 * This class is used to structure components within a document.
 * Can be used to separate independent document blocks: pages in multipart documents,
 * sheets in spreadsheet books, etc.
 */
public class DocumentCase extends CompositionPart<DocumentCase, DocumentItem> {

    protected String name = "Sheet";

    public static DocumentCase create(String name) {
        final var sheet = new DocumentCase();
        sheet.setName(name);
        return sheet;
    }

    public static DocumentCase create() {
        return DocumentCase.create("");
    }

    @Override
    public DocumentCase accept(FormatterVisitor visitor) throws Throwable {
        visitor.visitDocumentCase(this);
        return this;
    }

    public String getName() {
        return name;
    }

    public DocumentCase setName(String name) {
        if (StringUtils.hasText(name)) {
            this.name = name;
        }
        return this;
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("parent", super.toString())
                .toString();
    }
}
