package com.gmail.yauhenizhukovich.app.repository;

import java.util.List;

import com.gmail.yauhenizhukovich.app.repository.model.Article;

public interface ArticleRepository extends GenericRepository<Long, Article> {

    List<Article> getPaginatedArticlesUntilCurrentDate(int startPosition, int countOfArticlesByPage);

    String getRundownById(int rundownLength, Long id);

}
