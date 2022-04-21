package com.reporter.domain.styles;

import java.io.OutputStreamWriter;

/**
 * Basic StyleApplier interface
 * Object o - native object for which style(s) are applied
 */

public interface StyleApplier {
    /**
     * Writes styles to native object
     *
     * @param o native object to write styles
     * @throws Exception when cloning styles or writing via {@link OutputStreamWriter#write}
     */
    void writeStyles(Object o) throws Exception;
}
