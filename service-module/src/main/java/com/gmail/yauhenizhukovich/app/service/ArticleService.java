package com.gmail.yauhenizhukovich.app.service;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.exception.UserAccessDeniedException;
import com.gmail.yauhenizhukovich.app.service.model.article.UpdateArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.AddArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticlesDTO;

public interface ArticleService {

    List<ArticlesDTO> getArticlesByPage(Integer pageNumber);

    int getCountOfPages();

    ArticleDTO getArticleById(Long id);

    List<ArticlesDTO> getAllArticles();

    boolean deleteArticleById(Long id);

    ArticleDTO addArticle(AddArticleDTO article) throws AnonymousUserException, UserAccessDeniedException;

    ArticleDTO updateArticle(UpdateArticleDTO updatedArticle);

    boolean deleteCommentByArticleIdAndCommentId(Long articleId, Long commentId);

}
