package com.gmail.yauhenizhukovich.app.service.util;

import java.time.LocalDate;

import com.gmail.yauhenizhukovich.app.repository.model.Review;
import com.gmail.yauhenizhukovich.app.service.model.ReviewDTO;

public class ReviewConversionUtil {

    public static ReviewDTO convertDatabaseObjectToDTOToGetAll(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        Long id = review.getId();
        reviewDTO.setId(id);
        String fullName = review.getFullName();
        reviewDTO.setFullName(fullName);
        String reviewText = review.getReviewText();
        reviewDTO.setReviewText(reviewText);
        LocalDate date = review.getDate();
        reviewDTO.setDate(date);
        boolean active = review.getActive();
        reviewDTO.setActive(active);
        return reviewDTO;
    }

}
