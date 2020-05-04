package com.gmail.yauhenizhukovich.app.repository.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.RoleEnumRepository;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getPaginatedObjects(int startPosition, int maxResult) {
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
            logger.info("User was searched for by a nonexistent email(or just checked for email existence).");
            return null;
        }
    }

    @Override
    public User getUserByUniqueNumber(String uniqueNumber) {
        String queryString = "FROM " + entityClass.getSimpleName() + " e WHERE e.uniqueNumber=:uniqueNumber";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("uniqueNumber", uniqueNumber);
        try {
            Object result = query.getSingleResult();
            return (User) result;
        } catch (NoResultException e) {
            logger.info("User was searched for by a nonexistent unique number.");
            return null;
        }
    }

    @Override
    public Long getCountOfUsersByRole(RoleEnumRepository role) {
        String queryString = "SELECT COUNT(e.uniqueNumber) FROM " + entityClass.getSimpleName() + " e WHERE e.role=:role";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("role", role);
        return (Long) query.getSingleResult();
    }

}
