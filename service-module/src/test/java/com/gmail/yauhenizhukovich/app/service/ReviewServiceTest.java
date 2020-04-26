package com.gmail.yauhenizhukovich.app.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.gmail.yauhenizhukovich.app.repository.ReviewRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Review;
import com.gmail.yauhenizhukovich.app.service.impl.ReviewServiceImpl;
import com.gmail.yauhenizhukovich.app.service.model.ReviewDTO;
import com.gmail.yauhenizhukovich.app.service.util.PaginationUtil;
import com.gmail.yauhenizhukovich.app.service.util.ReviewConversionUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    private ReviewService reviewService;

    @BeforeEach
    public void setup() {
        reviewService = new ReviewServiceImpl(reviewRepository);
    }

    @Test
    public void updateReviewActivityByIds_returnTrueStatus() {
        Long[] ids = new Long[1];
        Review returnedReview = new Review();
        returnedReview.setActive(false);
        when(reviewRepository.getById(ids[0])).thenReturn(returnedReview);
        List<Boolean> returnedActivities = Collections.singletonList(new ArrayList<>().add(returnedReview.getActive()));
        List<ReviewDTO> updatedActivities = reviewService.updateActivityByIds(ids);
        verify(reviewRepository, times(1)).getById(ids[0]);
        Assertions.assertThat(updatedActivities).isNotNull();
    }

}
