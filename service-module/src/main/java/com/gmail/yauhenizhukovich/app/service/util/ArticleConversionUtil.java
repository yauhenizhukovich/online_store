package com.gmail.yauhenizhukovich.app.service.util;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.gmail.yauhenizhukovich.app.repository.model.Article;
import com.gmail.yauhenizhukovich.app.repository.model.ArticleDetails;
import com.gmail.yauhenizhukovich.app.repository.model.Comment;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.repository.model.UserDetails;
import com.gmail.yauhenizhukovich.app.service.model.ArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.CommentDTO;

public class ArticleConversionUtil {

    public static ArticleDTO convertDatabaseObjectToDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setDate(article.getDate());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setRundown(article.getRundown());
        ArticleDetails articleDetails = article.getArticleDetails();
        articleDTO.setContent(articleDetails.getContent());
        User author = article.getAuthor();
        UserDetails authorDetails = author.getUserDetails();
        articleDTO.setAuthorFirstName(authorDetails.getFirstName());
        articleDTO.setAuthorLastName(authorDetails.getLastName());
        List<Comment> comments = article.getComments();
        if (comments != null) {
            List<CommentDTO> commentsDTO = comments.stream()
                    .map(ArticleConversionUtil::getCommentDTO)
                    .sorted(Comparator.comparing(CommentDTO::getDate)
                            .reversed())
                    .collect(Collectors.toList());
            articleDTO.setComments(commentsDTO);
        }
        return articleDTO;
    }

    public static Article convertDTOToDatabaseObject(ArticleDTO articleDTO) {
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setRundown(articleDTO.getRundown());
        ArticleDetails articleDetails = new ArticleDetails();
        articleDetails.setArticle(article);
        articleDetails.setContent(articleDTO.getContent());
        article.setArticleDetails(articleDetails);
        return article;
    }

    private static CommentDTO getCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        User author = comment.getAuthor();
        UserDetails userDetails = author.getUserDetails();
        commentDTO.setAuthorFirstName(userDetails.getFirstName());
        commentDTO.setAuthorLastName(userDetails.getLastName());
        commentDTO.setDate(comment.getDate());
        commentDTO.setContent(comment.getContent());
        return commentDTO;
    }

}
