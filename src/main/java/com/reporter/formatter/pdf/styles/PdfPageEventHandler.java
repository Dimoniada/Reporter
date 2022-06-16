package com.reporter.formatter.pdf.styles;

import com.google.common.base.MoreObjects;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

// Handling header and footer additions on pages
//how-to-add-header-and-footer-to-a-pdf-with-itext-7
public class PdfPageEventHandler implements IEventHandler {
    private static final int TOP_MARGIN = 10;

    protected com.itextpdf.layout.element.Paragraph elParagraph;
    protected com.itextpdf.layout.Document document;

    /**
     * @param elParagraph - decorated paragraph
     * @param document - base pdf document
     */
    PdfPageEventHandler(
        com.itextpdf.layout.element.Paragraph elParagraph,
        com.itextpdf.layout.Document document
    ) {
        this.elParagraph = elParagraph;
        this.document = document;
    }

    public static PdfPageEventHandler create(
        com.itextpdf.layout.element.Paragraph elParagraph,
        com.itextpdf.layout.Document document
    ) {
        return new PdfPageEventHandler(elParagraph, document);
    }

    /**
     * Header and footer in itextpdf are drawn automatically when creating pages.
     * Passage events are registered for this
     * the corresponding sections of the pdf page when filling it out.
     * <p>
     * When events occur, a canvas is created that accepts elParagraph formatted text
     * and located at the desired coordinates.
     *
     * @param currentEvent - event type for rendering header or footer:
     * visiting the beginning of the page or its end
     */
    @Override
    public void handleEvent(Event currentEvent) {
        if (elParagraph == null) {
            return;
        }
        final var docEvent = (PdfDocumentEvent) currentEvent;
        final var pageSize = docEvent.getPage().getPageSize();

        final var posX = getXPositionOnPageByMiddlePoint(pageSize);
        final var posY = getYPositionOnPageByEvent(pageSize, docEvent);

        final var canvas = new Canvas(docEvent.getPage(), pageSize);
        canvas
            .showTextAligned(elParagraph, posX, posY, TextAlignment.LEFT)
            .close();
    }

    /**
     * Returns the horizontal position of the middle of the sheet,
     * counts as the width of the visible page divided by 2
     *
     * @param pageSize - pdf sheet rectangle
     * @return - x coordinate of the middle of the sheet
     */
    private float getXPositionOnPageByMiddlePoint(Rectangle pageSize) {
        return (pageSize.getLeft() + document.getLeftMargin()
            + (pageSize.getRight() - document.getRightMargin())) / 2;
    }

    /**
     * Returns the vertical position of the header or footer of the sheet
     *
     * @param pageSize - pdf sheet rectangle
     * @param docEvent - header or footer view event
     * @return - y coordinate of the header or footer of the sheet
     */
    private float getYPositionOnPageByEvent(Rectangle pageSize, PdfDocumentEvent docEvent) {
        float posY = 0;
        if (PdfDocumentEvent.START_PAGE.equals(docEvent.getType())) {
            posY = pageSize.getTop() - document.getTopMargin() + TOP_MARGIN;
        } else if (PdfDocumentEvent.END_PAGE.equals(docEvent.getType())) {
            posY = document.getBottomMargin();
        }
        return posY;
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("elParagraph", elParagraph)
                .add("document", document)
                .add("parent", super.toString())
                .toString();
    }

    public Paragraph getElParagraph() {
        return elParagraph;
    }

    public void setElParagraph(Paragraph elParagraph) {
        this.elParagraph = elParagraph;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
