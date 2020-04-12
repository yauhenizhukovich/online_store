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

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {this.reviewService = reviewService;}

    @GetMapping
    public String getAllReviews() {
        return "redirect:/reviews/page/1";
    }

    @GetMapping("/page/{pageNumber}")
    public String getReviewsByPage(@PathVariable Integer pageNumber, Model model) {
        List<ReviewDTO> reviews = reviewService.getReviewsByPage(pageNumber);
        model.addAttribute("reviews", reviews);
        List<Integer> pages = reviewService.getListOfPageNumbers();
        model.addAttribute("pages", pages);
        model.addAttribute("pageNumber", pageNumber);
        return "reviews";
    }

    @GetMapping("/{id}/delete")
    public String deleteReviewById(@PathVariable Long id, @RequestParam String pageNumber) {
        reviewService.deleteReviewById(id);
        return "redirect:/reviews/page/" + pageNumber;
    }

    @PostMapping("/update/active")
    public String updateReviewsActivity(@RequestParam Long[] ids, @RequestParam String pageNumber) {
        reviewService.updateActivityByIds(ids);
        return "redirect:/reviews/page/" + pageNumber;
    }

}
