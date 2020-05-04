package com.gmail.yauhenizhukovich.app.web.controller.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_CONTENT;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_DATE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_TITLE;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
@WithMockUser(roles = {"CUSTOMER_USER", "SALE_USER"})
public class ArticleControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getArticles_returnArticles() throws Exception {
        mockMvc.perform(get("/articles")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("test content")));
    }

    @Test
    public void getArticle_returnArticle() throws Exception {
        mockMvc.perform(get("/articles/2")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(
                        "test content")))
                .andExpect(content().string(containsString("test title")));
    }

    @Test
    public void deleteArticle_returnRedirectedUrl() throws Exception {
        mockMvc.perform(post("/articles/1/delete")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(redirectedUrl("/articles"));
    }

    @Test
    public void getAddArticlePage() throws Exception {
        mockMvc.perform(get("/articles/add")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("Article_model")));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void addArticle() throws Exception {
        mockMvc.perform(post("/articles")
                .contentType(MediaType.TEXT_HTML)
                .param("title", VALID_TITLE)
                .param("content", VALID_CONTENT)
                .param("date", VALID_DATE.toString()))
                .andExpect(content().string(containsString(VALID_TITLE)))
                .andExpect(content().string(containsString(VALID_CONTENT)));

    }

}
