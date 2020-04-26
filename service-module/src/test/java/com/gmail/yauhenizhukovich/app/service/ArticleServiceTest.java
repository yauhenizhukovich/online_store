package com.gmail.yauhenizhukovich.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhenizhukovich.app.repository.ArticleRepository;
import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Article;
import com.gmail.yauhenizhukovich.app.repository.model.Comment;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.repository.model.UserDetails;
import com.gmail.yauhenizhukovich.app.service.impl.ArticleServiceImpl;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticlesDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.UpdateArticleDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationRules.MAX_RUNDOWN_SIZE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.COUNT_OF_ARTICLES_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getStartPositionByPageNumber;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    private static final long COUNT_OF_OBJECTS = 13L;
    private static final int PAGE = 2;
    private static final int START_POSITION = getStartPositionByPageNumber(PAGE, COUNT_OF_ARTICLES_BY_PAGE);
    private static final String VALID_AUTHOR_FIRSTNAME = "Petr";
    private static final String VALID_AUTHOR_LASTNAME = "Petrov";
    public static final long VALID_COMMENT_ID = 2L;
    private static final String VALID_CONTENT = "Article content.";
    private static final Long VALID_ID = 3L;
    private static final String VALID_RUNDOWN = "Just rundown.";
    private static final String VALID_TITLE = "My new article";
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserRepository userRepository;
    private ArticleService articleService;

    @BeforeEach
    public void setup() {
        articleService = new ArticleServiceImpl(articleRepository, userRepository);
    }

    @Test
    public void getArticlesByPage_returnArticles() {
        List<Article> returnedArticles = getArticles();
        when(articleRepository.getObjectsByStartPositionAndMaxResult(START_POSITION, COUNT_OF_ARTICLES_BY_PAGE))
                .thenReturn(returnedArticles);
        when(articleRepository.getRundownById(MAX_RUNDOWN_SIZE, returnedArticles.get(0).getId())).thenReturn(VALID_RUNDOWN);
        List<ArticlesDTO> actualArticles = articleService.getArticlesByPage(PAGE);
        Assertions.assertThat(actualArticles).isNotNull();
        verify(articleRepository, times(1))
                .getObjectsByStartPositionAndMaxResult(START_POSITION, COUNT_OF_ARTICLES_BY_PAGE);
        verify(articleRepository, times(1))
                .getRundownById(MAX_RUNDOWN_SIZE, VALID_ID);
        ArticlesDTO actualArticle = actualArticles.get(0);
        Article returnedArticle = returnedArticles.get(0);
        Assertions.assertThat(actualArticle.getAuthorFirstName())
                .isEqualTo(returnedArticle.getAuthor().getUserDetails().getFirstName());
        Assertions.assertThat(actualArticle.getTitle()).isEqualTo(returnedArticle.getTitle());
        Assertions.assertThat(actualArticle.getRundown()).isEqualTo(VALID_RUNDOWN);
    }

    @Test
    public void getCountOfPages_returnPages() {
        when(articleRepository.getCountOfObjects()).thenReturn(COUNT_OF_OBJECTS);
        int pages = articleService.getCountOfPages();
        verify(articleRepository, times(1)).getCountOfObjects();
        Assertions.assertThat(pages).isEqualTo(getCountOfPagesByCountOfObjects(COUNT_OF_OBJECTS, COUNT_OF_ARTICLES_BY_PAGE));
    }

    @Test
    public void getArticleById_returnArticle() {
        Article returnedArticle = getArticle();
        when(articleRepository.getById(VALID_ID)).thenReturn(returnedArticle);
        ArticleDTO actualArticle = articleService.getArticleById(VALID_ID);
        Assertions.assertThat(actualArticle).isNotNull();
        verify(articleRepository, times(1)).getById(VALID_ID);
        Assertions.assertThat(actualArticle.getContent()).isEqualTo(returnedArticle.getContent());
        Assertions.assertThat(actualArticle.getAuthorLastName())
                .isEqualTo(returnedArticle.getAuthor().getUserDetails().getLastName());
        Assertions.assertThat(actualArticle.getDate()).isEqualTo(returnedArticle.getDate());
    }

    @Test
    public void getAllArticles_returnArticles() {
        List<Article> returnedArticles = getArticles();
        when(articleRepository.getAll()).thenReturn(returnedArticles);
        when(articleRepository.getRundownById(MAX_RUNDOWN_SIZE, VALID_ID)).thenReturn(VALID_RUNDOWN);
        List<ArticlesDTO> actualArticles = articleService.getAllArticles();
        Assertions.assertThat(actualArticles).isNotNull();
        verify(articleRepository, times(1)).getAll();
        ArticlesDTO actualArticle = actualArticles.get(0);
        Article returnedArticle = returnedArticles.get(0);
        Assertions.assertThat(actualArticle.getRundown()).isEqualTo(VALID_RUNDOWN);
        Assertions.assertThat(actualArticle.getTitle()).isEqualTo(returnedArticle.getTitle());
        Assertions.assertThat(actualArticle.getAuthorFirstName())
                .isEqualTo(returnedArticle.getAuthor().getUserDetails().getFirstName());
    }

    @Test
    public void deleteArticleById_returnTrue() {
        Article returnedArticle = getArticle();
        when(articleRepository.getById(VALID_ID)).thenReturn(returnedArticle);
        when(articleRepository.delete(returnedArticle)).thenReturn(true);
        boolean isDeleted = articleService.deleteArticleById(VALID_ID);
        Assertions.assertThat(isDeleted).isTrue();
    }

    @Test
    public void deleteArticleByNonexistentId_returnFalse() {
        when(articleRepository.getById(VALID_ID)).thenReturn(null);
        boolean isDeleted = articleService.deleteArticleById(VALID_ID);
        Assertions.assertThat(isDeleted).isFalse();
    }

    //    @Test                                                         TODO
    //    public void addArticle_returnArticle() {}

    @Test
    public void updateArticle_returnUpdatedArticle() {
        UpdateArticleDTO updateArticle = new UpdateArticleDTO();
        updateArticle.setId(VALID_ID);
        Article returnedArticle = getArticle();
        when(articleRepository.getById(updateArticle.getId())).thenReturn(returnedArticle);
        ArticleDTO actualArticle = articleService.updateArticle(updateArticle);
        Assertions.assertThat(actualArticle).isNotNull();
        verify(articleRepository, times(1)).getById(VALID_ID);
        Assertions.assertThat(updateArticle.getTitle()).isEqualTo(actualArticle.getTitle());
        Assertions.assertThat(updateArticle.getContent()).isEqualTo(actualArticle.getContent());
        Assertions.assertThat(returnedArticle.getAuthor().getUserDetails().getFirstName())
                .isEqualTo(actualArticle.getAuthorFirstName());
    }

    @Test
    public void deleteCommentByArticleIdAndCommentId_returnTrue() {
        Article article = getArticleWithComment();
        when(articleRepository.getById(VALID_ID)).thenReturn(article);
        boolean isDeleted = articleService.deleteCommentByArticleIdAndCommentId(VALID_ID, VALID_COMMENT_ID);
        Assertions.assertThat(isDeleted).isNotNull();
        verify(articleRepository, times(1)).getById(VALID_ID);
        Assertions.assertThat(isDeleted).isTrue();
    }

    @Test
    public void deleteCommentByArticleIdAndNonexistentCommentId_returnFalse() {
        Article article = getArticleWithComment();
        when(articleRepository.getById(VALID_ID)).thenReturn(article);
        boolean isDeleted = articleService.deleteCommentByArticleIdAndCommentId(VALID_ID, 13L);
        Assertions.assertThat(isDeleted).isNotNull();
        verify(articleRepository, times(1)).getById(VALID_ID);
        Assertions.assertThat(isDeleted).isFalse();
    }

    private Article getArticle() {
        Article article = new Article();
        article.setId(VALID_ID);
        article.setContent(VALID_CONTENT);
        article.setDate(LocalDate.now());
        article.setTitle(VALID_TITLE);
        User user = new User();
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(VALID_AUTHOR_FIRSTNAME);
        userDetails.setLastName(VALID_AUTHOR_LASTNAME);
        user.setUserDetails(userDetails);
        article.setAuthor(user);
        return article;
    }

    private Article getArticleWithComment() {
        Article article = new Article();
        article.setId(VALID_ID);
        article.setContent(VALID_CONTENT);
        article.setDate(LocalDate.now());
        article.setTitle(VALID_TITLE);
        User user = new User();
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(VALID_AUTHOR_FIRSTNAME);
        userDetails.setLastName(VALID_AUTHOR_LASTNAME);
        user.setUserDetails(userDetails);
        article.setAuthor(user);
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setId(VALID_COMMENT_ID);
        comments.add(comment);
        article.setComments(comments);
        return article;
    }

    private List<Article> getArticles() {
        List<Article> articles = new ArrayList<>();
        articles.add(getArticle());
        return articles;
    }

}
