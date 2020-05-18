package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhenizhukovich.app.service.ArticleService;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticlesDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.CommentDTO;
import com.gmail.yauhenizhukovich.app.web.controller.config.UnitTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.PAGE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.PAGES;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_AUTHOR_FIRSTNAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_AUTHOR_LASTNAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_COMMENT_CONTENT;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_CONTENT;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_DATE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_ID;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_RUNDOWN;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_TITLE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleController.class)
@Import(UnitTestConfig.class)
@WithMockUser(roles = "CUSTOMER_USER")
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ArticleService articleService;

    @Test
    void getArticles_returnStatusOk() throws Exception {
        List<ArticlesDTO> articles = new ArrayList<>();
        ObjectsDTOAndPagesEntity<ArticlesDTO> articlesAndPages = new ObjectsDTOAndPagesEntity<>(PAGES, articles);
        when(articleService.getArticlesByPage(any())).thenReturn(articlesAndPages);
        mockMvc.perform(get("/articles")).andExpect(status().isOk());
    }

    @Test
    void getArticlesWithParam_returnStatusOk() throws Exception {
        List<ArticlesDTO> articles = new ArrayList<>();
        ObjectsDTOAndPagesEntity<ArticlesDTO> articlesAndPages = new ObjectsDTOAndPagesEntity<>(PAGES, articles);
        when(articleService.getArticlesByPage(PAGE)).thenReturn(articlesAndPages);
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
        List<ArticlesDTO> articles = new ArrayList<>();
        ObjectsDTOAndPagesEntity<ArticlesDTO> articlesAndPages = new ObjectsDTOAndPagesEntity<>(PAGES, articles);
        when(articleService.getArticlesByPage(PAGE)).thenReturn(articlesAndPages);
        mockMvc.perform(get("/articles")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE))).andExpect(status().isOk());
        verify(articleService, times(1)).getArticlesByPage(eq(PAGE));
    }

    @Test
    void getArticles_returnArticles() throws Exception {
        ObjectsDTOAndPagesEntity<ArticlesDTO> articles = getOneArticleList();
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

    private ObjectsDTOAndPagesEntity<ArticlesDTO> getOneArticleList() {
        List<ArticlesDTO> articles = new ArrayList<>();
        ArticlesDTO article = getArticlesDTO();
        articles.add(article);
        return new ObjectsDTOAndPagesEntity<>(PAGES, articles);
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