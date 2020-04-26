package com.gmail.yauhenizhukovich.app.service.util;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.gmail.yauhenizhukovich.app.repository.model.Article;
import com.gmail.yauhenizhukovich.app.repository.model.Comment;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.repository.model.UserDetails;
import com.gmail.yauhenizhukovich.app.service.model.article.AddArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticlesDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.CommentDTO;

public class ArticleConversionUtil {

    public static ArticlesDTO convertDatabaseObjectToArticlesDTO(Article article) {
        ArticlesDTO articleDTO = new ArticlesDTO();
        articleDTO.setId(article.getId());
        articleDTO.setDate(article.getDate());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setAuthorFirstName(article.getAuthor().getUserDetails().getFirstName());
        articleDTO.setAuthorLastName(article.getAuthor().getUserDetails().getLastName());
        return articleDTO;
    }

    public static ArticleDTO convertDatabaseObjectToArticleDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setDate(article.getDate());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setContent(article.getContent());
        User author = article.getAuthor();
        UserDetails authorDetails = author.getUserDetails();
        articleDTO.setAuthorFirstName(authorDetails.getFirstName());
        articleDTO.setAuthorLastName(authorDetails.getLastName());
        List<Comment> comments = article.getComments();
        if (comments != null) {
            List<CommentDTO> commentsDTO = comments.stream()
                    .map(ArticleConversionUtil::convertDatabaseCommentToCommentDTO)
                    .sorted(Comparator.comparing(CommentDTO::getDate)
                            .reversed())
                    .collect(Collectors.toList());
            articleDTO.setComments(commentsDTO);
        }
        return articleDTO;
    }

    public static Article convertDTOToDatabaseObject(AddArticleDTO articleDTO) {
        Article article = new Article();
        article.setDate(articleDTO.getDate());
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        return article;
    }

    private static CommentDTO convertDatabaseCommentToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        User author = comment.getAuthor();
        UserDetails userDetails = author.getUserDetails();
        commentDTO.setAuthorFirstName(userDetails.getFirstName());
        commentDTO.setAuthorLastName(userDetails.getLastName());
        commentDTO.setDate(comment.getDate());
        commentDTO.setContent(comment.getContent());
        return commentDTO;
    }

}
