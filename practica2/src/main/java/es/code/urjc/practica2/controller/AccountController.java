package es.code.urjc.practica2.controller;

import java.time.LocalDate;

import es.code.urjc.practica2.service.ListsService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Lists;
import es.code.urjc.practica2.service.ReviewService;
import es.code.urjc.practica2.service.AccountService;
import jakarta.servlet.http.HttpSession;
import es.code.urjc.practica2.service.FilmographyService;

@Controller
public class AccountController {
    @Autowired private ListsService listsService;
    @Autowired private FilmographyService filmographyService;
    @Autowired private ReviewService reviewService;
    @Autowired private AccountService accountService;

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
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null){
            return "redirect:/login";
        }

        Filmography filmography = filmographyService.findById(filmographyId);
        Account currentUser = accountService.findById(userId);

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
    public String updateReview(@PathVariable Long reviewId, @RequestParam Float reviewStars,
            @RequestParam String reviewDescription) {
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

    @PostMapping("/myLists/new")
    public String addOwnList(Model model, String listName, HttpSession session) {
        Account currentUser = (Account) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        // 1. Validar si ya existe una lista con ese nombre para ese usuario
        List<Lists> userLists = listsService.findByOwner(currentUser);

        for (Lists l : userLists) {
            if (l.getListName().equals(listName)) {
                model.addAttribute("nameError", "Ya tienes una lista con ese nombre");
                return "myLists";
            }
        }

        if (!listName.isBlank()) {
            Lists newList = new Lists();
            newList.setListName(listName);
            newList.setListOwner(currentUser);
            listsService.save(newList);
        }

        return "redirect:/myLists";
    }

    @PostMapping("/lists/{id}/update")
    public String updateListName(@PathVariable Long id, @RequestParam String newName, HttpSession session) {
        Account currentUser = (Account) session.getAttribute("user");
        Lists list = listsService.findById(id);

        if (list != null && currentUser != null
                && list.getListOwner().getAccountId().equals(currentUser.getAccountId())) {
            list.setListName(newName);
            listsService.save(list);
        }
        return "redirect:/myLists";
    }

    @PostMapping("/lists/{id}/delete")
    public String deleteList(@PathVariable Long id, HttpSession session) {
        Account currentUser = (Account) session.getAttribute("user");
        Lists list = listsService.findById(id);

        // Seguridad: Solo el dueño puede borrar
        if (list != null && currentUser != null && list.getListOwner().equals(currentUser)) {
            listsService.delete(list.getListsId());
        }
        return "redirect:/myLists";
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
    public String profile(Model model,  HttpSession session) {
        Account currentUser = (Account) session.getAttribute("user");

        if(currentUser == null){
            return "redirect:/login";
        }



        boolean isAdmin = currentUser.getAccountRole() == Account.Role.ADMIN;
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("currentUser",currentUser);

        return "profile";
    }
    @PostMapping("/profile/avatar")
    public String saveAvatar(@RequestParam String avatarSrc, HttpSession session){
        Account currentUser = (Account) session.getAttribute("user");

        currentUser.setAccountAvatar(avatarSrc);
        accountService.save(currentUser);
        return "redirect:/profile";
    }
    @PostMapping("/profile/edit")
    public String editProfile(@RequestParam String accountName , @RequestParam String accountEmail, @RequestParam LocalDate accountBirthDate, HttpSession session ){
        Account currentUser = (Account) session.getAttribute("user");
        currentUser.setAccountName(accountName);
        currentUser.setAccountEmail(accountEmail);
        currentUser.setAccountBirthDate(accountBirthDate);
        
        return "redirect:/profile";
    }
}