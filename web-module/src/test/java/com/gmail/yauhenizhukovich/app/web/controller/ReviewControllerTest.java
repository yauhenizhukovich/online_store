package com.gmail.yauhenizhukovich.app.web.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhenizhukovich.app.service.ReviewService;
import com.gmail.yauhenizhukovich.app.service.model.review.ReviewDTO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReviewController.class)
@WithMockUser(roles = "ADMINISTRATOR")
class ReviewControllerTest {

    private static final int PAGE = 3;
    private static final String VALID_FULL_NAME = "Sidorov Ivan Petrovich";
    private static final String VALID_REVIEW_TEXT = "This is test review example.";
    private static final LocalDate VALID_DATE = LocalDate.now();
    private static final boolean VALID_ACTIVE = true;
    private static final long VALID_ID = 12L;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReviewService reviewService;
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void getReviews_returnStatusOk() throws Exception {
        mockMvc.perform(get("/reviews")).andExpect(status().isOk());
    }

    @Test
    void getReviewsWithParam_returnStatusOk() throws Exception {
        mockMvc.perform(get("/reviews")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE))).andExpect(status().isOk());
    }

    @Test
    void getReviewsWithInvalidParam_returnBadRequest() throws Exception {
        mockMvc.perform(get("/reviews")
                .contentType(MediaType.TEXT_HTML)
                .param("page", "d")).andExpect(status().isBadRequest());
    }

    @Test
    void getReviews_callBusinessLogic() throws Exception {
        mockMvc.perform(get("/reviews")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE))).andExpect(status().isOk());
        verify(reviewService, times(1)).getCountOfPages();
        verify(reviewService, times(1)).getReviewsByPage(eq(PAGE));
    }

    @Test
    void getReviews_returnReviews() throws Exception {
        List<ReviewDTO> reviews = getOneReviewList();
        when(reviewService.getReviewsByPage(PAGE)).thenReturn(reviews);
        MvcResult result = mockMvc.perform(get("/reviews")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE))).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(VALID_FULL_NAME);
        Assertions.assertThat(actualContent).contains(VALID_REVIEW_TEXT);
        Assertions.assertThat(actualContent).contains(VALID_DATE.toString());
    }

    @Test
    void updateReviews_returnRedirectedUrl() throws Exception {
        mockMvc.perform(post("/reviews")
                .param("ids", "2")
                .param("ids", "4")
        ).andExpect(redirectedUrl("/reviews"));
    }

    @Test
    void updateReviewsWithInvalidParams_returnBadRequest() throws Exception {
        mockMvc.perform(post("/reviews")
                .param("ids", "2")
                .param("ids", "g")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void updateReviews_callBusinessLogic() throws Exception {
        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        ids.add(5L);
        mockMvc.perform(post("/reviews")
                .param("ids", String.valueOf(ids.get(0)))
                .param("ids", String.valueOf(ids.get(1)))
        ).andExpect(redirectedUrl("/reviews"));
        verify(reviewService, times(1)).updateStatusByIds(eq(ids));
    }

    @Test
    void deleteReviewById_returnRedirectedUrl() throws Exception {
        mockMvc.perform(post("/reviews/{id}", "12"))
                .andExpect(redirectedUrl("/reviews"));
    }

    @Test
    void deleteReviewById_callBusinessLogic() throws Exception {
        mockMvc.perform(post("/reviews/{id}", VALID_ID))
                .andExpect(redirectedUrl("/reviews"));
        verify(reviewService, times(1)).deleteReviewById(eq(VALID_ID));
    }

    private List<ReviewDTO> getOneReviewList() {
        List<ReviewDTO> reviews = new ArrayList<>();
        ReviewDTO review = getReview();
        reviews.add(review);
        return reviews;
    }

    private ReviewDTO getReview() {
        ReviewDTO review = new ReviewDTO();
        review.setAuthorName(VALID_FULL_NAME);
        review.setReviewText(VALID_REVIEW_TEXT);
        review.setDate(VALID_DATE);
        review.setActive(VALID_ACTIVE);
        return review;
    }

}