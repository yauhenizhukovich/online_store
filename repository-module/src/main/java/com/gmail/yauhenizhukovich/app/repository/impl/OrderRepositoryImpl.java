package com.gmail.yauhenizhukovich.app.repository.impl;

import java.util.List;
import javax.persistence.Query;

import com.gmail.yauhenizhukovich.app.repository.OrderRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Order;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl extends GenericRepositoryImpl<Long, Order> implements OrderRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> getPaginatedObjects(int startPosition, int maxResult) {
        String queryString = "FROM " +
                entityClass.getSimpleName() + " e ORDER BY e.date DESC";
        Query query = entityManager.createQuery(queryString);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return (List<Order>) query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> getPaginatedOrdersByCustomer(int startPosition, int maxResult, User user) {
        String queryString = "FROM " +
                entityClass.getSimpleName() + " e WHERE e.customer=:user ORDER BY e.date DESC";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("user", user);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return (List<Order>) query.getResultList();
    }

    @Override
    public Long getCountOfOrdersByUser(User user) {
        String queryString = "SELECT COUNT(*) FROM " + entityClass.getSimpleName() + " e WHERE e.customer=:user";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("user", user);
        return (Long) query.getSingleResult();
    }

}
