package com.gmail.yauhenizhukovich.app.service.model.review;

import java.util.Objects;
import javax.validation.constraints.NotEmpty;

import static com.gmail.yauhenizhukovich.app.service.constant.validation.ReviewValidationMessages.NOT_EMPTY_REVIEW_TEXT_MESSAGE;

public class AddReviewDTO {

    @NotEmpty(message = NOT_EMPTY_REVIEW_TEXT_MESSAGE)
    private String reviewText;

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddReviewDTO that = (AddReviewDTO) o;
        return Objects.equals(reviewText, that.reviewText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewText);
    }

}
