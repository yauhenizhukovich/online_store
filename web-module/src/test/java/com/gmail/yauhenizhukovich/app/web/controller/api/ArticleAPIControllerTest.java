package com.gmail.yauhenizhukovich.app.web.controller.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauhenizhukovich.app.service.ArticleService;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.model.article.AddArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticlesDTO;
import com.gmail.yauhenizhukovich.app.web.controller.config.UnitTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.yauhenizhukovich.app.service.constant.validation.ArticleValidationMessages.NOT_EMPTY_CONTENT_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.ArticleValidationMessages.TITLE_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.web.constant.MessageConstant.DELETE_ARTICLE_FAIL_MESSAGE;
import static com.gmail.yauhenizhukovich.app.web.constant.MessageConstant.DELETE_ARTICLE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.web.constant.MessageConstant.NONEXISTENT_ARTICLE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_AUTHOR_FIRSTNAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_AUTHOR_LASTNAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_CONTENT;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_DATE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_ID;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_RUNDOWN;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_TITLE;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleAPIController.class)
@Import(UnitTestConfig.class)
@WithMockUser(roles = "SECURE_API_USER")
class ArticleAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ArticleService articleService;

    @Test
    void getArticles_returnStatusOk() throws Exception {
        mockMvc.perform(get("/api/articles")).andExpect(status().isOk());
    }

    @Test
    void getArticles_callBusinessLogic() throws Exception {
        mockMvc.perform(get("/api/articles")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        verify(articleService, times(1)).getAllArticles();
    }

    @Test
    void getArticles_returnArticles() throws Exception {
        List<ArticlesDTO> articlesAndPages = getArticles();
        when(articleService.getAllArticles()).thenReturn(articlesAndPages);
        MvcResult result = mockMvc.perform(get("/api/articles")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        String expectedContent = objectMapper.writeValueAsString(articlesAndPages);
        Assertions.assertThat(actualContent).isEqualTo(expectedContent);
    }

    @Test
    void getArticleById_returnStatusOk() throws Exception {
        ArticleDTO article = getArticle();
        when(articleService.getArticleById(VALID_ID)).thenReturn(article);
        mockMvc.perform(get("/api/articles/{id}", VALID_ID))
                .andExpect(status().isOk());
    }

    @Test
    void getArticleByNonexistentId_returnErrorMessage() throws Exception {
        when(articleService.getArticleById(VALID_ID)).thenReturn(null);
        MvcResult result = mockMvc.perform(get("/api/articles/{id}", VALID_ID))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).isEqualTo(NONEXISTENT_ARTICLE_MESSAGE);
    }

    @Test
    void getArticleById_callBusinessLogic() throws Exception {
        ArticleDTO article = getArticle();
        when(articleService.getArticleById(VALID_ID)).thenReturn(article);
        mockMvc.perform(get("/api/articles/{id}", VALID_ID))
                .andExpect(status().isOk());
        verify(articleService, times(1)).getArticleById(VALID_ID);
    }

    @Test
    void getArticleById_returnValidArticle() throws Exception {
        ArticleDTO article = getArticle();
        when(articleService.getArticleById(VALID_ID)).thenReturn(article);
        MvcResult result = mockMvc.perform(get("/api/articles/{id}", VALID_ID))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        String expectedContent = objectMapper.writeValueAsString(article);
        Assertions.assertThat(actualContent).isEqualTo(expectedContent);
    }

    @Test
    void addArticle_returnStatusCreated() throws Exception {
        AddArticleDTO addArticle = getAddArticle();
        String content = objectMapper.writeValueAsString(addArticle);
        mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isCreated());
    }

    @Test
    void addArticle_callBusinessLogic() throws Exception, AnonymousUserException {
        AddArticleDTO addArticle = getAddArticle();
        String content = objectMapper.writeValueAsString(addArticle);
        mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isCreated());
        ArgumentCaptor<AddArticleDTO> articleCaptor = ArgumentCaptor.forClass(AddArticleDTO.class);
        verify(articleService, times(1)).addArticle(articleCaptor.capture());
    }

    @Test
    void addArticle_returnArticleWithValidFields() throws Exception, AnonymousUserException {
        AddArticleDTO addArticle = getAddArticle();
        String content = objectMapper.writeValueAsString(addArticle);
        ArticleDTO returnedArticle = getArticle();
        when(articleService.addArticle(addArticle)).thenReturn(returnedArticle);
        mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isCreated());
        ArgumentCaptor<AddArticleDTO> articleCaptor = ArgumentCaptor.forClass(AddArticleDTO.class);
        verify(articleService, times(1)).addArticle(articleCaptor.capture());
        Assertions.assertThat(articleCaptor.getValue().getContent()).isEqualTo(addArticle.getContent());
        Assertions.assertThat(articleCaptor.getValue().getTitle()).isEqualTo(addArticle.getTitle());
    }

    @Test
    void addArticle_returnArticle() throws Exception, AnonymousUserException {
        AddArticleDTO addArticle = getAddArticle();
        String content = objectMapper.writeValueAsString(addArticle);
        ArticleDTO returnedArticle = getArticle();
        when(articleService.addArticle(eq(addArticle))).thenReturn(returnedArticle);
        MvcResult result = mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andReturn();
        String expectedContent = objectMapper.writeValueAsString(returnedArticle);
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(expectedContent).isEqualTo(actualContent);
    }

    @Test
    void addInvalidArticle_returnErrors() throws Exception, AnonymousUserException {
        AddArticleDTO addArticle = new AddArticleDTO();
        addArticle.setTitle("###");
        addArticle.setContent("");
        String content = objectMapper.writeValueAsString(addArticle);
        ArticleDTO returnedArticle = getArticle();
        when(articleService.addArticle(eq(addArticle))).thenReturn(returnedArticle);
        MvcResult result = mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(TITLE_PATTERN_MESSAGE);
        Assertions.assertThat(actualContent).contains(NOT_EMPTY_CONTENT_MESSAGE);
    }

    @Test
    void deleteArticleById_returnStatusOk() throws Exception {
        mockMvc.perform(delete("/api/articles/{id}", VALID_ID)).andExpect(status().isOk());
    }

    @Test
    void deleteArticleById_callBusinessLogic() throws Exception {
        mockMvc.perform(delete("/api/articles/{id}", VALID_ID)).andExpect(status().isOk());
        verify(articleService, times(1)).deleteArticleById(eq(VALID_ID));
    }

    @Test
    void deleteArticleById_returnDeleteMessage() throws Exception {
        when(articleService.deleteArticleById(VALID_ID)).thenReturn(true);
        MvcResult result = mockMvc.perform(delete("/api/articles/{id}", VALID_ID)).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        verify(articleService, times(1)).deleteArticleById(eq(VALID_ID));
        Assertions.assertThat(actualContent).isEqualTo(DELETE_ARTICLE_MESSAGE);
    }

    @Test
    void deleteArticleByNonexistentId_returnDeleteFailMessage() throws Exception {
        when(articleService.deleteArticleById(VALID_ID)).thenReturn(false);
        MvcResult result = mockMvc.perform(delete("/api/articles/{id}", VALID_ID)).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        verify(articleService, times(1)).deleteArticleById(eq(VALID_ID));
        Assertions.assertThat(actualContent).isEqualTo(DELETE_ARTICLE_FAIL_MESSAGE);
    }

    private AddArticleDTO getAddArticle() {
        AddArticleDTO article = new AddArticleDTO();
        article.setDate(LocalDate.now());
        article.setTitle(VALID_TITLE);
        article.setContent(VALID_CONTENT);
        return article;
    }

    private List<ArticlesDTO> getArticles() {
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
        return article;
    }

    private ArticleDTO getArticle() {
        ArticleDTO article = new ArticleDTO();
        article.setId(VALID_ID);
        article.setDate(VALID_DATE);
        article.setTitle(VALID_TITLE);
        article.setAuthorFirstName(VALID_AUTHOR_FIRSTNAME);
        article.setAuthorLastName(VALID_AUTHOR_LASTNAME);
        article.setContent(VALID_CONTENT);
        return article;
    }

}