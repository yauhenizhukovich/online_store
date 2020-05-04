package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.ReviewService;
import com.gmail.yauhenizhukovich.app.service.model.review.ReviewDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {this.reviewService = reviewService;}

    @GetMapping
    public String getReviews(
            @RequestParam(defaultValue = "1") Integer page,
            Model model
    ) {
        List<ReviewDTO> reviews = reviewService.getReviewsByPage(page);
        model.addAttribute("reviews", reviews);
        int countOfPages = reviewService.getCountOfPages();
        model.addAttribute("pages", countOfPages);
        return "reviews";
    }

    @PostMapping
    public String updateReviewsStatus(
            @RequestParam(defaultValue = "") List<Long> ids
    ) {
        reviewService.updateStatusByIds(ids);
        return "redirect:/reviews";
    }

    @PostMapping("/{id}")
    public String deleteReview(
            @PathVariable Long id
    ) {
        reviewService.deleteReviewById(id);
        return "redirect:/reviews";
    }

}
