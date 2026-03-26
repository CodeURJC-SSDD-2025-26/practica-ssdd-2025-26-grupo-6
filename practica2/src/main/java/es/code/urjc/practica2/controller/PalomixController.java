package es.code.urjc.practica2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PalomixController {
    @GetMapping("/aboutUs")
    public String aboutUs(Model model) {
        return "aboutUs";
    }

    @GetMapping("/administrator")
    public String administrator(Model model) {
        return "administrator";
    }

    @GetMapping("/cookies")
    public String cookies(Model model) {
        return "cookies";
    }

    @GetMapping("/filmsLists")
    public String filmsLists(Model model) {
        return "filmsLists";
    }

    @GetMapping("/frequentlyAskedQuestions")
    public String frequentlyAskedQuestions(Model model) {
        return "frequentlyAskedQuestions";
    }

    @GetMapping("/includeMovie")
    public String includeMovie(Model model) {
        return "includeMovie";
    }

    @GetMapping("/includeReview")
    public String includeReview(Model model) {
        return "includeReview";
    }

    @GetMapping("/includeSerie")
    public String includeSerie(Model model) {
        return "includeSerie";
    }

    @GetMapping("/legalAdvise")
    public String legalAdvise(Model model) {
        return "legalAdvise";
    }

    @GetMapping("/lists")
    public String lists(Model model) {
        return "lists";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/modifyMovie")
    public String modifyMovie(Model model) {
        return "modifyMovie";
    }

    @GetMapping("/movieReview")
    public String movieReview(Model model) {
        return "movieReview";
    }

    @GetMapping("/myLists")
    public String myLists(Model model) {
        return "myLists";
    }

    @GetMapping("/myReviews")
    public String myReviews(Model model) {
        return "myReviews";
    }

    @GetMapping("/principal")
    public String principal(Model model) {
        return "principal";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        return "profile";
    }

    @GetMapping("/review")
    public String review(Model model) {
        return "review";
    }

    @GetMapping("/series")
    public String series(Model model) {
        return "series";
    }

    @GetMapping("/seriesDetails")
    public String seriesDetails(Model model) {
        return "seriesDetails";
    }
    
    @GetMapping("/signUp")
    public String signUp(Model model) {
        return "signUp";
    }
}
