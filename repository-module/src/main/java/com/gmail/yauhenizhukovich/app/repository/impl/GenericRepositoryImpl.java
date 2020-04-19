package com.gmail.yauhenizhukovich.app.repository.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.gmail.yauhenizhukovich.app.repository.GenericRepository;

public abstract class GenericRepositoryImpl<I, T> implements GenericRepository<I, T> {

    protected Class<T> entityClass;
    @PersistenceContext
    protected EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public GenericRepositoryImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[1];
    }

    @Override
    public T add(T t) {
        entityManager.persist(t);
        return t;
    }

    @Override
    public void delete(T t) {
        entityManager.remove(t);
    }

    @Override
    public T getById(I id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public Long getCountOfObjects() {
        String queryString = "SELECT COUNT(*) FROM " + entityClass.getSimpleName() + " e";
        Query query = entityManager.createQuery(queryString);
        return (Long) query.getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        String queryString = "FROM " + entityClass.getSimpleName() + " e";
        Query query = entityManager.createQuery(queryString);
        return (List<T>) query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getObjectsByStartPositionAndMaxResult(int startPosition, int maxResult) {
        String queryString = "FROM " +
                entityClass.getSimpleName() + " e";
        Query query = entityManager.createQuery(queryString);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return (List<T>) query.getResultList();
    }

}
