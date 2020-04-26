package com.gmail.yauhenizhukovich.app.web.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhenizhukovich.app.service.ArticleService;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticlesDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.CommentDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleController.class)
@WithMockUser(roles = "CUSTOMER_USER")
class ArticleControllerTest {

    private static final int PAGE = 5;
    public static final String VALID_COMMENT_CONTENT = "Comment content.";
    private static final LocalDate VALID_DATE = LocalDate.now();
    public static final Long VALID_ID = 3L;
    private static final String VALID_TITLE = "Good article!";
    private static final String VALID_AUTHOR_FIRSTNAME = "I";
    private static final String VALID_AUTHOR_LASTNAME = "Moria";
    private static final String VALID_RUNDOWN = "This is the test content of article.";
    private static final String VALID_CONTENT = "This is the test content of article.";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ArticleService articleService;
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void getArticles_returnStatusOk() throws Exception {
        mockMvc.perform(get("/articles")).andExpect(status().isOk());
    }

    @Test
    void getArticlesWithParam_returnStatusOk() throws Exception {
        mockMvc.perform(get("/articles")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE))).andExpect(status().isOk());
    }

    @Test
    void getArticlesWithInvalidParam_returnBadRequest() throws Exception {
        mockMvc.perform(get("/articles")
                .contentType(MediaType.TEXT_HTML)
                .param("page", "d")).andExpect(status().isBadRequest());
    }

    @Test
    void getArticles_callBusinessLogic() throws Exception {
        mockMvc.perform(get("/articles")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE))).andExpect(status().isOk());
        verify(articleService, times(1)).getCountOfPages();
        verify(articleService, times(1)).getArticlesByPage(eq(PAGE));
    }

    @Test
    void getArticles_returnArticles() throws Exception {
        List<ArticlesDTO> articles = getOneArticleList();
        when(articleService.getArticlesByPage(PAGE)).thenReturn(articles);
        MvcResult result = mockMvc.perform(get("/articles")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE))).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(VALID_TITLE);
        Assertions.assertThat(actualContent).contains(VALID_DATE.toString());
        Assertions.assertThat(actualContent).contains(VALID_RUNDOWN);
    }

    @Test
    void getArticlePage_returnStatusOk() throws Exception {
        ArticleDTO article = getArticleDTO();
        when(articleService.getArticleById(VALID_ID)).thenReturn(article);
        mockMvc.perform(get("/articles/{id}", VALID_ID))
                .andExpect(status().isOk());
    }

    @Test
    void getArticlePageById_callBusinessLogic() throws Exception {
        ArticleDTO article = getArticleDTO();
        when(articleService.getArticleById(VALID_ID)).thenReturn(article);
        mockMvc.perform(get("/articles/{id}", VALID_ID))
                .andExpect(status().isOk());
        verify(articleService, times(1)).getArticleById(VALID_ID);
    }

    @Test
    void getArticlePageById_returnValidArticle() throws Exception {
        ArticleDTO article = getArticleDTO();
        when(articleService.getArticleById(VALID_ID)).thenReturn(article);
        MvcResult result = mockMvc.perform(get("/articles/{id}", VALID_ID))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(VALID_AUTHOR_LASTNAME);
        Assertions.assertThat(actualContent).contains(VALID_TITLE);
        Assertions.assertThat(actualContent).contains(VALID_CONTENT);
    }

    private List<ArticlesDTO> getOneArticleList() {
        List<ArticlesDTO> articles = new ArrayList<>();
        ArticlesDTO article = getArticlesDTO();
        articles.add(article);
        return articles;
    }

    private ArticlesDTO getArticlesDTO() {
        ArticlesDTO article = new ArticlesDTO();
        article.setId(VALID_ID);
        article.setDate(VALID_DATE);
        article.setTitle(VALID_TITLE);
        article.setRundown(VALID_RUNDOWN);
        article.setAuthorFirstName(VALID_AUTHOR_FIRSTNAME);
        article.setAuthorLastName(VALID_AUTHOR_LASTNAME);
        List<CommentDTO> comments = new ArrayList<>();
        CommentDTO comment = getComment();
        comments.add(comment);
        comments.add(comment);
        return article;
    }

    private ArticleDTO getArticleDTO() {
        ArticleDTO article = new ArticleDTO();
        article.setId(VALID_ID);
        article.setDate(VALID_DATE);
        article.setTitle(VALID_TITLE);
        article.setAuthorFirstName(VALID_AUTHOR_FIRSTNAME);
        article.setAuthorLastName(VALID_AUTHOR_LASTNAME);
        article.setContent(VALID_CONTENT);
        List<CommentDTO> comments = new ArrayList<>();
        CommentDTO comment = getComment();
        comments.add(comment);
        comments.add(comment);
        article.setComments(comments);
        return article;
    }

    private CommentDTO getComment() {
        CommentDTO comment = new CommentDTO();
        comment.setAuthorFirstName("Dmitry");
        comment.setAuthorLastName("Dmitrich");
        comment.setDate(VALID_DATE);
        comment.setContent(VALID_COMMENT_CONTENT);
        return comment;
    }

}