package es.code.urjc.practica2.controller;

import es.code.urjc.practica2.service.ListsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import es.code.urjc.practica2.service.FilmographyService;

@Controller
public class AccountController {
    @Autowired
    private ListsService listsService;

    @Autowired
    private FilmographyService filmographyService;

    @Autowired
    private ReviewService reviewService;

    AccountController(ListsService listsService) {
        this.listsService = listsService;
    }

    @GetMapping("/filmographies/{filmographyId}/reviews/new")
    public String newReview(@PathVariable Long filmographyId, Model model) {
        Review review = new Review();
        model.addAttribute("filmography", filmographyService.findById(filmographyId));

        review.setReviewStars(0f);
        review.setReviewDescription("");

        model.addAttribute("review", review);

        return "reviewForm";
    }

    @PostMapping("/filmographies/{filmographyId}/reviews/new")
    public String saveReview(@PathVariable Long filmographyId, Review review, HttpSession session) {
        Filmography filmography = filmographyService.findById(filmographyId);
        Account currentUser = (Account) session.getAttribute("user");

        review.setFilmography(filmography);
        review.setReviewAuthor(currentUser);
        reviewService.save(review);

        // Reload from DB so the review list is complete before recalculating
        Filmography updatedFilmography = filmographyService.findByIdWithReviews(filmographyId);
        updatedFilmography.updateAverageStars();
        filmographyService.save(updatedFilmography);

        return "redirect:/filmographies/" + filmographyId;
    }

    @GetMapping("/reviews/{reviewId}/edit")
    public String editReview(@PathVariable Long reviewId, Model model) {
        Review review = reviewService.findById(reviewId);

        model.addAttribute("filmography", review.getFilmography());
        model.addAttribute("review", review);

        return "reviewForm";
    }

    @PostMapping("/reviews/{reviewId}/edit")
    public String updateReview(@PathVariable Long reviewId, @RequestParam Float reviewStars, @RequestParam String reviewDescription) {
        Review review = reviewService.update(reviewId, reviewStars, reviewDescription);
        Long filmographyId = review.getFilmography().getFilmographyId();

        // Reload from DB to ensure the review list is complete
        Filmography updatedFilmography = filmographyService.findByIdWithReviews(filmographyId);
        updatedFilmography.updateAverageStars();
        filmographyService.save(updatedFilmography);

        return "redirect:/myReviews";
    }

    @PostMapping("/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable Long reviewId) {
        Review review = reviewService.findById(reviewId);
        Long filmographyId = review.getFilmography().getFilmographyId();

        reviewService.delete(reviewId);

        // Reload after deletion so the list reflects the removed review
        Filmography updatedFilmography = filmographyService.findByIdWithReviews(filmographyId);
        updatedFilmography.updateAverageStars();
        filmographyService.save(updatedFilmography);

        return "redirect:/myReviews";
    }

    @GetMapping("/myLists")
    public String myLists(Model model, HttpSession session) {
        Account currentUser = (Account) session.getAttribute("user");

        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("lists", listsService.findByOwner(currentUser));

        return "myLists";
    }

    @GetMapping("/myReviews")
    public String myReviews(Model model, HttpSession session) {
        Account currentUser = (Account) session.getAttribute("user");

        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("reviews", reviewService.findByAuthor(currentUser));
        return "myReviews";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        return "profile";
    }
}