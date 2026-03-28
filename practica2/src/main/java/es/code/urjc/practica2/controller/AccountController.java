package es.code.urjc.practica2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {
    @GetMapping("/includeReview")
    public String includeReview(Model model) {
        return "includeReview";
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

    @GetMapping("/profile")
    public String profile(Model model) {
        return "profile";
    }
}
