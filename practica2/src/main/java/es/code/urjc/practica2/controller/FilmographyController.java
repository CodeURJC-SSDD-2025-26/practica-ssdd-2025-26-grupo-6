package es.code.urjc.practica2.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.FilmographyService;

@Controller
public class FilmographyController {
    @Autowired
    private FilmographyService filmographyService;

    @Autowired
    private AccountService accountService;

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

    @GetMapping("/filmographies/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Filmography filmography = filmographyService.findById(id);
        
        //Check if it's a movie or a serie to show the correct information in the template
        if (filmography instanceof Serie serie) {
            model.addAttribute("isSeries", true);
            model.addAttribute("seasons", serie.getSerieDuration());
        } else {
            Movie movie = (Movie) filmography;
            model.addAttribute("isSeries", false);
            model.addAttribute("duration", movie.getMovieDuration());
        }
        
        model.addAttribute("filmography", filmography);

        //Give user lists
        Account currentUser = accountService.getCurrentUser();
        if(currentUser != null) {
            model.addAttribute("userLists", currentUser.getAccountLists());
        } else {
            model.addAttribute("userLists", new ArrayList<>());
        }

        return "filmographyDetail";
    }
}
