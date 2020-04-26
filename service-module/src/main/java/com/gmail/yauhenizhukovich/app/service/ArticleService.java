package com.gmail.yauhenizhukovich.app.service;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.exception.UserAccessDeniedException;
import com.gmail.yauhenizhukovich.app.service.model.ArticleDTO;

public interface ArticleService {

    List<Integer> getPages();

    List<ArticleDTO> getArticlesByPage(Integer pageNumber);

    ArticleDTO getArticleById(Long id);

    List<ArticleDTO> getAllArticles();

    void deleteArticleById(Long id);

    ArticleDTO addArticle(ArticleDTO article) throws AnonymousUserException, UserAccessDeniedException;

}
