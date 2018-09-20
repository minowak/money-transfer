package com.minowak.service;

import java.util.Collection;

public interface PersistenceService<K, T> {
    T get(K id);
    Collection<T> get();
    boolean add(T element);
    boolean delete(K id);
    boolean delete();
    boolean update(K id, T elementAfter);
}
