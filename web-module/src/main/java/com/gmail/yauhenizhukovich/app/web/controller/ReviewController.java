package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.List;
import javax.validation.Valid;

import com.gmail.yauhenizhukovich.app.service.ReviewService;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.review.AddReviewDTO;
import com.gmail.yauhenizhukovich.app.service.model.review.ReviewDTO;
import com.gmail.yauhenizhukovich.app.service.model.review.ReviewsDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        ObjectsDTOAndPagesEntity<ReviewsDTO> reviews = reviewService.getReviewsByPage(page);
        model.addAttribute("reviews", reviews.getObjects());
        model.addAttribute("pages", reviews.getPages());
        return "reviews";
    }

    @GetMapping("/add")
    public String getAddReviewPage(
            Model model
    ) {
        AddReviewDTO review = new AddReviewDTO();
        model.addAttribute("review", review);
        return "add_review";
    }

    @PostMapping("/add")
    public String addReview(
            @ModelAttribute(name = "review") @Valid AddReviewDTO addReview,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("review", addReview);
            return "add_review";
        }
        ReviewDTO review = reviewService.addReview(addReview);
        model.addAttribute("review", review);
        return "review";
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
