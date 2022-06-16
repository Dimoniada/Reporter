package com.reporter.formatter;

import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Utility class for initializing/creating filesystem documents
 */
public final class DocumentCreator {
    /**
     * Pattern for any characters in the file name, except:
     * # % & { } \ < > * ? / $ ! ' " : @ + ` | =
     * Also contains the control character \c? and \000 (for unix systems)
     */
    public static final String REGEX_PATTERN_FILENAME = "([^#%&{}\\=`+@'!$/\"*:<>?\\\\|\\c?\\000]*){1,255}";

    private DocumentCreator() {
        /**/
    }

    /**
     * Creates a WritableResource if the original resource = null.
     * WritableResource is created as a file named fileName if given.
     * If fileName is not set, then the name is generated automatically
     *
     * @param resource file system resource or null
     * @param fileName desired filename, optional
     * @param ext file extension
     * @return returns a resource ready to be written to
     * @throws IOException if file already exists
     * @throws IllegalArgumentException if fileName cannot be used for a file
     */
    public static WritableResource initResource(
        WritableResource resource,
        String fileName,
        String ext
    ) throws IOException {
        if (resource != null) {
            return resource;
        }

        final var extension = (StringUtils.hasText(ext) ? "." + ext : "");
        var localFileName = fileName + extension;

        if (!StringUtils.hasText(fileName)) {
            localFileName = UUID.randomUUID() + extension;
        }

        validateStringFilename(localFileName);

        if (new File(localFileName).exists()) {
            throw new IOException(String.format("File \"%s\" already exists", localFileName));
        }

        return new FileUrlResource(localFileName);
    }

    /**
     * Checks if a string is used as a filename
     * by pattern REGEX_PATTERN_FILENAME
     * @param filename expected filename
     * @throws IllegalArgumentException if name fails validation
     */
    public static void validateStringFilename(String filename) {
        if (!StringUtils.hasText(filename) || !filename.matches(REGEX_PATTERN_FILENAME)) {
            throw new IllegalArgumentException(String.format("Filename \"%s\" is incorrect", filename));
        }
    }
}

