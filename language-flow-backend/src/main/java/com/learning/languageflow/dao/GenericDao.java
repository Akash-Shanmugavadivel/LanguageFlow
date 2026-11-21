package com.learning.languageflow.dao;

import java.util.List;
import java.util.Optional;

/**
 * Generic Data Access Object contract providing fundamental CRUD operations.
 *
 * @param <T>  The domain entity type
 * @param <ID> The entity primary key type
 */
public interface GenericDao<T, ID> {

    Optional<T> findById(ID id);

    List<T> findAll();

    T save(T entity);

    boolean deleteById(ID id);
}
