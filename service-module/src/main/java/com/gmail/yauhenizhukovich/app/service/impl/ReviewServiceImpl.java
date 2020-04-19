package com.gmail.yauhenizhukovich.app.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhenizhukovich.app.repository.ReviewRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Review;
import com.gmail.yauhenizhukovich.app.service.ReviewService;
import com.gmail.yauhenizhukovich.app.service.model.ReviewDTO;
import com.gmail.yauhenizhukovich.app.service.util.PaginationUtil;
import com.gmail.yauhenizhukovich.app.service.util.ReviewConversionUtil;
import org.springframework.stereotype.Service;

import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.COUNT_OF_REVIEWS_BY_PAGE;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {this.reviewRepository = reviewRepository;}

    @Override
    @Transactional
    public List<ReviewDTO> getReviewsByPage(Integer page) {
        int startPosition = PaginationUtil.getStartPositionByPageNumber(page, COUNT_OF_REVIEWS_BY_PAGE);
        List<Review> reviews = reviewRepository.getObjectsByStartPositionAndMaxResult(startPosition, COUNT_OF_REVIEWS_BY_PAGE);
        return reviews.stream()
                .map(ReviewConversionUtil::convertDatabaseObjectToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Integer> getPages() {
        Long countOfReviews = reviewRepository.getCountOfObjects();
        return PaginationUtil.getCountOfPages(countOfReviews, COUNT_OF_REVIEWS_BY_PAGE);
    }

    @Override
    @Transactional
    public void deleteReviewById(Long id) {
        Review review = reviewRepository.getById(id);
        reviewRepository.delete(review);
    }

    @Override
    @Transactional
    public List<ReviewDTO> updateActivityByIds(Long[] ids) {
        return Arrays.stream(ids)
                .map(reviewRepository::getById)
                .peek(review -> {
                    boolean actualActive = review.getActive();
                    review.setActive(!actualActive);
                })
                .map(ReviewConversionUtil::convertDatabaseObjectToDTO).collect(Collectors.toList());
    }

}
