package com.gmail.yauhenizhukovich.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhenizhukovich.app.repository.ReviewRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Review;
import com.gmail.yauhenizhukovich.app.service.ReviewService;
import com.gmail.yauhenizhukovich.app.service.model.review.ReviewDTO;
import com.gmail.yauhenizhukovich.app.service.util.ReviewConversionUtil;
import org.springframework.stereotype.Service;

import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.COUNT_OF_REVIEWS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getStartPositionByPageNumber;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {this.reviewRepository = reviewRepository;}

    @Override
    public List<ReviewDTO> getReviewsByPage(Integer page) {
        int startPosition = getStartPositionByPageNumber(page, COUNT_OF_REVIEWS_BY_PAGE);
        List<Review> reviews = reviewRepository.getObjectsByStartPositionAndMaxResult(startPosition, COUNT_OF_REVIEWS_BY_PAGE);
        return reviews.stream()
                .map(ReviewConversionUtil::convertDatabaseObjectToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int getCountOfPages() {
        Long countOfReviews = reviewRepository.getCountOfObjects();
        return getCountOfPagesByCountOfObjects(countOfReviews, COUNT_OF_REVIEWS_BY_PAGE);
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
    public List<ReviewDTO> updateStatusByIds(List<Long> ids) {
        return ids.stream()
                .map(reviewRepository::getById)
                .peek(review -> {
                    boolean actualStatus = review.getActive();
                    review.setActive(!actualStatus);
                })
                .map(ReviewConversionUtil::convertDatabaseObjectToDTO)
                .collect(Collectors.toList());
    }

}
