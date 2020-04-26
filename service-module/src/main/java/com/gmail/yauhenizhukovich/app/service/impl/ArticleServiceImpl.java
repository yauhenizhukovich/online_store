package com.gmail.yauhenizhukovich.app.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhenizhukovich.app.repository.ArticleRepository;
import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Article;
import com.gmail.yauhenizhukovich.app.repository.model.Comment;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.service.ArticleService;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.exception.UserAccessDeniedException;
import com.gmail.yauhenizhukovich.app.service.model.article.UpdateArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.AddArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticlesDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService;
import com.gmail.yauhenizhukovich.app.service.util.ArticleConversionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationRules.MAX_RUNDOWN_SIZE;
import static com.gmail.yauhenizhukovich.app.service.model.user.AppUser.ROLE_PREFIX;
import static com.gmail.yauhenizhukovich.app.service.util.ArticleConversionUtil.convertDTOToDatabaseObject;
import static com.gmail.yauhenizhukovich.app.service.util.ArticleConversionUtil.convertDatabaseObjectToArticleDTO;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.COUNT_OF_ARTICLES_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getStartPositionByPageNumber;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ArticleDTO addArticle(AddArticleDTO articleDTO) throws AnonymousUserException, UserAccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals("anonymousUser")) {
            //            throw new AnonymousUserException("Anonim"); TODO
        }
        boolean isSecureAPIUser = authentication
                .getAuthorities()
                .stream()
                .anyMatch(auth -> Objects.equals(auth.getAuthority(), ROLE_PREFIX + RoleEnumService.SECURE_API_USER));
        boolean isSaleUser = authentication
                .getAuthorities()
                .stream()
                .anyMatch(auth -> Objects.equals(auth.getAuthority(), ROLE_PREFIX + RoleEnumService.SALE_USER));
        if (!isSecureAPIUser || !isSaleUser) {
            //            throw new UserAccessDeniedException();  TODO
        }
        Article article = convertDTOToDatabaseObject(articleDTO);
        User user = userRepository.getUserByEmail("secure@gmail.com");    //authentication.getName() <-- to replace TODO
        article.setAuthor(user);
        article = articleRepository.add(article);
        return convertDatabaseObjectToArticleDTO(article);
    }

    @Override
    public List<ArticlesDTO> getAllArticles() {
        List<Article> articles = articleRepository.getAll();
        return articles.stream()
                .map(ArticleConversionUtil::convertDatabaseObjectToArticlesDTO)
                .peek(article ->
                        article.setRundown(
                                articleRepository.getRundownById(
                                        MAX_RUNDOWN_SIZE, article.getId()
                                )))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticlesDTO> getArticlesByPage(Integer page) {
        int startPosition = getStartPositionByPageNumber(page, COUNT_OF_ARTICLES_BY_PAGE);
        List<Article> articles = articleRepository.getObjectsByStartPositionAndMaxResult(startPosition, COUNT_OF_ARTICLES_BY_PAGE);
        return articles.stream()
                .map(ArticleConversionUtil::convertDatabaseObjectToArticlesDTO)
                .peek(article ->
                        article.setRundown(
                                articleRepository.getRundownById(
                                        MAX_RUNDOWN_SIZE, article.getId()
                                )))
                .collect(Collectors.toList());
    }

    @Override
    public int getCountOfPages() {
        Long countOfArticles = articleRepository.getCountOfObjects();
        return getCountOfPagesByCountOfObjects(countOfArticles, COUNT_OF_ARTICLES_BY_PAGE);
    }

    @Override
    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.getById(id);
        if (article == null) {
            return null;
        }
        return convertDatabaseObjectToArticleDTO(article);
    }

    @Override
    public boolean deleteArticleById(Long id) {
        Article article = articleRepository.getById(id);
        if (article == null) {
            return false;
        }
        return articleRepository.delete(article);
    }

    @Override
    public ArticleDTO updateArticle(UpdateArticleDTO updatedArticle) {
        Article article = articleRepository.getById(updatedArticle.getId());
        updateArticle(updatedArticle, article);
        return convertDatabaseObjectToArticleDTO(article);
    }

    @Override
    public boolean deleteCommentByArticleIdAndCommentId(Long articleId, Long commentId) {
        Article article = articleRepository.getById(articleId);
        List<Comment> comments = article.getComments();
        if (!clearIfArticleHasOneComment(commentId, comments)) {
            return removeComment(commentId, comments);
        }
        return true;
    }

    private boolean removeComment(Long commentId, List<Comment> comments) {
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getId().equals(commentId)) {
                comments.remove(i);
                return true;
            }
        }
        return false;
    }

    private boolean clearIfArticleHasOneComment(Long commentId, List<Comment> comments) {
        if (comments.size() == 1) {
            if (comments.get(0).getId().equals(commentId)) {
                comments.clear();
                return true;
            }
        }
        return false;
    }

    private void updateArticle(UpdateArticleDTO updatedArticle, Article article) {
        article.setTitle(updatedArticle.getTitle());
        article.setContent(updatedArticle.getContent());
        article.setDate(LocalDate.now());
    }

}
