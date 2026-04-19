package es.code.urjc.practica2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Genre;
import es.code.urjc.practica2.model.Image;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.model.Lists;
import es.code.urjc.practica2.model.Director;
import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.service.FilmographyService;
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.DirectorService;
import es.code.urjc.practica2.service.GenreService;
import es.code.urjc.practica2.service.ImageService;
import es.code.urjc.practica2.service.ListsService;
import es.code.urjc.practica2.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;


@Controller
public class AdministratorController {
    @Autowired
    private FilmographyService filmographyService;
    @Autowired
    private DirectorService directorService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ListsService listsService;
    @Autowired
    private ReviewService reviewService;
    

    @GetMapping("/administrator")
    public String administrator(Model model) {

        List<Account> allUsers = accountService.findAll();
        model.addAttribute("usersPreview", allUsers.stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("users", allUsers);

        List<Movie> allMovies = filmographyService.findAllMovies();
        model.addAttribute("moviesPreview", allMovies.stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("movies", allMovies);

        List<Serie> allSeries = filmographyService.findAllSeries();
        model.addAttribute("seriesPreview", allSeries.stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("series", allSeries);

        var allSystemLists = listsService.findAllSystemLists();
        model.addAttribute("systemListsPreview", allSystemLists.stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("systemLists", allSystemLists);

        List<Director> allDirectors = directorService.findAll();
        model.addAttribute("directorPreview", allDirectors.stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("director", allDirectors);

        // Chart
        Map<String, Long> genreCount = filmographyService.countByGenre();
        String labels = "[" + genreCount.keySet().stream().map(k -> "\"" + k + "\"").collect(Collectors.joining(","))
                + "]";
        String data = "[" + genreCount.values().stream().map(String::valueOf).collect(Collectors.joining(",")) + "]";

        model.addAttribute("chartLabels", labels);
        model.addAttribute("chartData", data);

        return "administrator";
    }

    @GetMapping("/administrator/profile/{id}/editProfile")
    public String editUserFromAdmin(@PathVariable Long id, Model model) {
        Account user = accountService.findById(id);
        model.addAttribute("currentUser", user);
        model.addAttribute("isAdmin", true);
        model.addAttribute("editMode", true);
        model.addAttribute("fromAdmin", true);

        // Chart
        List<Review> reviews = reviewService.findByAuthor(user);
        model.addAttribute("chartData", reviewService.getChartData(reviews));

        return "profile";
    }

    @PostMapping("/administrator/profile/{id}/editProfile")
    public String updateUserFromAdmin(@PathVariable Long id, @RequestParam String accountName,
            @RequestParam String accountEmail, @RequestParam LocalDate accountBirthDate) {
        Account user = accountService.findById(id);
        user.setAccountName(accountName);
        user.setAccountEmail(accountEmail);
        user.setAccountBirthDate(accountBirthDate);
        accountService.save(user);

        return "redirect:/administrator";
    }

    @PostMapping("/administrator/account/{id}/delete")
    public String deleteUsers(@PathVariable Long id) {
        accountService.delete(id);

        return "redirect:/administrator";
    }

    @GetMapping("/administrator/profile/{id}/review")
    public String editUsersReviewFromAdmin(@PathVariable Long id, Model model, HttpServletRequest request) {
        Account user = accountService.findById(id);
        model.addAttribute("currentUrl", request.getRequestURI());

        model.addAttribute("currentUser", user);

        boolean isAdmin = user.getAccountRole() == Account.Role.ADMIN;
        List<Review> reviews;
        if (isAdmin) {

            reviews = reviewService.findAll();
        } else {
            reviews = reviewService.findByAuthor(user);
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("isAdmin", true);
        model.addAttribute("fromAdmin", true);

        return "myReviews";
    }

    @GetMapping("/administrator/profile/{id}/userlist")
    public String editUsersListsFromAdmin(@PathVariable Long id, Model model, HttpServletRequest request) {
        Account user = accountService.findById(id);
        model.addAttribute("currentUser", user);
        List<Lists> list = listsService.findAllListsByAuthor(user);

        model.addAttribute("lists", list);
        model.addAttribute("isAdmin", true);
        model.addAttribute("fromAdmin", true);
        model.addAttribute("currentUrl", request.getRequestURI());

        return "myLists";
    }

    @GetMapping("/director/new")
    public String newDirector(Model model) {
        model.addAttribute("directorName", "");
        model.addAttribute("directorBirthDate", "");

        return "directorForm";
    }

    @PostMapping("/director/new")
    public String saveDirector(@RequestParam String directorName, @RequestParam String directorBirthDate) {
        Director director = new Director();
        director.setDirectorName(directorName);
        director.setDirectorBirthDate(directorBirthDate);
        directorService.save(director);

        return "redirect:/administrator";
    }

    @GetMapping("/administrator/director/{id}/edit")
    public String editDirectorFromAdmin(@PathVariable Long id, Model model) {
        Director director = directorService.findById(id);
        model.addAttribute("director", director);
        model.addAttribute("directorId", director.getDirectorId());
        model.addAttribute("directorName", director.getDirectorName());
        model.addAttribute("directorBirthDate", director.getDirectorBirthDate());
        return "directorForm";
    }

    @PostMapping("/administrator/director/{id}/edit")
    public String updateDirector(@PathVariable Long id, @RequestParam String directorName,
            @RequestParam String directorBirthDate) {
        Director director = directorService.findById(id);
        director.setDirectorName(directorName);
        director.setDirectorBirthDate(directorBirthDate);
        directorService.save(director);

        return "redirect:/administrator";
    }

    @PostMapping("/administrator/director/{id}/delete")
    public String deleteDirector(@PathVariable Long id) {
        Director director = directorService.findById(id);

        // Remove the directors of his filmographies
        List<Filmography> filmographies = filmographyService.findByDirector(director);
        for (Filmography f : filmographies) {
            f.setFilmographyDirector(null);
            filmographyService.save(f);
        }
        directorService.delete(id);
        return "redirect:/administrator";
    }

    @PostMapping("/administrator/profile/{id}/changeRole")
    public String changeUserRole(@PathVariable Long id, @RequestParam(required = false) String isAdmin) {
        Account user = accountService.findById(id);
        if (isAdmin != null) {
            user.setAccountRole(Account.Role.ADMIN);
        } else {
            user.setAccountRole(Account.Role.USER);
        }
        accountService.save(user);
        return "redirect:/administrator";
    }

    // INCLUDING/MODIFYING FILMOGRAPHIES
    @GetMapping("/movies/new")
    public String newMovie(Model model) {
        addEmptyFilmographyAttributes(model);
        model.addAttribute("isSeries", false);
        model.addAttribute("movieDuration", "");
        return "filmographyForm";
    }

    @PostMapping("/movies/new")
    public String saveMovie(Movie movie, @RequestParam String directorName,
            @RequestParam(required = false) List<String> genreIds,
            @RequestParam(required = false) List<String> platformsIds,
            @RequestParam(required = false) MultipartFile imageFile) throws IOException {

        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = imageService.createImage(imageFile.getInputStream());
            movie.setFilmographyImage(image);
        }

        movie.setFilmographyDirector(
            "UNKNOWN".equals(directorName) ? null : directorService.getDirectorByName(directorName)
        );
        movie.setFilmographyGenres(genreService.getGenresByName(genreIds));
        movie.setFilmographyPlatforms(filmographyService.toPlatformList(platformsIds));
        filmographyService.save(movie);

        // Add movie to system lists that match its genres
        List<es.code.urjc.practica2.model.Lists> systemLists = listsService.findAllSystemLists();
        for (es.code.urjc.practica2.model.Lists list : systemLists) {
            if (list.getListName().endsWith("- Series"))
                continue;
            boolean matches = movie.getFilmographyGenres().stream()
                    .anyMatch(g -> formatGenre(g.getGenres()).equals(list.getListName()));
            if (matches) {
                list.getFilmographyList().add(movie);
                listsService.save(list);
            }
        }
        return "redirect:/administrator";
    }

    @GetMapping("/movies/{id}/edit")
    public String editMovie(@PathVariable Long id, Model model) {
        Movie movie = filmographyService.findMovieById(id);
        addFilmographyAttributes(model, movie);
        model.addAttribute("isSeries", false);
        model.addAttribute("movieDuration", movie.getMovieDuration());
        return "filmographyForm";
    }

    @PostMapping("/movies/{id}/edit")
    public String updateMovie(@PathVariable Long id, Movie movie, @RequestParam String directorName,
            @RequestParam(required = false) List<String> genreIds,
            @RequestParam(required = false) List<String> platformsIds,
            @RequestParam(required = false) MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = imageService.createImage(imageFile.getInputStream());
            movie.setFilmographyImage(image);
        }

        movie.setFilmographyDirector(
            "UNKNOWN".equals(directorName) ? null : directorService.getDirectorByName(directorName)
        );
        movie.setFilmographyGenres(genreService.getGenresByName(genreIds));
        movie.setFilmographyPlatforms(filmographyService.toPlatformList(platformsIds));
        filmographyService.updateMovie(id, movie);
        return "redirect:/administrator";
    }

    @PostMapping("/movies/{id}/delete")
    public String deleteMovie(@PathVariable Long id) {
        filmographyService.deleteMovie(id);
        return "redirect:/administrator";
    }

    @GetMapping("/series/new")
    public String newSeries(Model model) {
        addEmptyFilmographyAttributes(model);
        model.addAttribute("isSeries", true);
        model.addAttribute("serieDuration", "");
        return "filmographyForm";
    }

    @PostMapping("/series/new")
    public String saveSeries(Serie serie, @RequestParam String directorName,
            @RequestParam(required = false) List<String> genreIds,
            @RequestParam(required = false) List<String> platformsIds,
            @RequestParam(required = false) MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = imageService.createImage(imageFile.getInputStream());
            serie.setFilmographyImage(image);
        }

        serie.setFilmographyDirector(
            "UNKNOWN".equals(directorName) ? null : directorService.getDirectorByName(directorName)
        );
        serie.setFilmographyGenres(genreService.getGenresByName(genreIds));
        serie.setFilmographyPlatforms(filmographyService.toPlatformList(platformsIds));
        filmographyService.save(serie);

        // Add serie to system lists that match its genres
        List<Lists> systemLists = listsService.findAllSystemLists();
        for (Lists list : systemLists) {
            if (!list.getListName().endsWith("- Series"))
                continue;
            String listGenre = list.getListName().replace(" - Series", "");
            boolean matches = serie.getFilmographyGenres().stream()
                    .anyMatch(g -> formatGenre(g.getGenres()).equals(listGenre));
            if (matches) {
                list.getFilmographyList().add(serie);
                listsService.save(list);
            }
        }
        return "redirect:/administrator";
    }

    @GetMapping("/series/{id}/edit")
    public String editSeries(@PathVariable Long id, Model model) {
        Serie serie = filmographyService.findSeriesById(id);
        addFilmographyAttributes(model, serie);
        model.addAttribute("isSeries", true);
        model.addAttribute("serieDuration", serie.getSerieDuration());
        return "filmographyForm";
    }

    @PostMapping("/series/{id}/edit")
    public String updateSerie(@PathVariable Long id, Serie serie, @RequestParam String directorName,
            @RequestParam(required = false) List<String> genreIds,
            @RequestParam(required = false) List<String> platformsIds,
            @RequestParam(required = false) MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = imageService.createImage(imageFile.getInputStream());
            serie.setFilmographyImage(image);
        }

        serie.setFilmographyDirector(
            "UNKNOWN".equals(directorName) ? null : directorService.getDirectorByName(directorName)
        );
        serie.setFilmographyGenres(genreService.getGenresByName(genreIds));
        serie.setFilmographyPlatforms(filmographyService.toPlatformList(platformsIds));
        filmographyService.updateSeries(id, serie);
        return "redirect:/administrator";
    }

    @PostMapping("/series/{id}/delete")
    public String deleteSerie(@PathVariable Long id) {
        filmographyService.deleteMovie(id);
        return "redirect:/administrator";
    }

    private void addEmptyFilmographyAttributes(Model model) {
        model.addAttribute("filmographyName", "");
        model.addAttribute("allDirectors", buildDirectorList(null));
        model.addAttribute("filmographyYear", "");
        model.addAttribute("filmographySynopsis", "");
        model.addAttribute("filmographyTrailerUrl", "");
        model.addAttribute("allGenres", buildGenreList(null));
        model.addAttribute("allPlatforms", buildPlatformList(null));
    }

    private void addFilmographyAttributes(Model model, Filmography f) {
        model.addAttribute("filmographyId", f.getFilmographyId());
        model.addAttribute("filmographyImage", f.getFilmographyImage());
        model.addAttribute("filmographyName", f.getFilmographyName());
        model.addAttribute("allDirectors", buildDirectorList(f));
        model.addAttribute("filmographyYear", f.getFilmographyYear());
        model.addAttribute("filmographySynopsis", f.getFilmographySynopsis());
        model.addAttribute("filmographyTrailerUrl", f.getFilmographyTrailerUrl());
        model.addAttribute("allGenres", buildGenreList(f));
        model.addAttribute("allPlatforms", buildPlatformList(f));
    }

    private List<Map<String, Object>> buildDirectorList(Filmography filmography) {
        List<Map<String, Object>> allDirectors = new ArrayList<>();
        for (Director director : directorService.findAll()) {
            Map<String, Object> map = new HashMap<>();
            map.put("directorName", director.getDirectorName());
            boolean selected = filmography != null 
                && filmography.getFilmographyDirector() != null
                && director.getDirectorName().equals(filmography.getDirectorName());
            map.put("selected", selected);
            allDirectors.add(map);
        }
        return allDirectors;
    }

    private List<Map<String, Object>> buildGenreList(Filmography filmography) {
        List<Map<String, Object>> allGenres = new ArrayList<>();
        for (Genre.Genres genre : Genre.Genres.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", genre.name());
            map.put("displayName", genre.name());
            boolean selected = filmography != null && filmography.getFilmographyGenres().stream()
                    .anyMatch(g -> g.getGenres() == genre);
            map.put("selected", selected);
            allGenres.add(map);
        }
        return allGenres;
    }

    private List<Map<String, Object>> buildPlatformList(Filmography filmography) {
        List<Map<String, Object>> allPlatforms = new ArrayList<>();
        for (Filmography.Platforms platform : Filmography.Platforms.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", platform.name());
            map.put("displayName", platform.name());
            boolean selected = filmography != null && filmography.getFilmographyPlatforms().contains(platform);
            map.put("selected", selected);
            allPlatforms.add(map);
        }
        return allPlatforms;
    }

    @PostMapping("/administrator/systemLists/{id}/delete")
    public String deleteSystemList(@PathVariable Long id) {
        listsService.delete(id);

        return "redirect:/administrator";
    }

    @PostMapping("/administrator/userLists/{id}/delete")
    public String deleteUserLists(@PathVariable Long id) {
        listsService.delete(id);

        return "redirect:/administrator";
    }

    private String formatGenre(Genre.Genres g) {
        return switch (g) {
            case ACCIÓN -> "Acción";
            case ANIMACIÓN -> "Animación";
            case AVENTURA -> "Aventura";
            case BÉLICO -> "Bélico";
            case BIOGRÁFICO -> "Biográfico";
            case CIENCIA_FICCIÓN -> "Ciencia Ficción";
            case CINE_NEGRO -> "Cine Negro";
            case COMEDIA -> "Comedia";
            case CRIMEN -> "Crimen";
            case DEPORTE -> "Deporte";
            case DOCUMENTAL -> "Documental";
            case DRAMA -> "Drama";
            case FAMILIAR -> "Familiar";
            case FANTASÍA -> "Fantasía";
            case HISTORIA -> "Historia";
            case INDEPENDIENTE -> "Independiente";
            case MIEDO -> "Miedo";
            case MISTERIO -> "Misterio";
            case MUSICAL -> "Musical";
            case OESTE -> "Oeste";
            case REALITY -> "Reality";
            case ROMANCE -> "Romance";
            case SUSPENSE -> "Suspense";
        };
    }
}
