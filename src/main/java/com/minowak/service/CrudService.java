package com.minowak.service;

import java.util.List;

// TODO rename
public interface CrudService<T> {
    T add(T element);
    List<T> add(List<T> elements);
    boolean delete(T element);
    boolean delete(List<T> elements);
    T update(T element);
}
