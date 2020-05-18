package com.gmail.yauhenizhukovich.app.repository.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gmail.yauhenizhukovich.app.repository.ItemRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepositoryImpl extends GenericRepositoryImpl<Long, Item> implements ItemRepository {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    @SuppressWarnings("unchecked")
    public List<Item> getPaginatedObjects(int startPosition, int maxResult) {
        String queryString = "FROM " +
                entityClass.getSimpleName() + " e ORDER BY e.name";
        Query query = entityManager.createQuery(queryString);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return (List<Item>) query.getResultList();
    }

    @Override
    public Item getItemByUniqueNumber(String uniqueNumber) {
        String queryString = "FROM " + entityClass.getSimpleName() + " e WHERE e.uniqueNumber=:uniqueNumber";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("uniqueNumber", uniqueNumber);
        try {
            Object result = query.getSingleResult();
            return (Item) result;
        } catch (NoResultException e) {
            logger.info("Item was searched for by a nonexistent unique number.");
            return null;
        }
    }

}
