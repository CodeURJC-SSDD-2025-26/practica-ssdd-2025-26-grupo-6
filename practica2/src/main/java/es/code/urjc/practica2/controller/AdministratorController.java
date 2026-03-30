package es.code.urjc.practica2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.service.FilmographyService;

@Controller
public class AdministratorController {
    @GetMapping("/administrator")
    public String administrator(Model model) {
        return "administrator";
    }

    // INCLUDING/MODIFYING FILMOGRAPHIES
    @PostMapping("/movies/new")
    public String newMovie(Model model) {
        model.addAttribute("filmography", new Movie());
        model.addAttribute("isSeries", false);
        return "filmographyForm";
    }

    @GetMapping("/movies/{id}/edit")
    public String editMovie(@PathVariable Long id, Model model) {
        model.addAttribute("filmography", FilmographyService.findById(id));
        model.addAttribute("isSeries", false);
        return "filmographyForm";
    }

    // SERIES
    @GetMapping("/series/new")
    public String newSeries(Model model) {
        model.addAttribute("filmography", new Serie());
        model.addAttribute("isSeries", true);
        return "filmographyForm";
    }

    @GetMapping("/series/{id}/edit")
    public String editSeries(@PathVariable Long id, Model model) {
        model.addAttribute("filmography", FilmographyService.findById(id));
        model.addAttribute("isSeries", true);
        return "filmographyForm";
    }
    }
