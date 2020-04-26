package com.gmail.yauhenizhukovich.app.service;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.model.ReviewDTO;

public interface ReviewService {

    List<ReviewDTO> getReviewsByPage(Integer pageNumber);

    List<Integer> getPages();

    void deleteReviewById(Long id);

    List<ReviewDTO> updateActivityByIds(Long[] ids);

}
