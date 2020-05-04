package com.gmail.yauhenizhukovich.app.service.util.conversion;

import com.gmail.yauhenizhukovich.app.repository.model.Review;
import com.gmail.yauhenizhukovich.app.service.model.review.AddReviewDTO;
import com.gmail.yauhenizhukovich.app.service.model.review.ReviewDTO;
import com.gmail.yauhenizhukovich.app.service.model.review.ReviewsDTO;

public class ReviewConversionUtil {

    public static ReviewsDTO convertDatabaseObjectToReviewsDTO(Review review) {
        ReviewsDTO reviewDTO = new ReviewsDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setAuthorName(review.getAuthorName());
        reviewDTO.setReviewText(review.getReviewText());
        reviewDTO.setDate(review.getDate());
        reviewDTO.setActive(review.getActive());
        return reviewDTO;
    }

    public static Review convertDTOToDatabaseObject(AddReviewDTO reviewDTO) {
        Review review = new Review();
        review.setReviewText(reviewDTO.getReviewText());
        return review;
    }

    public static ReviewDTO convertDatabaseObjectToReviewDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setAuthorName(review.getAuthorName());
        reviewDTO.setDate(review.getDate());
        reviewDTO.setReviewText(review.getReviewText());
        return reviewDTO;
    }

}
