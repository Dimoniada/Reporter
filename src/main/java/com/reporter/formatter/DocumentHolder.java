package com.reporter.formatter;

import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.WritableResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class-holder of the result,
 * successor to AutoCloseable:
 * after closing {@link DocumentHolder#close()}
 * deletes document {@link DocumentHolder#resource}
 * and its attachments {@link DocumentHolder#attachments}.
 */
public class DocumentHolder implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(DocumentHolder.class);

    protected List<WritableResource> attachments;
    protected WritableResource resource;
    protected InputStream resourceInputStream;

    public DocumentHolder(WritableResource resource) {
        this.resource = resource;
    }

    @Override
    public void close() throws Exception {
        resource.getOutputStream().close();
        getResourceInputStream().close();
        deleteResource(resource);
        for (final var attachment : getAttachments()) {
            if (attachment.isFile()) {
                deleteResource(attachment);
            }
        }
    }

    public void deleteResource(WritableResource resource) {
        final String filename = resource.getFilename();

        log.debug("Try to remove file:'{}'", filename);
        try {
            Files.delete(resource.getFile().toPath());
            log.debug("File:'{}' successfully removed", filename);
        } catch (Exception e) {
            final String errorMsg = String.format("Error on remove file:'%s'", filename);

            log.error(errorMsg, e);
            throw new IllegalStateException(errorMsg, e);
        }
    }

    @Override
    public String toString() {
        return
            MoreObjects.toStringHelper(this)
                .add("attachments", attachments)
                .add("resource", resource)
                .add("resourceInputStream", resourceInputStream)
                .toString();
    }

    public WritableResource getResource() {
        return resource;
    }

    public List<WritableResource> getAttachments() {
        return Optional.ofNullable(attachments).orElse(new ArrayList<>());
    }

    public InputStream getResourceInputStream() throws IOException {
        if (resourceInputStream == null && resource != null) {
            resourceInputStream = resource.getInputStream();
        }
        return resourceInputStream;
    }

    public DocumentHolder setResource(WritableResource resource) {
        this.resource = resource;
        return this;
    }

    public DocumentHolder setAttachments(List<WritableResource> attachments) {
        this.attachments = attachments;
        return this;
    }
}
