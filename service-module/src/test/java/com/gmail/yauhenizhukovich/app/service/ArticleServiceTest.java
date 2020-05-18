package com.gmail.yauhenizhukovich.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gmail.yauhenizhukovich.app.repository.ArticleRepository;
import com.gmail.yauhenizhukovich.app.repository.CommentRepository;
import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Article;
import com.gmail.yauhenizhukovich.app.repository.model.Comment;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.repository.model.UserDetails;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.impl.ArticleServiceImpl;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.article.AddArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticlesDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.UpdateArticleDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.gmail.yauhenizhukovich.app.service.constant.AuthorityConstant.ANONYMOUS_USER_NAME;
import static com.gmail.yauhenizhukovich.app.service.constant.AuthorityConstant.ROLE_PREFIX;
import static com.gmail.yauhenizhukovich.app.service.constant.PageConstant.COUNT_OF_ARTICLES_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.PageConstant.MAX_RUNDOWN_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.COUNT_OF_OBJECTS;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.PAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.START_POSITION;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_AUTHOR_FIRSTNAME;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_AUTHOR_LASTNAME;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_COMMENT_ID;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_CONTENT;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_EMAIL;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_ID;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_RUNDOWN;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_TITLE;
import static com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException.ANONYMOUS_USER_EXCEPTION_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService.CUSTOMER_USER;
import static com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService.SALE_USER;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static com.gmail.yauhenizhukovich.app.service.util.ServiceUnitTestUtil.getUser;
import static com.gmail.yauhenizhukovich.app.service.util.ServiceUnitTestUtil.mockAuthentication;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.ArticleConversionUtil.convertDTOToDatabaseObject;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.ArticleConversionUtil.convertDatabaseObjectToArticleDTO;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    private ArticleService articleService;

    @BeforeEach
    public void setup() {
        articleService = new ArticleServiceImpl(articleRepository, userRepository, commentRepository);
    }

    @Test
    public void addArticle_callDatabase() throws AnonymousUserException {
        mockAuthentication(VALID_EMAIL);
        when(userRepository.getUserByEmail(VALID_EMAIL)).thenReturn(getUser());
        AddArticleDTO addArticleDTO = getAddArticle();
        Article addArticle = convertDTOToDatabaseObject(addArticleDTO);
        when(articleRepository.add(addArticle)).thenReturn(addArticle);
        articleService.addArticle(addArticleDTO);
        verify(userRepository, times(1)).getUserByEmail(VALID_EMAIL);
        verify(articleRepository, times(1)).add(addArticle);
    }

    @Test
    public void addArticleByAnonymous_throwAnonymousUserException() {
        mockAuthentication(ANONYMOUS_USER_NAME);
        org.junit.jupiter.api.Assertions.assertThrows(
                AnonymousUserException.class,
                () -> articleService.addArticle(getAddArticle()),
                ANONYMOUS_USER_EXCEPTION_MESSAGE
        );
    }

    @Test
    public void addArticle_returnArticleCheckTitleContentDate() throws AnonymousUserException {
        mockAuthentication(VALID_EMAIL);
        when(userRepository.getUserByEmail(VALID_EMAIL)).thenReturn(getUser());
        AddArticleDTO addArticleDTO = getAddArticle();
        Article addArticle = convertDTOToDatabaseObject(addArticleDTO);
        when(articleRepository.add(addArticle)).thenReturn(addArticle);
        ArticleDTO actualAddedArticle = articleService.addArticle(addArticleDTO);
        ArticleDTO expectedAddedArticle = convertDatabaseObjectToArticleDTO(addArticle);
        Assertions.assertThat(actualAddedArticle.getTitle()).isEqualTo(expectedAddedArticle.getTitle());
        Assertions.assertThat(actualAddedArticle.getContent()).isEqualTo(expectedAddedArticle.getContent());
        Assertions.assertThat(actualAddedArticle.getDate()).isEqualTo(expectedAddedArticle.getDate());
    }

    @Test
    public void addArticle_returnArticleCheckAuthorNameAndComments() throws AnonymousUserException {
        mockAuthentication(VALID_EMAIL);
        when(userRepository.getUserByEmail(VALID_EMAIL)).thenReturn(getUser());
        AddArticleDTO addArticleDTO = getAddArticle();
        Article addArticle = convertDTOToDatabaseObject(addArticleDTO);
        when(articleRepository.add(addArticle)).thenReturn(addArticle);
        ArticleDTO actualAddedArticle = articleService.addArticle(addArticleDTO);
        ArticleDTO expectedAddedArticle = convertDatabaseObjectToArticleDTO(addArticle);
        Assertions.assertThat(actualAddedArticle.getAuthorLastName()).isEqualTo(expectedAddedArticle.getAuthorLastName());
        Assertions.assertThat(actualAddedArticle.getAuthorFirstName()).isEqualTo(expectedAddedArticle.getAuthorFirstName());
        Assertions.assertThat(actualAddedArticle.getComments()).isEqualTo(expectedAddedArticle.getComments());
    }

    @Test
    public void getAllArticles_callDatabase() {
        List<Article> returnedArticles = getArticles();
        when(articleRepository.getAll()).thenReturn(returnedArticles);
        when(articleRepository.getRundownById(MAX_RUNDOWN_SIZE, VALID_ID)).thenReturn(VALID_RUNDOWN);
        List<ArticlesDTO> actualArticles = articleService.getAllArticles();
        Assertions.assertThat(actualArticles).isNotNull();
        verify(articleRepository, times(1)).getAll();
    }

    @Test
    public void getAllArticles_returnArticlesCheckRundownTitleDate() {
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
        Assertions.assertThat(actualArticle.getDate()).isEqualTo(returnedArticle.getDate());
    }

    @Test
    public void getAllArticles_returnArticlesCheckAuthorName() {
        List<Article> returnedArticles = getArticles();
        when(articleRepository.getAll()).thenReturn(returnedArticles);
        when(articleRepository.getRundownById(MAX_RUNDOWN_SIZE, VALID_ID)).thenReturn(VALID_RUNDOWN);
        List<ArticlesDTO> actualArticles = articleService.getAllArticles();
        Assertions.assertThat(actualArticles).isNotNull();
        verify(articleRepository, times(1)).getAll();
        ArticlesDTO actualArticle = actualArticles.get(0);
        Article returnedArticle = returnedArticles.get(0);
        Assertions.assertThat(actualArticle.getAuthorFirstName())
                .isEqualTo(returnedArticle.getAuthor().getUserDetails().getFirstName());
        Assertions.assertThat(actualArticle.getAuthorLastName())
                .isEqualTo(returnedArticle.getAuthor().getUserDetails().getLastName());
    }

    @Test
    public void getArticlesByPageForSaleUser_callDatabase() {
        Collection authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + SALE_USER.name()));
        mockAuthentication(authorities);
        List<Article> returnedArticles = getArticles();
        when(articleRepository.getPaginatedObjects(START_POSITION, COUNT_OF_ARTICLES_BY_PAGE))
                .thenReturn(returnedArticles);
        when(articleRepository.getCountOfObjects()).thenReturn(COUNT_OF_OBJECTS);
        when(articleRepository.getRundownById(MAX_RUNDOWN_SIZE, returnedArticles.get(0).getId())).thenReturn(VALID_RUNDOWN);
        ObjectsDTOAndPagesEntity<ArticlesDTO> actualArticles = articleService.getArticlesByPage(PAGE);
        Assertions.assertThat(actualArticles).isNotNull();
        verify(articleRepository, times(1))
                .getPaginatedObjects(START_POSITION, COUNT_OF_ARTICLES_BY_PAGE);
        verify(articleRepository, times(1))
                .getCountOfObjects();
        verify(articleRepository, times(1))
                .getRundownById(MAX_RUNDOWN_SIZE, VALID_ID);
    }

    @Test
    public void getArticlesByPageForSaleUser_returnArticles() {
        Collection authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + SALE_USER.name()));
        mockAuthentication(authorities);
        List<Article> returnedArticles = getArticles();
        when(articleRepository.getPaginatedObjects(START_POSITION, COUNT_OF_ARTICLES_BY_PAGE))
                .thenReturn(returnedArticles);
        when(articleRepository.getRundownById(MAX_RUNDOWN_SIZE, returnedArticles.get(0).getId())).thenReturn(VALID_RUNDOWN);
        ObjectsDTOAndPagesEntity<ArticlesDTO> actualArticles = articleService.getArticlesByPage(PAGE);
        Assertions.assertThat(actualArticles).isNotNull();
        ArticlesDTO actualArticle = actualArticles.getObjects().get(0);
        Article returnedArticle = returnedArticles.get(0);
        Assertions.assertThat(actualArticle.getAuthorFirstName())
                .isEqualTo(returnedArticle.getAuthor().getUserDetails().getFirstName());
        Assertions.assertThat(actualArticle.getTitle()).isEqualTo(returnedArticle.getTitle());
        Assertions.assertThat(actualArticle.getRundown()).isEqualTo(VALID_RUNDOWN);
    }

    @Test
    public void getArticlesByPageForCustomerUser_returnArticles() {
        Collection authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + CUSTOMER_USER.name()));
        mockAuthentication(authorities);
        List<Article> returnedArticles = getArticles();
        when(articleRepository.getPaginatedArticlesUntilCurrentDate(START_POSITION, COUNT_OF_ARTICLES_BY_PAGE))
                .thenReturn(returnedArticles);
        when(articleRepository.getRundownById(MAX_RUNDOWN_SIZE, returnedArticles.get(0).getId())).thenReturn(VALID_RUNDOWN);
        ObjectsDTOAndPagesEntity<ArticlesDTO> actualArticles = articleService.getArticlesByPage(PAGE);
        Assertions.assertThat(actualArticles).isNotNull();
        ArticlesDTO actualArticle = actualArticles.getObjects().get(0);
        Article returnedArticle = returnedArticles.get(0);
        Assertions.assertThat(actualArticle.getAuthorFirstName())
                .isEqualTo(returnedArticle.getAuthor().getUserDetails().getFirstName());
        Assertions.assertThat(actualArticle.getTitle()).isEqualTo(returnedArticle.getTitle());
        Assertions.assertThat(actualArticle.getRundown()).isEqualTo(VALID_RUNDOWN);
    }

    @Test
    public void getArticlesByPageForCustomerUser_returnPages() {
        Collection authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + CUSTOMER_USER.name()));
        mockAuthentication(authorities);
        when(articleRepository.getCountOfObjects()).thenReturn(COUNT_OF_OBJECTS);
        ObjectsDTOAndPagesEntity<ArticlesDTO> actualArticles = articleService.getArticlesByPage(PAGE);
        Assertions.assertThat(actualArticles).isNotNull();
        Assertions.assertThat(actualArticles.getPages())
                .isEqualTo(getCountOfPagesByCountOfObjects(COUNT_OF_OBJECTS, COUNT_OF_ARTICLES_BY_PAGE));
    }

    @Test
    public void getArticleById_returnArticle() {
        Article returnedArticle = getArticle();
        when(articleRepository.getById(VALID_ID)).thenReturn(returnedArticle);
        Collection authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + CUSTOMER_USER.name()));
        mockAuthentication(authorities);
        ArticleDTO actualArticle = articleService.getArticleById(VALID_ID);
        Assertions.assertThat(actualArticle).isNotNull();
        verify(articleRepository, times(1)).getById(VALID_ID);
        Assertions.assertThat(actualArticle.getContent()).isEqualTo(returnedArticle.getContent());
        Assertions.assertThat(actualArticle.getAuthorLastName())
                .isEqualTo(returnedArticle.getAuthor().getUserDetails().getLastName());
        Assertions.assertThat(actualArticle.getDate()).isEqualTo(returnedArticle.getDate());
    }

    @Test
    public void getNonexistentArticleById_returnNull() {
        when(articleRepository.getById(VALID_ID)).thenReturn(null);
        mockAuthentication(VALID_EMAIL);
        ArticleDTO actualArticle = articleService.getArticleById(VALID_ID);
        Assertions.assertThat(actualArticle).isNull();
        verify(articleRepository, times(1)).getById(VALID_ID);
    }

    @Test
    public void getArticleForFutureByCustomer_returnNull() {
        Article returnedArticle = getArticle();
        returnedArticle.setDate(LocalDate.of(2021, 1, 1));
        when(articleRepository.getById(VALID_ID)).thenReturn(returnedArticle);
        Collection authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + CUSTOMER_USER.name()));
        mockAuthentication(authorities);
        ArticleDTO actualArticle = articleService.getArticleById(VALID_ID);
        Assertions.assertThat(actualArticle).isNull();
        verify(articleRepository, times(1)).getById(VALID_ID);
    }

    @Test
    public void deleteArticleById_callDatabase() {
        Article returnedArticle = getArticle();
        when(articleRepository.getById(VALID_ID)).thenReturn(returnedArticle);
        when(articleRepository.delete(returnedArticle)).thenReturn(true);
        boolean isDeleted = articleService.deleteArticleById(VALID_ID);
        Assertions.assertThat(isDeleted).isNotNull();
        verify(articleRepository, times(1)).getById(VALID_ID);
        verify(articleRepository, times(1)).delete(returnedArticle);
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

    @Test
    public void updateArticle_callDatabase() {
        UpdateArticleDTO updateArticle = new UpdateArticleDTO();
        updateArticle.setId(VALID_ID);
        Article returnedArticle = getArticle();
        when(articleRepository.getById(updateArticle.getId())).thenReturn(returnedArticle);
        ArticleDTO actualArticle = articleService.updateArticle(updateArticle);
        Assertions.assertThat(actualArticle).isNotNull();
        verify(articleRepository, times(1)).getById(VALID_ID);
    }

    @Test
    public void updateArticle_returnUpdatedArticle() {
        UpdateArticleDTO updateArticle = new UpdateArticleDTO();
        updateArticle.setId(VALID_ID);
        Article returnedArticle = getArticle();
        when(articleRepository.getById(updateArticle.getId())).thenReturn(returnedArticle);
        ArticleDTO actualArticle = articleService.updateArticle(updateArticle);
        Assertions.assertThat(actualArticle).isNotNull();
        Assertions.assertThat(updateArticle.getTitle()).isEqualTo(actualArticle.getTitle());
        Assertions.assertThat(updateArticle.getContent()).isEqualTo(actualArticle.getContent());
        Assertions.assertThat(returnedArticle.getAuthor().getUserDetails().getFirstName())
                .isEqualTo(actualArticle.getAuthorFirstName());
    }

    @Test
    public void deleteCommentByArticleIdAndCommentId_returnTrue() {
        Article article = getArticleWithComment();
        when(articleRepository.getById(VALID_ID)).thenReturn(article);
        Comment comment = getComment();
        when(commentRepository.getById(VALID_COMMENT_ID)).thenReturn(comment);
        boolean isDeleted = articleService.deleteCommentByArticleIdAndCommentId(VALID_ID, VALID_COMMENT_ID);
        Assertions.assertThat(isDeleted).isNotNull();
        verify(articleRepository, times(1)).getById(VALID_ID);
        Assertions.assertThat(isDeleted).isTrue();
    }

    @Test
    public void deleteCommentByArticleIdAndNonexistentCommentId_returnFalse() {
        Article article = getArticleWithComment();
        when(articleRepository.getById(VALID_ID)).thenReturn(article);
        when(commentRepository.getById(VALID_COMMENT_ID)).thenReturn(null);
        boolean isDeleted = articleService.deleteCommentByArticleIdAndCommentId(VALID_ID, VALID_COMMENT_ID);
        Assertions.assertThat(isDeleted).isNotNull();
        verify(articleRepository, times(1)).getById(VALID_ID);
        Assertions.assertThat(isDeleted).isFalse();
    }

    private AddArticleDTO getAddArticle() {
        AddArticleDTO addArticleDTO = new AddArticleDTO();
        addArticleDTO.setContent(VALID_CONTENT);
        addArticleDTO.setTitle(VALID_TITLE);
        addArticleDTO.setDate(LocalDate.now());
        return addArticleDTO;
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

    private Comment getComment() {
        Comment comment = new Comment();
        comment.setId(VALID_COMMENT_ID);
        return comment;
    }

    private List<Article> getArticles() {
        List<Article> articles = new ArrayList<>();
        articles.add(getArticle());
        return articles;
    }

}
