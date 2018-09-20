package com.minowak.service;

import java.util.Collection;

// TODO rename
public interface CrudService<K, T> {
    T get(K id);
    Collection<T> get();
    boolean add(T element);
    boolean add(Collection<T> elements);
    boolean delete(K id);
    boolean delete();
    boolean update(K id, T elementAfter);
}
