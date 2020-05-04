package com.gmail.yauhenizhukovich.app.service.impl;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhenizhukovich.app.repository.ArticleRepository;
import com.gmail.yauhenizhukovich.app.repository.CommentRepository;
import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Article;
import com.gmail.yauhenizhukovich.app.repository.model.Comment;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.service.ArticleService;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.article.AddArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticlesDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.UpdateArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService;
import com.gmail.yauhenizhukovich.app.service.util.conversion.ArticleConversionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.gmail.yauhenizhukovich.app.service.constant.AuthorityConstant.ANONYMOUS_USER_NAME;
import static com.gmail.yauhenizhukovich.app.service.constant.AuthorityConstant.ROLE_PREFIX;
import static com.gmail.yauhenizhukovich.app.service.constant.PageConstant.COUNT_OF_ARTICLES_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.PageConstant.MAX_RUNDOWN_SIZE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getStartPositionByPageNumber;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.ArticleConversionUtil.convertDTOToDatabaseObject;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.ArticleConversionUtil.convertDatabaseObjectToArticleDTO;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ArticleServiceImpl(
            ArticleRepository articleRepository,
            UserRepository userRepository,
            CommentRepository commentRepository
    ) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ArticleDTO addArticle(AddArticleDTO articleDTO) throws AnonymousUserException {
        Authentication authentication = getAuthentication();
        if (authentication.getName().equals(ANONYMOUS_USER_NAME)) {
            logger.error("Anonymous user tried to add an article.");
            throw new AnonymousUserException();
        }
        User user = userRepository.getUserByEmail(authentication.getName());
        Article article = convertDTOToDatabaseObject(articleDTO);
        article.setAuthor(user);
        article = articleRepository.add(article);
        return convertDatabaseObjectToArticleDTO(article);
    }

    @Override
    public List<ArticlesDTO> getAllArticles() {
        List<Article> articles = articleRepository.getAll();
        return getArticlesDTO(articles);
    }

    @Override
    public ObjectsDTOAndPagesEntity<ArticlesDTO> getArticlesByPage(Integer page) {
        int startPosition = getStartPositionByPageNumber(page, COUNT_OF_ARTICLES_BY_PAGE);
        Authentication authentication = getAuthentication();
        Optional<String> role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst();
        List<Article> articles = new ArrayList<>();
        if (role.isPresent()) {
            articles = getArticlesByRole(startPosition, role.get());
        }
        List<ArticlesDTO> articlesDTO = getArticlesDTO(articles);
        int pages = getPages();
        return new ObjectsDTOAndPagesEntity<>(pages, articlesDTO);
    }

    @Override
    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.getById(id);
        if (article == null) {
            logger.info(getAuthentication().getName() + " tried to get nonexistent article.");
            return null;
        }
        if (checkCustomerRoleAndArticleForFuture(article)) {
            logger.info(getAuthentication().getName() + " tried to get closed article.");
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
        Comment comment = commentRepository.getById(commentId);
        List<Comment> comments = article.getComments();
        return comments.remove(comment);
    }

    private int getPages() {
        Long countOfArticles = articleRepository.getCountOfObjects();
        return getCountOfPagesByCountOfObjects(countOfArticles, COUNT_OF_ARTICLES_BY_PAGE);
    }

    private List<ArticlesDTO> getArticlesDTO(List<Article> articles) {
        return articles.stream()
                .map(ArticleConversionUtil::convertDatabaseObjectToArticlesDTO)
                .peek(article ->
                        article.setRundown(
                                articleRepository.getRundownById(
                                        MAX_RUNDOWN_SIZE,
                                        article.getId()
                                )))
                .collect(Collectors.toList());
    }

    private List<Article> getArticlesByRole(int startPosition, String role) {
        List<Article> articles;
        if (role.equals(ROLE_PREFIX + RoleEnumService.SALE_USER.name())) {
            articles = articleRepository.getPaginatedObjects(startPosition, COUNT_OF_ARTICLES_BY_PAGE);
        } else {
            articles = articleRepository.getPaginatedArticlesUntilCurrentDate(startPosition,
                    COUNT_OF_ARTICLES_BY_PAGE);
        }
        return articles;
    }

    private boolean checkCustomerRoleAndArticleForFuture(Article article) {
        Authentication authentication = getAuthentication();
        boolean isCustomer = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals(ROLE_PREFIX + RoleEnumService.CUSTOMER_USER.name()));
        if (isCustomer) {
            return article.getDate().isAfter(LocalDate.now());
        }
        return false;
    }

    private void updateArticle(UpdateArticleDTO updatedArticle, Article article) {
        article.setTitle(updatedArticle.getTitle());
        article.setContent(updatedArticle.getContent());
        article.setDate(LocalDate.now());
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
