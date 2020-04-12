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

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {this.reviewRepository = reviewRepository;}

    @Override
    @Transactional
    public List<ReviewDTO> getReviewsByPage(Integer pageNumber) {
        int reviewsByPage = PaginationUtil.getCountOfReviewsByPage();
        int startPosition = PaginationUtil.getStartPositionByPageNumber(pageNumber, reviewsByPage);
        List<Review> reviews = reviewRepository.getObjectsByStartPositionAndMaxResult(startPosition, reviewsByPage);
        return reviews.stream()
                .map(ReviewConversionUtil::convertDatabaseObjectToDTOToGetAll)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Integer> getListOfPageNumbers() {
        Long countOfReviews = reviewRepository.getCountOfObjects();
        int reviewsByPage = PaginationUtil.getCountOfReviewsByPage();
        return PaginationUtil.getCountOfPages(countOfReviews, reviewsByPage);
    }

    @Override
    @Transactional
    public boolean deleteReviewById(Long id) {
        Review review = reviewRepository.getById(id);
        return reviewRepository.delete(review);
    }

    @Override
    @Transactional
    public List<Boolean> updateActivityByIds(Long[] ids) {
        return Arrays.stream(ids)
                .map(id -> reviewRepository.getById(id))
                .peek(review -> {
                    boolean actualActive = review.getActive();
                    review.setActive(!actualActive);
                }).map(Review::getActive).collect(Collectors.toList());
    }

}
