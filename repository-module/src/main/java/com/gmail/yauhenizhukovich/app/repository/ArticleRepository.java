package com.gmail.yauhenizhukovich.app.repository;

import com.gmail.yauhenizhukovich.app.repository.model.Article;

public interface ArticleRepository extends GenericRepository<Long, Article> {

    String getRundownById(int rundownLength, Long id);

}
