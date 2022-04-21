package com.reporter.db.repositories.h2;

/**
 * Interface of an identified object = an object that can be addressed by the key Id
 *
 * @param <ID> the primary key class for the entity
 */
public interface Identifiable<ID> {
    ID getId();
}
