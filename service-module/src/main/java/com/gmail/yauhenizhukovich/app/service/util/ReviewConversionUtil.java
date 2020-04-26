package com.gmail.yauhenizhukovich.app.service.util;

import com.gmail.yauhenizhukovich.app.repository.model.Review;
import com.gmail.yauhenizhukovich.app.service.model.ReviewDTO;

public class ReviewConversionUtil {

    public static ReviewDTO convertDatabaseObjectToDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setFullName(review.getFullName());
        reviewDTO.setReviewText(review.getReviewText());
        reviewDTO.setDate(review.getDate());
        reviewDTO.setActive(review.getActive());
        return reviewDTO;
    }

}
