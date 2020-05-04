package com.gmail.yauhenizhukovich.app.repository.impl;

import java.util.List;
import javax.persistence.Query;

import com.gmail.yauhenizhukovich.app.repository.ArticleRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Article;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Long, Article> implements ArticleRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Article> getObjectsByStartPositionAndMaxResult(int startPosition, int maxResult) {
        String queryString = "FROM " +
                entityClass.getSimpleName() + " e ORDER BY e.date DESC";
        Query query = entityManager.createQuery(queryString);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return (List<Article>) query.getResultList();
    }

    @Override
    public String getRundownById(int rundownLength, Long id) {
        Query query = entityManager.createNativeQuery("SELECT LEFT (content, ?) FROM article WHERE id=?");
        query.setParameter(1, rundownLength);
        query.setParameter(2, id);
        return (String) query.getSingleResult();
    }

}
