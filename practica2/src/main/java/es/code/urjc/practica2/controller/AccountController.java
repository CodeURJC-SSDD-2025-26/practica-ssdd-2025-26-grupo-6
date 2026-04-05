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
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.FilmographyService;

@Controller
public class AccountController {

    @Autowired
    private ListsService listsService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private FilmographyService filmographyService;

    @Autowired
    private ReviewService reviewService;

    AccountController(ListsService listsService) {
        this.listsService = listsService;
    }

    @GetMapping("/includeReview")
    public String includeReview(Model model) {
        return "includeReview";
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
    public String saveReview(@PathVariable Long filmographyId, Review review) {
        Filmography filmography = filmographyService.findById(filmographyId);

        review.setFilmography(filmography);
        review.setReviewAuthor(accountService.getCurrentUser());
        reviewService.save(review);

        filmography.getFilmographyReviews().add(review);
        filmography.updateAverageStars();
        filmographyService.save(filmography);

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
    public String updateReview(@PathVariable Long reviewId, @RequestParam Float reviewStars,
            @RequestParam String reviewDescription) {
        Review review = reviewService.update(reviewId, reviewStars, reviewDescription);
        Filmography filmography = review.getFilmography();

        filmography.updateAverageStars();
        filmographyService.save(filmography);

        return "redirect:/filmographies/" + review.getFilmography().getFilmographyId();
    }

    @PostMapping("/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable Long reviewId) {
        Review review = reviewService.findById(reviewId);
        Filmography filmography = review.getFilmography();

        filmography.getFilmographyReviews().remove(review);

        filmography.updateAverageStars();
        filmographyService.save(filmography);

        return "redirect:/filmographies/" + filmography.getFilmographyId();
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