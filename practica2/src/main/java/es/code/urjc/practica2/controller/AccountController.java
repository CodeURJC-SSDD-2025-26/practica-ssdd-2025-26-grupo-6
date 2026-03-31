package es.code.urjc.practica2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.service.ReviewService;
import es.code.urjc.practica2.service.FilmographyService;


@Controller
public class AccountController {
    @GetMapping("/includeReview")
    public String includeReview(Model model) {
        return "includeReview";
    }

    @GetMapping("/filmographies/{filmographyId}/reviews/new")
    public String newReview(@PathVariable Long filmographyId, Model model) {
        model.addAttribute("filmography", FilmographyService.findById(filmographyId));
        model.addAttribute("review", new Review());
        return "review-form";
    }

    @GetMapping("/reviews/{reviewId}/edit")
    public String editReview(@PathVariable Long reviewId, Model model) {
        Review review = ReviewService.findById(reviewId);
        model.addAttribute("filmography", review.getFilmography());
        model.addAttribute("review", review);
        return "review-form";
    }

    @GetMapping("/myLists")
    public String myLists(Model model) {
        return "myLists";
    }

    @GetMapping("/myReviews")
    public String myReviews(Model model) {
        return "myReviews";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        return "profile";
    }
}