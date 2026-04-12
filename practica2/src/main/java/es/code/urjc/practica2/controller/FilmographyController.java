package es.code.urjc.practica2.controller;

import java.security.Principal;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.stream.Collectors;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.FilmographyService;
import es.code.urjc.practica2.service.ListsService;


@Controller
public class FilmographyController {
    @Autowired private FilmographyService filmographyService;
    @Autowired private AccountService accountService;
    @Autowired private ListsService listsService;

    @GetMapping("/principal")
    public String principal(Model model) {

        model.addAttribute("newMovies", filmographyService.findTop10MoviesByYear());

        Account admin = accountService.findByName("admin");
        List<Lists> systemLists = listsService.findByOwner(admin);

        List<Map<String, Object>> movieSections = new ArrayList<>();

        for (Lists list : systemLists) {
            if (list.getListName().endsWith("- Series"))
                continue;
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

        model.addAttribute("movieSections", movieSections);
        return "principal";
    }

    private String formatGenre(Genres g) {
        return switch (g) {
            case ACCION -> "Acción";
            case AVENTURA -> "Aventura";
            case CIENCIA_FICCION -> "Ciencia Ficción";
            case SUSPENSE -> "Suspense";
            case DRAMA -> "Drama";
            case MIEDO -> "Miedo";
            case COMEDIA -> "Comedia";
            case ROMANCE -> "Romance";
        };
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

        Account admin = accountService.findByName("admin");
        List<Lists> systemLists = listsService.findByOwner(admin);

        List<Map<String, Object>> seriesSections = new ArrayList<>();

        for (Lists list : systemLists) {
            if (!list.getListName().endsWith("- Series"))
                continue;

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

        model.addAttribute("seriesSections", seriesSections);
        return "series";
    }

    @GetMapping("/filmographies/{id}")
    public String detail(@PathVariable Long id, Model model, Principal principal) {
        Filmography filmography = filmographyService.findById(id);
        Account currentUser = null;
        if (principal != null){
            currentUser = accountService.findByEmail(principal.getName());
        }

        model.addAttribute("filmography", filmography);

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
            for (Lists list : currentUser.getAccountLists()) {
                Map<String, Object> listMap = new HashMap<>();
                listMap.put("listsId", list.getListsId());
                listMap.put("listName", list.getListName());
                listMap.put("checked", list.getFilmographyList().contains(filmography));
                userListsWithCheck.add(listMap);
            }
        }
        model.addAttribute("userLists", userListsWithCheck);

        //Chart
        List<Review> reviews = filmography.getFilmographyReviews();

        float[] starValues = {0f, 0.5f , 1f, 1.5f, 2f, 2.5f, 3f, 3.5f, 4f, 4.5f, 5f};
        long[] counts = new long[starValues.length];

        for(int i = 0; i < starValues.length; i++){
            final float star= starValues[i];
            counts[i] = reviews.stream().filter(r -> r.getReviewStars() != null && Float.compare(r.getReviewStars(), star) ==0 ).count();
        }

        String chartData = "[" + Arrays.stream(counts).mapToObj(String::valueOf).collect(Collectors.joining(","))+ "]";

        model.addAttribute("chartData", chartData);

        return "filmographyDetails";
    }

    // Updates which lists contain this filmography (add or remove based on checkboxes)
    @PostMapping("/filmographies/{id}/lists/update")
    @ResponseBody
    public ResponseEntity<Void> updateFilmographyLists(@PathVariable Long id,
            @RequestParam(required = false) List<Long> listIds, Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).build();

        Filmography filmography = filmographyService.findById(id);
        Account currentUser = accountService.findByEmail(principal.getName());

        for (Lists list : currentUser.getAccountLists()) {
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
        return ResponseEntity.ok().build();
    }

    @PostMapping("/filmographies/{id}/lists/new")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createFilmographyList(@PathVariable Long id,
            @RequestParam String newListName, Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).build();

        Account currentUser = accountService.findByEmail(principal.getName());

        Lists newList = new Lists(newListName.trim(), new ArrayList<>());
        newList.setListOwner(currentUser);
        listsService.save(newList);
        currentUser.getAccountLists().add(newList);
        accountService.save(currentUser);

        Map<String, Object> result = new HashMap<>();
        result.put("listsId", newList.getListsId());
        result.put("listName", newList.getListName());
        return ResponseEntity.ok(result);
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
            return "redirect:/lists"; // o página de error si quieres
        }

        model.addAttribute("listName", list.getListName().replace(" - Series", ""));
        model.addAttribute("filmographyList", list.getFilmographyList());

        return "filmslists";
    }

    // 1. 20 películas más recientes
    @GetMapping("/films/recent")
    public String recentFilms(Model model) {
        model.addAttribute("filmographyList", filmographyService.getRecentFilms(20));
        model.addAttribute("listName", "Películas Recientes");
        return "filmslists"; // Usaremos un HTML genérico
    }

    @PostMapping("/searchBar")
    public String searchBar(Model model, @RequestParam String search) {
        String query = (search != null) ? search.trim() : "";

        if (query.isEmpty()) {
            return "redirect:/principal";
        }

        // 1. Search by title
        List<Filmography> byTitle = filmographyService.findByTitleContaining(query);

        // 2. Search by genre (normalized for Enum comparison)
        List<Filmography> byGenre = new ArrayList<>();
        try {
            // Replaces spaces with underscores and removes common Spanish accents to match Enum constants
            String enumQuery = query.toUpperCase().replace(" ", "_")
                                    .replace("Ó", "O").replace("É", "E")
                                    .replace("Í", "I").replace("Á", "A");
            
            Genres genreSearched = Genres.valueOf(enumQuery);
            byGenre = filmographyService.findByGenre(genreSearched);
        } catch (IllegalArgumentException e) {
            // Not a valid genre, list remains empty
        }

        Set<Filmography> relatedFilms = new HashSet<>(); 
        relatedFilms.addAll(filmographyService.findFilmographyRelatedByTitleOrGenre(query));

        // 3. Combine results using a Set to avoid duplicates
        Set<Filmography> uniqueResults = new HashSet<>(byTitle);
        uniqueResults.addAll(byGenre);

        // 4. Split into Movies and Series
        List<Movie> movies = uniqueResults.stream()
                .filter(f -> f instanceof Movie)
                .map(f -> (Movie) f)
                .toList();

        List<Serie> series = uniqueResults.stream()
                .filter(f -> f instanceof Serie)
                .map(f -> (Serie) f)
                .toList();

        // 5. Check if both categories are empty
        boolean noResults = movies.isEmpty() && series.isEmpty() && relatedFilms.isEmpty();

        // 6. Add attributes to the model
        model.addAttribute("query", query);
        model.addAttribute("movies", movies);
        model.addAttribute("series", series);
        model.addAttribute("noResults", noResults);
        model.addAttribute("related", relatedFilms);

        return "searchBar"; 
    }
}
