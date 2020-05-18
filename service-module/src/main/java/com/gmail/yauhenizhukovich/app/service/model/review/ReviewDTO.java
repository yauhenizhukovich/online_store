package com.gmail.yauhenizhukovich.app.service.model.review;

import java.time.LocalDate;

public class ReviewDTO {

    private String authorName;
    private LocalDate date;
    private String reviewText;

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReviewText() {
        return reviewText;
    }

}
