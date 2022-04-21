package com.reporter.formatter.word;

import com.documents4j.api.DocumentType;
import com.documents4j.job.ConverterAdapter;
import com.documents4j.job.LocalConverter;
import com.google.common.base.MoreObjects;
import com.reporter.domain.Document;
import com.reporter.formatter.DocumentCreator;
import com.reporter.formatter.DocumentHolder;
import com.reporter.formatter.FormatterContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * Class for writing to .doc format (MS Word 2007 or higher)
 * through converting from .docx (!)
 *
 * P.S.:
 * I can write only simple text and page-breakers using apache-poi in .doc (MS 95 or higher),
 * therefore I decide to use converter
 */
@Component
public class DocFormatter extends DocxFormatter {
    private static final String EXTENSION = "doc";
    private static final MediaType MEDIA_TYPE = MediaType.parseMediaType("application/msword");
    private static final String FILE_PREFIX = "for_convert_";

    /**
     * documents4j converter:
     * LocalConverter or RemoteConverter (see documents4j docs)
     */
    protected ConverterAdapter converterAdapter;

    public DocFormatter() {
        super();
    }

    public DocFormatter(FormatterContext context) {
        super(context);
    }

    public static DocFormatter create() {
        return new DocFormatter();
    }

    public static DocFormatter create(FormatterContext context) {
        return new DocFormatter(context);
    }

    @Override
    public String getExtension() {
        return EXTENSION;
    }

    @Override
    public MediaType getContentMediaType() {
        return MEDIA_TYPE;
    }

    /**
     * Handles document as .docx MS document and then converts it to .doc format
     * @param document meta-information about subject
     * @return DocumentHolder with resource
     * @throws Throwable mainly something wrong with converter
     */
    @Override
    public DocumentHolder handle(Document document) throws Throwable {
        final var docName = document.getLabel();
        document.setLabel(FILE_PREFIX + docName);
        final var documentHolder = super.handle(document);
        final var inputStream = documentHolder.getResourceInputStream();

        final var writableResource =
            DocumentCreator.initResource(null, docName, getExtension());

        if (converterAdapter != null) {
            converterAdapter
                .convert(inputStream).as(DocumentType.DOCX)
                .to(writableResource.getOutputStream()).as(DocumentType.DOC)
                .execute();
        } else {
            LocalConverter
                .builder()
                .baseFolder(documentHolder.getResource().getFile().getParentFile())
                .build()
                .convert(inputStream).as(DocumentType.DOCX)
                .to(writableResource.getOutputStream()).as(DocumentType.DOC)
                .execute();
        }
        documentHolder.close();
        return new DocumentHolder(writableResource);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("converterAdapter", converterAdapter)
            .toString();
    }

    public ConverterAdapter getConverterAdapter() {
        return converterAdapter;
    }

    public DocFormatter setConverterAdapter(ConverterAdapter converterAdapter) {
        this.converterAdapter = converterAdapter;
        return this;
    }
}
