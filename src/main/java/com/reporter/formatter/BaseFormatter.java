package com.reporter.formatter;

import com.reporter.domain.Document;
import com.reporter.domain.styles.StyleService;
import org.springframework.http.MediaType;

/**
 * Contains common formatter methods
 */
public interface BaseFormatter {
    /**
     * Returns the file extension for the entry
     *
     * @return extension
     */
    String getExtension();

    /**
     * Returns the mimetype of the received file/resource
     *
     * @return MediaType of file/resource
     */
    MediaType getContentMediaType();

    /**
     * Starts generating a file/resource from a Document
     *
     * @param document input document
     * @return received file/resource holder
     * @throws Throwable if file/resource generation failed
     */
    DocumentHolder handle(Document document) throws Throwable;

    /**
     * Returns the style service for the document
     *
     * @return style service
     */
    StyleService getStyleService() throws Exception;
}
