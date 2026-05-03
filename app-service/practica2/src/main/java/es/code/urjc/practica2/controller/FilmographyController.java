package es.code.urjc.practica2.controller;

import java.security.Principal;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Genre.Genres;
import es.code.urjc.practica2.model.Lists;
import es.code.urjc.practica2.model.Lists.Types;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.FilmographyService;
import es.code.urjc.practica2.service.ListsService;
import es.code.urjc.practica2.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FilmographyController {
    @Autowired private FilmographyService filmographyService;
    @Autowired private AccountService accountService;
    @Autowired private ListsService listsService;
    @Autowired private ReviewService reviewService;

    @GetMapping("/principal")
    public String principal(Model model) {
        model.addAttribute("newMovies", filmographyService.findTop10MoviesByYear());

        model.addAttribute("movieSections", listsService.getMovieSections());
        return "principal";
    }

    @GetMapping("/lists")
    public String listsPage(Model model) {

    
        model.addAttribute("sections", listsService.getAllListSections());

        return "lists";
    }

    @GetMapping("/series")
    public String series(Model model) {
        model.addAttribute("seriesSections", listsService.getSeriesSections());
        return "series";
    }

    @GetMapping("/filmographies/{id}")
    public String detail(@PathVariable Long id, Model model, Principal principal, HttpServletRequest request) {
        Filmography filmography = filmographyService.findById(id);
        Account currentUser = null;
        if (principal != null) {
            currentUser = accountService.findByEmail(principal.getName());
        }

        model.addAttribute("filmography", filmography);
        model.addAttribute("logged", currentUser != null);
        model.addAttribute("currentUrl", request.getRequestURI());

        // Check if it's a movie or a serie to show the correct information
        if (filmography instanceof Serie serie) {
            model.addAttribute("isSeries", true);
            model.addAttribute("serieDuration", serie.getSerieDuration());
        } else {
            Movie movie = (Movie) filmography;
            model.addAttribute("isSeries", false);
            model.addAttribute("movieDuration", movie.getMovieDuration());
        }

        // Stars
        float avg = filmography.getFilmographyAverageStars();

        model.addAttribute("averageStars", Math.round(avg * 100.0f) / 100.0f);
        model.addAttribute("starsList", filmographyService.builtStartsList(avg));

        // Empty review
        model.addAttribute("review", new Review());

        // User lists
        if (currentUser != null) {
            model.addAttribute("userLists", listsService.getUserListsWithCheck(currentUser, filmography));
        } else {
            model.addAttribute("userLists", List.of());
        }

        // Chart
        model.addAttribute("chartData", reviewService.getChartData(filmography.getFilmographyReviews()));

        return "filmographyDetails";
    }

    // Updates which lists contain this filmography (add or remove based on
    // checkboxes)
    @PostMapping("/filmographies/{id}/lists/update")
    public String updateFilmographyLists(@PathVariable Long id, @RequestParam(required = false) List<Long> listIds,
            Principal principal) {
        if (principal == null)
            return "redirect:/login";

        listsService.updateFilmographyInUserLists(id, listIds, principal.getName());
        return "redirect:/filmographies/" + id;
    }

    @GetMapping("/filmographies/{id}/reviews")
    public String filmographyReviews(@PathVariable Long id, Model model) {
        Filmography filmography = filmographyService.findByIdWithReviews(id);
        model.addAttribute("filmography", filmography);
        model.addAttribute("reviews", filmography.getFilmographyReviews());
        return "review";
    }

    @GetMapping("/lists/{id}")
    public String showList(@PathVariable Long id, Model model) {
        Lists list = listsService.findById(id);

        if (list == null) {
            return "redirect:/lists";
        }

        model.addAttribute("listName", list.getListName().replace(" - Series", ""));
        model.addAttribute("filmographyList", list.getFilmographyList());

        return "filmslists";
    }

    @GetMapping("/films/recent")
    public String recentFilms(Model model) {
        model.addAttribute("filmographyList", filmographyService.getRecentFilms(20));
        model.addAttribute("listName", "Películas Recientes");
        return "filmslists";
    }

    @GetMapping("/searchBar")
    public String searchBar(Model model, @RequestParam String search) {
        String query = (search != null) ? search.trim() : "";
        if (query.isEmpty()) return "redirect:/principal";

        Map<String, Object> results = filmographyService.search(query);
        model.addAttribute("query", query);
        model.mergeAttributes(results); // mete movies, series, related, noResults
        return "searchBar";
    }

    @GetMapping("/api/search")
    @ResponseBody
    public List<Map<String, Object>> searchApi(@RequestParam String query) {
        List<Filmography> results = filmographyService.findByTitleContaining(query);
        return results.stream().map(f -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", f.getFilmographyId());
            map.put("name", f.getFilmographyName());
            map.put("type", (f instanceof Movie) ? "Película" : "Serie");
            return map;
        }).collect(Collectors.toList());
    }
}
