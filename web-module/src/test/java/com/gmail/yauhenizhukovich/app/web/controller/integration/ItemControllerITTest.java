package com.gmail.yauhenizhukovich.app.web.controller.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

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
public class ItemControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getItem_returnItem() throws Exception {
        mockMvc.perform(get("/items/1")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(
                        "Watermelon")))
                .andExpect(content().string(containsString("test item description")));
    }

    @Test
    @Sql("/data.sql")
    public void getItems_returnItems() throws Exception {
        mockMvc.perform(get("/items")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("Watermelon")))
                .andExpect(content().string(containsString("13.30")));
    }

    @Test
    public void deleteItem_returnRedirectedUrl() throws Exception {
        mockMvc.perform(post("/items/1")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(redirectedUrl("/items"));
    }

}
