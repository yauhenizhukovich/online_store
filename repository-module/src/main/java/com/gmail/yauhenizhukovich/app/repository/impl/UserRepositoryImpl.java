package com.gmail.yauhenizhukovich.app.repository.impl;

import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.RoleEnumRepository;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getObjectsByStartPositionAndMaxResult(int startPosition, int maxResult) {
        String queryString = "FROM " +
                entityClass.getSimpleName() + " e ORDER BY e.email";
        Query query = entityManager.createQuery(queryString);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return (List<User>) query.getResultList();
    }

    @Override
    public User getUserByEmail(String email) {
        String queryString = "FROM " + entityClass.getSimpleName() + " e WHERE e.email=:email";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("email", email);
        try {
            Object result = query.getSingleResult();
            return (User) result;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User getUserByUniqueNumber(String uniqueNumber) {
        String queryString = "FROM " + entityClass.getSimpleName() + " e WHERE e.uniqueNumber=:uniqueNumber";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("uniqueNumber", uniqueNumber);
        return (User) query.getSingleResult();
    }

    @Override
    public Long getCountOfUsersByRole(RoleEnumRepository role) {
        String queryString = "SELECT COUNT(e.uniqueNumber) FROM " + entityClass.getSimpleName() + " e WHERE e.role=:role";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("role", role);
        return (Long) query.getSingleResult();
    }

}
