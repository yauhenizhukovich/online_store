package com.gmail.yauhenizhukovich.app.repository;

import java.util.List;

public interface GenericRepository<I, T> {

    T add(T t);

    void delete(T t);

    T getById(I id);

    Long getCountOfObjects();

    List<T> getAll();

    List<T> getObjectsByStartPositionAndMaxResult(int startPosition, int maxResult);

}
