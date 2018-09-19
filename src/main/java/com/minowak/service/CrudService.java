package com.minowak.service;

import java.util.List;

// TODO rename
public interface CrudService<T> {
    T get(Long id);
    List<T> get();
    boolean add(T element);
    boolean add(List<T> elements);
    void delete(Long id);
    void delete();
    void update(Long id, T elementAfter);
}
