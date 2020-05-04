package com.gmail.yauhenizhukovich.app.service.util.conversion;

import java.time.LocalDate;
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

    public static Article convertDTOToDatabaseObject(AddArticleDTO articleDTO) {
        Article article = new Article();
        if (articleDTO.getDate() == null) {
            article.setDate(LocalDate.now());
        } else {
            article.setDate(articleDTO.getDate());
        }
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        return article;
    }

    public static ArticlesDTO convertDatabaseObjectToArticlesDTO(Article article) {
        ArticlesDTO articleDTO = new ArticlesDTO();
        articleDTO.setId(article.getId());
        articleDTO.setDate(article.getDate());
        articleDTO.setTitle(article.getTitle());
        User author = article.getAuthor();
        setAuthorName(articleDTO, author);
        return articleDTO;
    }

    public static ArticleDTO convertDatabaseObjectToArticleDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setDate(article.getDate());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setContent(article.getContent());
        User author = article.getAuthor();
        setAuthorName(articleDTO, author);
        List<Comment> comments = article.getComments();
        setArticleComments(articleDTO, comments);
        return articleDTO;
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

    private static void setArticleComments(ArticleDTO articleDTO, List<Comment> comments) {
        if (comments != null) {
            List<CommentDTO> commentsDTO = comments.stream()
                    .map(ArticleConversionUtil::convertDatabaseCommentToCommentDTO)
                    .sorted(Comparator.comparing(CommentDTO::getDate)
                            .reversed())
                    .collect(Collectors.toList());
            articleDTO.setComments(commentsDTO);
        }
    }

    private static void setAuthorName(ArticlesDTO articleDTO, User author) {
        if (author != null) {
            UserDetails userDetails = author.getUserDetails();
            articleDTO.setAuthorFirstName(userDetails.getFirstName());
            articleDTO.setAuthorLastName(userDetails.getLastName());
        }
    }

    private static void setAuthorName(ArticleDTO articleDTO, User author) {
        if (author != null) {
            UserDetails authorDetails = author.getUserDetails();
            articleDTO.setAuthorFirstName(authorDetails.getFirstName());
            articleDTO.setAuthorLastName(authorDetails.getLastName());
        }
    }

}
