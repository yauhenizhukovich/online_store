package com.gmail.yauhenizhukovich.app.repository;

import java.util.List;

public interface GenericRepository<I, T> {

    T add(T t);

    boolean delete(T t);

    T getById(I id);

    Long getCountOfObjects();

    List<T> getAll();

    List<T> getPaginatedObjects(int startPosition, int maxResult);

}
