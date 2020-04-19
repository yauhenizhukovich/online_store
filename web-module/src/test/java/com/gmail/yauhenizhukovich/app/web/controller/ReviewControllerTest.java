package com.gmail.yauhenizhukovich.app.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauhenizhukovich.app.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReviewController.class)
@WithMockUser(roles = "ADMINISTRATOR")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReviewService reviewService;
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void getAllReviews_returnRedirectedUrl() throws Exception {
        mockMvc.perform(get("/reviews")).
                andExpect(redirectedUrl("/reviews/page/1"));
    }

    @Test
    void getReviewsByPage_returnStatusOk() throws Exception {
        mockMvc.perform(get("/reviews/page/21")).
                andExpect(status().isOk());
    }

    @Test
    void getReviewsByPage_callBusinessLogic() throws Exception {
        mockMvc.perform(get("/reviews/page/24")).
                andExpect(status().isOk());
        verify(reviewService, times(1)).getReviewsByPage(eq(24));
    }

    @Test
    void deleteReviewById_returnRedirectedUrl() throws Exception {
        mockMvc.perform(
                get("/reviews/20/delete").
                        param("pageNumber", "2")
        ).andExpect(redirectedUrl("/reviews/page/2"));
    }

    @Test
    void deleteReviewById_callBusinessLogic() throws Exception {
        mockMvc.perform(
                get("/reviews/21/delete").
                        param("pageNumber", "2")
        ).andExpect(redirectedUrl("/reviews/page/2"));
        verify(reviewService, times(1)).deleteReviewById(eq(21L));
    }

    @Test
    void updateReviewsActivity_returnRedirectedUrl() throws Exception {
        mockMvc.perform(
                post("/reviews/update/active")
                        .param("ids", "1")
                        .param("ids", "4")
                        .param("pageNumber", "2"))
                .andExpect(redirectedUrl("/reviews/page/2"));
    }

    @Test
    void updateReviewsActivity_callBusinessLogic() throws Exception {
        mockMvc.perform(
                post("/reviews/update/active")
                        .param("ids", "1")
                        .param("ids", "4")
                        .param("pageNumber", "2"))
                .andExpect(redirectedUrl("/reviews/page/2"));
        verify(reviewService, times(1)).updateActivityByIds(eq(new Long[] {1L, 4L}));
    }

}