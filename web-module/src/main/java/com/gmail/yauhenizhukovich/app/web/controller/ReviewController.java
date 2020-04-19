package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.ReviewService;
import com.gmail.yauhenizhukovich.app.service.model.ReviewDTO;
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
    public String getReviews(@RequestParam(required = false) Integer page, Model model) {
        if (page == null) {
            page = 1;
        }
        List<ReviewDTO> reviews = reviewService.getReviewsByPage(page);
        model.addAttribute("reviews", reviews);
        List<Integer> pages = reviewService.getPages();
        model.addAttribute("pages", pages);
        return "reviews";
    }

    @PostMapping
    public String updateReviews(@RequestParam(required = false) Long[] ids) {
        if (ids != null) {
            reviewService.updateActivityByIds(ids);
        }
        return "redirect:/reviews";
    }

    @PostMapping("/{id}")
    public String deleteReviewById(@PathVariable Long id) {
        reviewService.deleteReviewById(id);
        return "redirect:/reviews";
    }

}
