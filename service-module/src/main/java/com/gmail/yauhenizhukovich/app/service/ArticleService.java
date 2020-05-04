package com.gmail.yauhenizhukovich.app.service;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.article.AddArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticlesDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.UpdateArticleDTO;

public interface ArticleService {

    ObjectsDTOAndPagesEntity<ArticlesDTO> getArticlesByPage(Integer pageNumber);

    ArticleDTO getArticleById(Long id);

    List<ArticlesDTO> getAllArticles();

    boolean deleteArticleById(Long id);

    ArticleDTO addArticle(AddArticleDTO article) throws AnonymousUserException;

    ArticleDTO updateArticle(UpdateArticleDTO updatedArticle);

    boolean deleteCommentByArticleIdAndCommentId(Long articleId, Long commentId);

}
