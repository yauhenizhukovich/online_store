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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
@WithMockUser(roles = "ADMINISTRATOR")
public class ReviewControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(statements = "INSERT INTO review (author_name,review_text,active,date) VALUES ('ivan ivanov','review text test',1,'2020-04-01');")
    public void getReviews_returnReviews() throws Exception {
        mockMvc.perform(get("/reviews")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("ivan ivanov")))
                .andExpect(content().string(containsString("review text test")))
                .andExpect(content().string(containsString("SHOWED")));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getAddReviewPage() throws Exception {
        mockMvc.perform(get("/reviews/add")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("Review_model")));
    }

}
