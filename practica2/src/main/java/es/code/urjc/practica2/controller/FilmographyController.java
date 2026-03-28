package es.code.urjc.practica2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FilmographyController {
    @GetMapping("/filmsLists")
    public String filmsLists(Model model) {
        return "filmsLists";
    }

    @GetMapping("/principal") //Header
    public String principal(Model model) {
        return "principal";
    }

    @GetMapping("/lists") //Header
    public String lists(Model model) {
        return "lists";
    }

    @GetMapping("/review")
    public String review(Model model) {
        return "review";
    }

    @GetMapping("/series") //Header
    public String series(Model model) {
        return "series";
    }

    @GetMapping("/movieDetails")
    public String movieDetails(Model model) {
        return "movieDetails";
    }

    @GetMapping("/seriesDetails")
    public String seriesDetails(Model model) {
        return "seriesDetails";
    }
}
