package es.code.urjc.practica2.controller;

import java.security.Principal;
import java.time.LocalDate;

import es.code.urjc.practica2.service.ListsService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Lists;
import es.code.urjc.practica2.service.ReviewService;
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.FilmographyService;

@Controller
public class AccountController {
    @Autowired
    private ListsService listsService;
    @Autowired
    private FilmographyService filmographyService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private AccountService accountService;

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
    public String saveReview(@PathVariable Long filmographyId, Review review, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Filmography filmography = filmographyService.findById(filmographyId);
        Account currentUser = accountService.findByEmail(principal.getName());

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
    public String myLists(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Account currentUser = accountService.findByEmail(principal.getName());

        boolean isAdmin = currentUser.getAccountRole() == Account.Role.ADMIN;
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("lists", listsService.findByOwner(currentUser));
        return "myLists";
    }

    @PostMapping("/myLists/new")
    public String addOwnList(Model model, String listName, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Account currentUser = accountService.findByEmail(principal.getName());

        // 1. Validate if the user has a list with the same name
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

    @GetMapping("/lists/{id}/edit")
    public String editList(@PathVariable Long id, Model model, Principal principal) {
        Lists list = listsService.findById(id);
        Account currentUser = accountService.findByEmail(principal.getName());

        // Todas las filmografías disponibles
        List<Filmography> allFilmographies = filmographyService.findAllFilmography();

        // Marcamos cuáles ya están en la lista
        List<Map<String, Object>> filmographiesWithCheck = new ArrayList<>();
        for (Filmography f : allFilmographies) {
            Map<String, Object> item = new HashMap<>();
            item.put("filmographyId", f.getFilmographyId());
            item.put("filmographyName", f.getFilmographyName());
            item.put("checked", list.getFilmographyList().contains(f));
            filmographiesWithCheck.add(item);
        }

        model.addAttribute("list", list);
        model.addAttribute("filmographies", filmographiesWithCheck);
        return "editList";
    }

    @PostMapping("/lists/{id}/update")
    public String updateListName(@PathVariable Long id,
            @RequestParam String newName,
            @RequestParam(required = false) List<Long> filmographyIds,
            Principal principal) {
        if (principal == null)
            return "redirect:/login";

        Account currentUser = accountService.findByEmail(principal.getName());
        Lists list = listsService.findById(id);

        if (list != null && list.getListOwner().getAccountId().equals(currentUser.getAccountId())) {
            list.setListName(newName);

            // Actualizar el contenido de la lista
            if (filmographyIds != null) {
                List<Filmography> selectedFilms = filmographyIds.stream()
                        .map(fId -> filmographyService.findById(fId))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                list.setFilmographyList(selectedFilms);
            } else {
                list.getFilmographyList().clear();
            }

            listsService.save(list);
        }
        return "redirect:/myLists";
    }

    @PostMapping("/lists/{id}/delete")
    public String deleteList(@PathVariable Long id, Principal principal) {
        if (principal == null)
            return "redirect:/login";

        Account currentUser = accountService.findByEmail(principal.getName());
        Lists list = listsService.findById(id);

        if (list != null && list.getListOwner().getAccountId().equals(currentUser.getAccountId())) {
            listsService.delete(list.getListsId());
        }
        return "redirect:/myLists";
    }

    @GetMapping("/myReviews")
    public String myReviews(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Account currentUser = accountService.findByEmail(principal.getName());

        boolean isAdmin = currentUser.getAccountRole() == Account.Role.ADMIN;
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("reviews", reviewService.findByAuthor(currentUser));
        return "myReviews";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Account currentUser = accountService.findByEmail(email);

        boolean isAdmin = currentUser.getAccountRole() == Account.Role.ADMIN;
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("currentUser", currentUser);

        // Chart
        List<Review> reviews = reviewService.findByAuthor(currentUser);

        float[] starValues = { 0f, 0.5f, 1f, 1.5f, 2f, 2.5f, 3f, 3.5f, 4f, 4.5f, 5f };
        long[] counts = new long[starValues.length];

        for (int i = 0; i < starValues.length; i++) {
            final float star = starValues[i];
            counts[i] = reviews.stream()
                    .filter(r -> r.getReviewStars() != null && Float.compare(r.getReviewStars(), star) == 0).count();
        }

        String chartData = "[" + Arrays.stream(counts).mapToObj(String::valueOf).collect(Collectors.joining(",")) + "]";

        model.addAttribute("chartData", chartData);

        return "profile";
    }

    @PostMapping("/profile/avatar")
    public String saveAvatar(@RequestParam String avatarSrc, Principal principal) {
        Account currentUser = accountService.findByEmail(principal.getName());

        currentUser.setAccountAvatar(avatarSrc);
        accountService.save(currentUser);
        return "redirect:/profile";
    }

    @PostMapping("/profile/edit")
    public String editProfile(@RequestParam String accountName, @RequestParam String accountEmail,
            @RequestParam LocalDate accountBirthDate, Principal principal) {
        Account currentUser = accountService.findByEmail(principal.getName());
        currentUser.setAccountName(accountName);
        currentUser.setAccountEmail(accountEmail);
        currentUser.setAccountBirthDate(accountBirthDate);

        accountService.save(currentUser);

        return "redirect:/profile";
    }
}