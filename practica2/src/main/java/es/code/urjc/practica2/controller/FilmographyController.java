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
    @Autowired
    private FilmographyService filmographyService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ListsService listsService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/principal")
    public String principal(Model model) {
        model.addAttribute("newMovies", filmographyService.findTop10MoviesByYear());

        List<Lists> systemLists = listsService.findAllSystemLists();

        List<Map<String, Object>> movieSections = new ArrayList<>();

        for (Lists list : systemLists) {
            if (list.getType().equals(Types.MOVIE)) {
                List<Movie> movies = list.getFilmographyList().stream()
                        .filter(f -> f instanceof Movie)
                        .map(f -> (Movie) f)
                        .toList();

                if (!movies.isEmpty()) {
                    Map<String, Object> sec = new HashMap<>();
                    sec.put("name", list.getListName());
                    sec.put("listsId", list.getListsId());
                    sec.put("movies", movies);
                    movieSections.add(sec);
                }
            }
        }

        model.addAttribute("movieSections", movieSections);
        return "principal";
    }

    @GetMapping("/lists")
    public String listsPage(Model model) {

        List<Map<String, Object>> sections = new ArrayList<>();

        Map<String, Object> sec1 = new HashMap<>();
        sec1.put("sectionTitle", "Listas Mejor Valoradas");
        sec1.put("lists", listsService.getBestRatedLists());
        sections.add(sec1);

        Map<String, Object> sec2 = new HashMap<>();
        sec2.put("sectionTitle", "Listas Peor Valoradas");
        sec2.put("lists", listsService.getWorstRatedLists());
        sections.add(sec2);

        Map<String, Object> sec3 = new HashMap<>();
        sec3.put("sectionTitle", "Listas Más Largas");
        sec3.put("lists", listsService.getLongestLists());
        sections.add(sec3);

        Map<String, Object> sec4 = new HashMap<>();
        sec4.put("sectionTitle", "Listas Películas Más Largas");
        sec4.put("lists", listsService.getLongestMoviesLists());
        sections.add(sec4);

        Map<String, Object> sec5 = new HashMap<>();
        sec5.put("sectionTitle", "Listas Series con Más Temporadas");
        sec5.put("lists", listsService.getSeriesWithMostSeasons());
        sections.add(sec5);

        model.addAttribute("sections", sections);

        return "lists";
    }

    @GetMapping("/series")
    public String series(Model model) {
        List<Lists> systemLists = listsService.findAllSystemLists();

        List<Map<String, Object>> seriesSections = new ArrayList<>();

        for (Lists list : systemLists) {
            if (list.getType().equals(Types.SERIE)) {
                List<Serie> seriesList = list.getFilmographyList().stream()
                        .filter(f -> f instanceof Serie)
                        .map(f -> (Serie) f)
                        .toList();

                if (!seriesList.isEmpty()) {
                    Map<String, Object> sec = new HashMap<>();
                    sec.put("name", list.getListName().replace(" - Series", ""));
                    sec.put("listsId", list.getListsId());
                    sec.put("series", seriesList);
                    seriesSections.add(sec);
                }
            }
        }

        model.addAttribute("seriesSections", seriesSections);
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
        List<Map<String, Object>> starsList = new ArrayList<>();
        float avg = filmography.getFilmographyAverageStars();
        model.addAttribute("averageStars", Math.round(avg * 100.0f) / 100.0f);
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> star = new HashMap<>();
            float fill;
            if (avg >= i) {
                fill = 100f;
            } else if (avg > i - 1) {
                fill = (avg - (i - 1)) * 100f;
            } else {
                fill = 0f;
            }
            star.put("fillPercent", Math.round(fill));
            star.put("hasColor", fill > 0);
            starsList.add(star);
        }
        model.addAttribute("starsList", starsList);

        // Empty review
        model.addAttribute("review", new Review());

        // User lists
        List<Map<String, Object>> userListsWithCheck = new ArrayList<>();
        if (currentUser != null) {
            List<Lists> listsToShow;
            if (currentUser.getAccountRole() == Account.Role.ADMIN) {
                listsToShow = listsService.findAllSystemLists();
            } else {
                listsToShow = currentUser.getAccountLists();
            }

            for (Lists list : listsToShow) {
                Map<String, Object> listMap = new HashMap<>();
                listMap.put("listsId", list.getListsId());
                listMap.put("listName", list.getListName());
                listMap.put("checked", list.getFilmographyList().contains(filmography));
                userListsWithCheck.add(listMap);
            }
        }
        model.addAttribute("userLists", userListsWithCheck);

        // Chart
        List<Review> reviews = filmography.getFilmographyReviews();
        model.addAttribute("chartData", reviewService.getChartData(reviews));

        return "filmographyDetails";
    }

    // Updates which lists contain this filmography (add or remove based on
    // checkboxes)
    @PostMapping("/filmographies/{id}/lists/update")
    public String updateFilmographyLists(@PathVariable Long id, @RequestParam(required = false) List<Long> listIds,
            Principal principal) {
        if (principal == null)
            return "redirect:/login";

        Filmography filmography = filmographyService.findById(id);
        Account currentUser = accountService.findByEmail(principal.getName());

        List<Lists> listsToUpdate;
        if (currentUser.getAccountRole() == Account.Role.ADMIN) {
            listsToUpdate = listsService.findAllSystemLists();
        } else {
            listsToUpdate = currentUser.getAccountLists();
        }

        for (Lists list : listsToUpdate) {
            boolean isChecked = listIds != null && listIds.contains(list.getListsId());
            boolean alreadyContains = list.getFilmographyList().contains(filmography);

            if (isChecked && !alreadyContains) {
                list.getFilmographyList().add(filmography);
                listsService.save(list);
            } else if (!isChecked && alreadyContains) {
                list.getFilmographyList().remove(filmography);
                listsService.save(list);
            }
        }
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

        if (query.isEmpty()) {
            return "redirect:/principal";
        }

        List<Filmography> directResults = filmographyService.findByTitleContaining(query);

        List<Filmography> byGenre = new ArrayList<>();
        try {
            String enumQuery = query.toUpperCase()
                    .replace(" ", "_")
                    .replace("Ó", "O").replace("É", "E")
                    .replace("Í", "I").replace("Á", "A")
                    .replace("CION", "CIÓN").replace("FICCION", "FICCIÓN")
                    .replace("FANTASIA", "FANTASÍA").replace("BIOGRAFICO", "BIOGRÁFICO")
                    .replace("BELICO", "BÉLICO").replace("ANIMACION", "ANIMACIÓN");

            Genres genreSearched = Genres.valueOf(enumQuery);
            byGenre = filmographyService.findByGenre(genreSearched);
        } catch (IllegalArgumentException e) {
           e.printStackTrace();
        }

        Set<Filmography> uniqueResults = new HashSet<>(directResults);
        uniqueResults.addAll(byGenre);

        List<Movie> movies = uniqueResults.stream()
                .filter(f -> f instanceof Movie)
                .map(f -> (Movie) f)
                .toList();

        List<Serie> series = uniqueResults.stream()
                .filter(f -> f instanceof Serie)
                .map(f -> (Serie) f)
                .toList();

        
        List<Filmography> relatedFilms = filmographyService.findFilmographyRelatedByTitleOrGenre(query);
        relatedFilms.removeAll(new ArrayList<>(uniqueResults));

        boolean noResults = movies.isEmpty() && series.isEmpty() && relatedFilms.isEmpty();

        model.addAttribute("query", query);
        model.addAttribute("movies", movies);
        model.addAttribute("series", series);
        model.addAttribute("related", relatedFilms);
        model.addAttribute("noResults", noResults);

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
