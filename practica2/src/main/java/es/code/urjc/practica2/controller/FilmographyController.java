package es.code.urjc.practica2.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Genre.Genres;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Review;
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

    @GetMapping("/principal")
    public String principal(Model model) {

    model.addAttribute("peliculasNuevas",
        filmographyService.findTop10MoviesByYear());


        model.addAttribute("peliculasAccion",
            filmographyService.findByGenre(Genres.ACCION));

        model.addAttribute("peliculasAventura",
            filmographyService.findByGenre(Genres.AVENTURA));

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

    @GetMapping("/series")
    public String series(Model model) {

        // Obtener series por género
        List<Serie> aventura = filmographyService.findSeriesByGenre(Genres.AVENTURA);
        List<Serie> accion = filmographyService.findSeriesByGenre(Genres.ACCION);
        List<Serie> ciencia = filmographyService.findSeriesByGenre(Genres.CIENCIA_FICCION);

        // Enviar al modelo
        model.addAttribute("aventura", aventura);
        model.addAttribute("accion", accion);
        model.addAttribute("ciencia", ciencia);

        return "series";
    }


    @GetMapping("/filmographies/{id:[0-9]+}")
    public String detail(@PathVariable Long id, Model model) {
        Filmography filmography = filmographyService.findById(id);
        
        //Check if it's a movie or a serie to show the correct information in the template
        if (filmography instanceof Serie serie) {
            model.addAttribute("isSeries", true);
            model.addAttribute("serieDuration", serie.getSerieDuration());
        } else {
            Movie movie = (Movie) filmography;
            model.addAttribute("isSeries", false);
            model.addAttribute("movieDuration", movie.getMovieDuration());
        }
        
        // STARS
        List<Map<String, String>> stars = new ArrayList<>();
        double average = filmography.getFilmographyAverageStars();
        
        for (int i = 1; i <= 5; i++) {
            Map<String, String> star = new HashMap<>();
            if (average >= i) {
                star.put("class", "active"); // Estrella llena
            } else if (average > i - 1 && average < i) {
                star.put("class", "partial"); // Estrella media (si tu CSS lo soporta)
            } else {
                star.put("class", ""); // Estrella vacía
            }
            stars.add(star);
        }
        model.addAttribute("starsList", stars);

        model.addAttribute("filmography", filmography);

        //Review
        model.addAttribute("review", new Review());

        //Give user lists
        Account currentUser = accountService.getCurrentUser();
        if(currentUser != null) {
            model.addAttribute("userLists", currentUser.getAccountLists());
        } else {
            model.addAttribute("userLists", new ArrayList<>());
        }

        return "filmographyDetails";
    }
}
