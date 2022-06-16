package com.reporter.formatter;

import com.reporter.domain.Document;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Class contains the document resource, the resource stream, and the resource's file name if it is a file.
 */
public abstract class Formatter extends FormatterVisitor implements BaseFormatter {
    /**
     * Document file resource
     */
    protected WritableResource resource;
    /**
     * Stream to record document
     */
    protected OutputStream outputStream;
    /**
     * Document file name if it is an OS file
     */
    protected String fileName;

    /**
     * Writes meta-informational document as selected type document to {@link Formatter#resource}
     * @param document input meta-document
     * @return DocumentHolder
     * @throws Throwable can occur while creating/initializing/cleaning up {@link Formatter#resource}
     */
    public DocumentHolder handle(Document document) throws Throwable {
        if (document == null) {
            throw new IllegalArgumentException("Document not set");
        }
        if (!StringUtils.hasText(fileName)) {
            fileName = document.getLabel();
        }
        resource = DocumentCreator.initResource(resource, fileName, getExtension());
        fileName = resource.getFilename();
        initializeResource();
        document.accept(this);
        cleanupResource();
        return new DocumentHolder(resource);
    }

    /**
     * Initialization of resources required by the formatter for writing
     *
     * @throws IOException if the resource cannot be opened
     */
    public abstract void initializeResource() throws IOException;

    /**
     * Finalization of resources required by the formatter for recording
     *
     * @throws IOException if an error occurred while saving the resource
     */
    public abstract void cleanupResource() throws IOException;

    public WritableResource getResource() {
        return resource;
    }

    public OutputStream getOutputStream() throws IOException {
        return
            outputStream != null
                ? outputStream
                : resource != null
                    ? resource.getOutputStream()
                    : null;
    }

    public String getFileName() {
        return fileName;
    }

    @SuppressWarnings("unchecked")
    public <T extends Formatter> T setResource(WritableResource resource) throws IOException {
        this.resource = resource;
        this.outputStream = resource.getOutputStream();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Formatter> T setFileName(String fileName) {
        this.fileName = fileName;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Formatter> T setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        return (T) this;
    }
}
