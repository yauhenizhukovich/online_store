package com.gmail.yauhenizhukovich.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhenizhukovich.app.repository.ReviewRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Review;
import com.gmail.yauhenizhukovich.app.service.impl.ReviewServiceImpl;
import com.gmail.yauhenizhukovich.app.service.model.review.ReviewDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.COUNT_OF_ITEMS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.COUNT_OF_REVIEWS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getStartPositionByPageNumber;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    private static final long COUNT_OF_OBJECTS = 24L;
    private static final int PAGE = 2;
    private static final int START_POSITION = getStartPositionByPageNumber(PAGE, COUNT_OF_ITEMS_BY_PAGE);
    private static final String VALID_AUTHOR_NAME = "Ivan Ivanov";
    private static final String VALID_REVIEW_TEXT = "That's good!";
    private static final Long VALID_ID = 9L;

    @Mock
    private ReviewRepository reviewRepository;
    private ReviewService reviewService;

    @BeforeEach
    public void setup() {
        reviewService = new ReviewServiceImpl(reviewRepository);
    }

    @Test
    public void getReviewsByPage_returnReviews() {
        List<Review> returnedReviews = getReviews();
        when(reviewRepository.getObjectsByStartPositionAndMaxResult(START_POSITION, COUNT_OF_REVIEWS_BY_PAGE))
                .thenReturn(returnedReviews);
        List<ReviewDTO> actualReviews = reviewService.getReviewsByPage(PAGE);
        Assertions.assertThat(actualReviews).isNotNull();
        verify(reviewRepository, times(1))
                .getObjectsByStartPositionAndMaxResult(START_POSITION, COUNT_OF_REVIEWS_BY_PAGE);
        ReviewDTO actualReview = actualReviews.get(0);
        Review returnedReview = returnedReviews.get(0);
        Assertions.assertThat(actualReview.getAuthorName()).isEqualTo(returnedReview.getAuthorName());
        Assertions.assertThat(actualReview.getReviewText()).isEqualTo(returnedReview.getReviewText());
        Assertions.assertThat(actualReview.getDate()).isNotNull();
    }

    @Test
    public void getCountOfPages_returnPages() {
        when(reviewRepository.getCountOfObjects()).thenReturn(COUNT_OF_OBJECTS);
        int pages = reviewService.getCountOfPages();
        verify(reviewRepository, times(1)).getCountOfObjects();
        Assertions.assertThat(pages).isEqualTo(getCountOfPagesByCountOfObjects(COUNT_OF_OBJECTS, COUNT_OF_ITEMS_BY_PAGE));
    }

    @Test
    public void deleteReviewById_returnTrue() {
        Review returnedReview = getReview();
        when(reviewRepository.getById(VALID_ID)).thenReturn(returnedReview);
        when(reviewRepository.delete(returnedReview)).thenReturn(true);
        boolean isDeleted = reviewService.deleteReviewById(VALID_ID);
        Assertions.assertThat(isDeleted).isTrue();
    }

    @Test
    public void deleteReviewByNonexistentId_returnFalse() {
        when(reviewRepository.getById(VALID_ID)).thenReturn(null);
        boolean isDeleted = reviewService.deleteReviewById(VALID_ID);
        Assertions.assertThat(isDeleted).isFalse();
    }

    @Test
    public void updateStatusByIds_returnReviewsWithUpdatedStatus() {
        List<Long> ids = getIds();
        Review returnedReview = getReview();
        when(reviewRepository.getById(ids.get(0))).thenReturn(returnedReview);
        List<ReviewDTO> actualUpdatedReviews = reviewService.updateStatusByIds(ids);
        Assertions.assertThat(actualUpdatedReviews).isNotNull();
        verify(reviewRepository, times(ids.size())).getById(any());
        boolean actualReviewStatus = actualUpdatedReviews.get(0).getActive();
        Assertions.assertThat(actualReviewStatus).isEqualTo(returnedReview.getActive());
    }

    private List<Long> getIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(3L);
        return ids;
    }

    private Review getReview() {
        Review review = new Review();
        review.setAuthorName(VALID_AUTHOR_NAME);
        review.setReviewText(VALID_REVIEW_TEXT);
        review.setDate(LocalDate.now());
        review.setActive(true);
        return review;
    }

    private List<Review> getReviews() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(getReview());
        return reviews;
    }

}
