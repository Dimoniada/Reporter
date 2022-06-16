package com.reporter.db.repositories.h2;

import org.springframework.util.StringUtils;

/**
 * Labeling interface to support canonical name
 * @param <PK>
 */
public interface CanonicalNameAware<PK> extends Identifiable<PK>, NameAware {
    default String getCanonicalName() {
        if (!StringUtils.hasText(getName())) {
            return String.format("#%s", getId());
        }

        return String.format("#%s %s", getId(), getName());
    }
}
