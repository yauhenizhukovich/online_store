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
    public void getListOfPageNumbers_returnListOfPages() {
        Long countOfObjects = 25L;
        when(reviewRepository.getCountOfObjects()).thenReturn(countOfObjects);
        List<Integer> returnedCountOfPages = getReturnedCountOfPages(countOfObjects);
        List<Integer> actualCountOfPages = reviewService.getListOfPageNumbers();
        verify(reviewRepository, times(1)).getCountOfObjects();
        Assertions.assertThat(actualCountOfPages).isNotNull();
        Assertions.assertThat(actualCountOfPages).isEqualTo(returnedCountOfPages);
    }

    @Test
    public void getReviewsByPage_returnReviewsList() {
        int pageNumber = 7;
        int countOfReviewsByPage = PaginationUtil.getCountOfReviewsByPage();
        int startPosition = PaginationUtil.getStartPositionByPageNumber(pageNumber, countOfReviewsByPage);
        List<Review> returnedReviews = new ArrayList<>();
        when(reviewRepository.getObjectsByStartPositionAndMaxResult(startPosition, countOfReviewsByPage))
                .thenReturn(returnedReviews);
        List<ReviewDTO> returnedReviewsDTO = returnedReviews.stream()
                .map(ReviewConversionUtil::convertDatabaseObjectToDTOToGetAll)
                .collect(Collectors.toList());
        List<ReviewDTO> actualReviewsDTO = reviewService.getReviewsByPage(pageNumber);
        verify(reviewRepository, times(1)).getObjectsByStartPositionAndMaxResult(startPosition, countOfReviewsByPage);
        Assertions.assertThat(actualReviewsDTO).isNotNull();
        Assertions.assertThat(actualReviewsDTO).isEqualTo(returnedReviewsDTO);
    }

    @Test
    public void deleteReviewById_returnTrueStatus() {
        Long id = 19L;
        Review returnedReview = new Review();
        when(reviewRepository.getById(id)).thenReturn(returnedReview);
        when(reviewRepository.delete(returnedReview)).thenReturn(true);
        boolean actualStatus = reviewService.deleteReviewById(id);
        verify(reviewRepository, times(1)).getById(id);
        verify(reviewRepository, times(1)).delete(returnedReview);
        Assertions.assertThat(actualStatus).isNotNull();
        Assertions.assertThat(actualStatus).isEqualTo(true);
    }

    @Test
    public void updateReviewActivityByIds_returnTrueStatus() {
        Long[] ids = new Long[1];
        Review returnedReview = new Review();
        returnedReview.setActive(false);
        when(reviewRepository.getById(ids[0])).thenReturn(returnedReview);
        List<Boolean> returnedActivities = Collections.singletonList(new ArrayList<>().add(returnedReview.getActive()));
        List<Boolean> updatedActivities = reviewService.updateActivityByIds(ids);
        verify(reviewRepository, times(1)).getById(ids[0]);
        Assertions.assertThat(updatedActivities).isNotNull();
        Assertions.assertThat(updatedActivities).isEqualTo(returnedActivities);
    }

    private List<Integer> getReturnedCountOfPages(Long countOfObjects) {
        int reviewsByPage = PaginationUtil.getCountOfReviewsByPage();
        return PaginationUtil.getCountOfPages(countOfObjects, reviewsByPage);
    }

}
