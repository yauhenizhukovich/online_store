package com.gmail.yauhenizhukovich.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhenizhukovich.app.repository.ReviewRepository;
import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Review;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.repository.model.UserDetails;
import com.gmail.yauhenizhukovich.app.service.impl.ReviewServiceImpl;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.review.AddReviewDTO;
import com.gmail.yauhenizhukovich.app.service.model.review.ReviewDTO;
import com.gmail.yauhenizhukovich.app.service.model.review.ReviewsDTO;
import com.gmail.yauhenizhukovich.app.service.util.conversion.ReviewConversionUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gmail.yauhenizhukovich.app.service.constant.PageConstant.COUNT_OF_REVIEWS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.COUNT_OF_OBJECTS;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.PAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.START_POSITION;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_AUTHOR_NAME;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_EMAIL;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_ID;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_REVIEW_TEXT;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static com.gmail.yauhenizhukovich.app.service.util.ServiceUnitTestUtil.getUser;
import static com.gmail.yauhenizhukovich.app.service.util.ServiceUnitTestUtil.mockAuthentication;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.ReviewConversionUtil.convertDTOToDatabaseObject;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    private ReviewService reviewService;

    @BeforeEach
    public void setup() {
        reviewService = new ReviewServiceImpl(reviewRepository, userRepository);
    }

    @Test
    public void addReview_callDatabase() {
        mockAuthentication(VALID_EMAIL);
        AddReviewDTO addReviewDTO = new AddReviewDTO();
        addReviewDTO.setReviewText(VALID_REVIEW_TEXT);
        Review addReview = convertDTOToDatabaseObject(addReviewDTO);
        addReview.setDate(LocalDate.now());
        addReview.setActive(false);
        User user = getUser();
        UserDetails userDetails = user.getUserDetails();
        addReview.setAuthorName(userDetails.getFirstName() + " " + userDetails.getLastName());
        when(userRepository.getUserByEmail(VALID_EMAIL)).thenReturn(user);
        when(reviewRepository.add(addReview)).thenReturn(addReview);
        ReviewDTO actualAddedReview = reviewService.addReview(addReviewDTO);
        Assertions.assertThat(actualAddedReview).isNotNull();
        verify(userRepository, times(1)).getUserByEmail(VALID_EMAIL);
        verify(reviewRepository, times(1)).add(addReview);
    }

    @Test
    public void addReview_returnReview() {
        mockAuthentication(VALID_EMAIL);
        AddReviewDTO addReviewDTO = new AddReviewDTO();
        addReviewDTO.setReviewText(VALID_REVIEW_TEXT);
        Review addReview = convertDTOToDatabaseObject(addReviewDTO);
        addReview.setDate(LocalDate.now());
        addReview.setActive(false);
        User user = getUser();
        UserDetails userDetails = user.getUserDetails();
        addReview.setAuthorName(userDetails.getFirstName() + " " + userDetails.getLastName());
        when(userRepository.getUserByEmail(VALID_EMAIL)).thenReturn(user);
        when(reviewRepository.add(addReview)).thenReturn(addReview);
        ReviewDTO actualAddedReview = reviewService.addReview(addReviewDTO);
        Assertions.assertThat(actualAddedReview).isNotNull();
        verify(userRepository, times(1)).getUserByEmail(VALID_EMAIL);
        verify(reviewRepository, times(1)).add(addReview);
        ReviewDTO expectedAddedReview = ReviewConversionUtil.convertDatabaseObjectToReviewDTO(addReview);
        Assertions.assertThat(actualAddedReview.getReviewText()).isEqualTo(expectedAddedReview.getReviewText());
        Assertions.assertThat(actualAddedReview.getDate()).isEqualTo(expectedAddedReview.getDate());
        Assertions.assertThat(actualAddedReview.getAuthorName()).isEqualTo(expectedAddedReview.getAuthorName());
    }

    @Test
    public void getReviewsByPage_callDatabase() {
        List<Review> returnedReviews = getReviews();
        when(reviewRepository.getPaginatedObjects(START_POSITION, COUNT_OF_REVIEWS_BY_PAGE))
                .thenReturn(returnedReviews);
        ObjectsDTOAndPagesEntity<ReviewsDTO> reviewsAndPages = reviewService.getReviewsByPage(PAGE);
        List<ReviewsDTO> actualReviews = reviewsAndPages.getObjects();
        Assertions.assertThat(actualReviews).isNotNull();
        verify(reviewRepository, times(1))
                .getPaginatedObjects(START_POSITION, COUNT_OF_REVIEWS_BY_PAGE);
    }

    @Test
    public void getReviewsByPage_returnReviews() {
        List<Review> returnedReviews = getReviews();
        when(reviewRepository.getPaginatedObjects(START_POSITION, COUNT_OF_REVIEWS_BY_PAGE))
                .thenReturn(returnedReviews);
        ObjectsDTOAndPagesEntity<ReviewsDTO> reviewsAndPages = reviewService.getReviewsByPage(PAGE);
        List<ReviewsDTO> actualReviews = reviewsAndPages.getObjects();
        Assertions.assertThat(actualReviews).isNotNull();
        verify(reviewRepository, times(1))
                .getPaginatedObjects(START_POSITION, COUNT_OF_REVIEWS_BY_PAGE);
        ReviewsDTO actualReview = actualReviews.get(0);
        Review returnedReview = returnedReviews.get(0);
        Assertions.assertThat(actualReview.getAuthorName()).isEqualTo(returnedReview.getAuthorName());
        Assertions.assertThat(actualReview.getReviewText()).isEqualTo(returnedReview.getReviewText());
        Assertions.assertThat(actualReview.getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    public void getReviewsByPage_returnPages() {
        when(reviewRepository.getCountOfObjects())
                .thenReturn(COUNT_OF_OBJECTS);
        ObjectsDTOAndPagesEntity<ReviewsDTO> reviewsAndPages = reviewService.getReviewsByPage(PAGE);
        Assertions.assertThat(reviewsAndPages).isNotNull();
        verify(reviewRepository, times(1))
                .getCountOfObjects();
        Assertions.assertThat(reviewsAndPages.getPages())
                .isEqualTo(getCountOfPagesByCountOfObjects(COUNT_OF_OBJECTS, COUNT_OF_REVIEWS_BY_PAGE));
    }

    @Test
    public void deleteReviewById_callDatabase() {
        Review returnedReview = getReview();
        when(reviewRepository.getById(VALID_ID)).thenReturn(returnedReview);
        when(reviewRepository.delete(returnedReview)).thenReturn(true);
        boolean isDeleted = reviewService.deleteReviewById(VALID_ID);
        verify(reviewRepository, times(1)).delete(returnedReview);
        Assertions.assertThat(isDeleted).isNotNull();
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
    public void updateStatusByIds_callDatabase() {
        List<Long> ids = getIds();
        Review returnedReview = getReview();
        when(reviewRepository.getById(ids.get(0))).thenReturn(returnedReview);
        List<ReviewsDTO> actualUpdatedReviews = reviewService.updateStatusByIds(ids);
        Assertions.assertThat(actualUpdatedReviews).isNotNull();
        verify(reviewRepository, times(ids.size())).getById(any());
    }

    @Test
    public void updateStatusByIds_returnReviewsWithUpdatedStatus() {
        List<Long> ids = getIds();
        Review returnedReview = getReview();
        when(reviewRepository.getById(ids.get(0))).thenReturn(returnedReview);
        List<ReviewsDTO> actualUpdatedReviews = reviewService.updateStatusByIds(ids);
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
