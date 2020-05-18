package com.gmail.yauhenizhukovich.app.service;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.review.AddReviewDTO;
import com.gmail.yauhenizhukovich.app.service.model.review.ReviewDTO;
import com.gmail.yauhenizhukovich.app.service.model.review.ReviewsDTO;

public interface ReviewService {

    ObjectsDTOAndPagesEntity<ReviewsDTO> getReviewsByPage(Integer page);

    boolean deleteReviewById(Long id);

    List<ReviewsDTO> updateStatusByIds(List<Long> ids);

    ReviewDTO addReview(AddReviewDTO reviewText);

}
