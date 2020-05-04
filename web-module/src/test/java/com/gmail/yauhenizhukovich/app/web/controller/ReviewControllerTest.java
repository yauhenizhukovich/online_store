package com.gmail.yauhenizhukovich.app.web.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhenizhukovich.app.service.ReviewService;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.review.ReviewsDTO;
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
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_ACTIVE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_AUTHOR_FIRSTNAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_DATE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_FULL_NAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_ID;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_LAST_NAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_REVIEW_TEXT;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReviewController.class)
@Import(UnitTestConfig.class)
@WithMockUser(roles = "ADMINISTRATOR")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReviewService reviewService;

    @Test
    void getReviews_returnStatusOk() throws Exception {
        ObjectsDTOAndPagesEntity<ReviewsDTO> reviewsAndPages = getReviews();
        when(reviewService.getReviewsByPage(eq(PAGE))).thenReturn(reviewsAndPages);
        mockMvc.perform(get("/reviews")
                .param("page", String.valueOf(PAGE)))
                .andExpect(status().isOk());
    }

    @Test
    void getReviewsWithInvalidParam_returnBadRequest() throws Exception {
        mockMvc.perform(get("/reviews")
                .contentType(MediaType.TEXT_HTML)
                .param("page", "d")).andExpect(status().isBadRequest());
    }

    @Test
    void getReviews_callBusinessLogic() throws Exception {
        ObjectsDTOAndPagesEntity<ReviewsDTO> reviewsAndPages = getReviews();
        when(reviewService.getReviewsByPage(eq(PAGE))).thenReturn(reviewsAndPages);
        mockMvc.perform(get("/reviews")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE))).andExpect(status().isOk());
        verify(reviewService, times(1)).getReviewsByPage(eq(PAGE));
    }

    @Test
    void getReviews_returnReviews() throws Exception {
        ObjectsDTOAndPagesEntity<ReviewsDTO> reviewsAndPages = getOneReviewList();
        when(reviewService.getReviewsByPage(PAGE)).thenReturn(reviewsAndPages);
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

    private ObjectsDTOAndPagesEntity<ReviewsDTO> getOneReviewList() {
        List<ReviewsDTO> reviews = new ArrayList<>();
        ReviewsDTO review = getReview();
        reviews.add(review);
        return new ObjectsDTOAndPagesEntity<>(PAGES, reviews);
    }

    private ReviewsDTO getReview() {
        ReviewsDTO review = new ReviewsDTO();
        review.setAuthorName(VALID_FULL_NAME);
        review.setReviewText(VALID_REVIEW_TEXT);
        review.setDate(VALID_DATE);
        review.setActive(VALID_ACTIVE);
        return review;
    }

    private ObjectsDTOAndPagesEntity<ReviewsDTO> getReviews() {
        List<ReviewsDTO> reviews = new ArrayList<>();
        ReviewsDTO review = new ReviewsDTO();
        review.setReviewText(VALID_REVIEW_TEXT);
        review.setAuthorName(VALID_AUTHOR_FIRSTNAME + " " + VALID_LAST_NAME);
        review.setDate(LocalDate.now());
        reviews.add(review);
        return new ObjectsDTOAndPagesEntity<>(PAGES, reviews);
    }

}