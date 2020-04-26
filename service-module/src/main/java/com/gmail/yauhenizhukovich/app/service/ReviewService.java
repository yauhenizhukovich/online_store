package com.gmail.yauhenizhukovich.app.service;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.model.review.ReviewDTO;

public interface ReviewService {

    List<ReviewDTO> getReviewsByPage(Integer page);

    int getCountOfPages();

    boolean deleteReviewById(Long id);

    List<ReviewDTO> updateStatusByIds(List<Long> ids);

}
