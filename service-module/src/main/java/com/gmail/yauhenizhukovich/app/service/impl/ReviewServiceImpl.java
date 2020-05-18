package com.gmail.yauhenizhukovich.app.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhenizhukovich.app.repository.ReviewRepository;
import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Review;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.repository.model.UserDetails;
import com.gmail.yauhenizhukovich.app.service.ReviewService;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.review.AddReviewDTO;
import com.gmail.yauhenizhukovich.app.service.model.review.ReviewDTO;
import com.gmail.yauhenizhukovich.app.service.model.review.ReviewsDTO;
import com.gmail.yauhenizhukovich.app.service.util.conversion.ReviewConversionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.gmail.yauhenizhukovich.app.service.constant.PageConstant.COUNT_OF_REVIEWS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getStartPositionByPageNumber;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.ReviewConversionUtil.convertDTOToDatabaseObject;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.ReviewConversionUtil.convertDatabaseObjectToReviewDTO;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            UserRepository userRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ReviewDTO addReview(AddReviewDTO reviewDTO) {
        Review review = convertDTOToDatabaseObject(reviewDTO);
        review.setDate(LocalDate.now());
        review.setActive(false);
        String email = getAuthenticationName();
        User user = userRepository.getUserByEmail(email);
        UserDetails userDetails = user.getUserDetails();
        review.setAuthorName(userDetails.getLastName() + " " + userDetails.getFirstName());
        review = reviewRepository.add(review);
        return convertDatabaseObjectToReviewDTO(review);
    }

    @Override
    public ObjectsDTOAndPagesEntity<ReviewsDTO> getReviewsByPage(Integer page) {
        int startPosition = getStartPositionByPageNumber(page, COUNT_OF_REVIEWS_BY_PAGE);
        List<Review> reviews = reviewRepository.getPaginatedObjects(startPosition, COUNT_OF_REVIEWS_BY_PAGE);
        int pages = getPages();
        List<ReviewsDTO> reviewsDTO = getReviewsDTO(reviews);
        return new ObjectsDTOAndPagesEntity<>(pages, reviewsDTO);
    }

    @Override
    public boolean deleteReviewById(Long id) {
        Review review = reviewRepository.getById(id);
        if (review == null) {
            return false;
        }
        return reviewRepository.delete(review);
    }

    @Override
    public List<ReviewsDTO> updateStatusByIds(List<Long> ids) {
        return ids.stream()
                .map(reviewRepository::getById)
                .peek(review -> {
                    boolean actualStatus = review.getActive();
                    review.setActive(!actualStatus);
                })
                .map(ReviewConversionUtil::convertDatabaseObjectToReviewsDTO)
                .collect(Collectors.toList());
    }

    private int getPages() {
        Long countOfReviews = reviewRepository.getCountOfObjects();
        return getCountOfPagesByCountOfObjects(countOfReviews, COUNT_OF_REVIEWS_BY_PAGE);
    }

    private String getAuthenticationName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private List<ReviewsDTO> getReviewsDTO(List<Review> reviews) {
        return reviews.stream()
                .map(ReviewConversionUtil::convertDatabaseObjectToReviewsDTO)
                .collect(Collectors.toList());
    }

}
