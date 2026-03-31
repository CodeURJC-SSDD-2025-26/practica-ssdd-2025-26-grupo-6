package es.code.urjc.practica2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.service.ReviewService;
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.FilmographyService;


@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private FilmographyService filmographyService;

    @Autowired
    private ReviewService reviewService;


    @GetMapping("/includeReview")
    public String includeReview(Model model) {
        return "includeReview";
    }

    @GetMapping("/filmographies/{filmographyId}/reviews/new")
    public String newReview(@PathVariable Long filmographyId, Model model) {
        model.addAttribute("filmography", filmographyService.findById(filmographyId));
        model.addAttribute("review", new Review());
        return "review-form";
    }

    @PostMapping("/filmographies/{filmographyId}/reviews")
    public String saveReview(@PathVariable Long filmographyId, Review review) {
        Filmography filmography = filmographyService.findById(filmographyId); 
        review.setFilmography(filmography);
        review.setReviewAuthor(accountService.getCurrentUser());
        reviewService.save(review);
        return "redirect:/filmographies/" + filmographyId;
    }

    @GetMapping("/reviews/{reviewId}/edit")
    public String editReview(@PathVariable Long reviewId, Model model) {
        Review review = reviewService.findById(reviewId);
        model.addAttribute("filmography", review.getFilmography());
        model.addAttribute("review", review);
        return "review-form";
    }

    @PostMapping("/reviews/{reviewId}/edit") 
    public String updateReview(@PathVariable Long reviewId, @RequestParam Float reviewStars, @RequestParam String reviewDescription) {
        Review review = reviewService.update(reviewId, reviewStars, reviewDescription);
        return "redirect:/filmographies/" + review.getFilmography().getFilmographyId();
    }

    @PostMapping("/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable Long reviewId) {
        Long filmographyId = reviewService.findById(reviewId)
            .getFilmography().getFilmographyId();

        reviewService.delete(reviewId);

        return "redirect:/filmographies/" + filmographyId;
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