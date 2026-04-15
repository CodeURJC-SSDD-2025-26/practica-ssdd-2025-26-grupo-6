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
import java.util.Arrays;
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


@Controller
public class AdministratorController {
    @Autowired private FilmographyService filmographyService;
    @Autowired private DirectorService directorService;
    @Autowired private GenreService genreService;
    @Autowired private ImageService imageService;
    @Autowired private AccountService accountService;
    @Autowired private ListsService listsService;
    @Autowired private ReviewService reviewService;

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

        //Chart
        Map <String, Long> genreCount = filmographyService.countByGenre();
        String labels = "[" + genreCount.keySet().stream().map(k -> "\"" + k + "\"" ).collect(Collectors.joining(","))+ "]";
        String data = "[" + genreCount.values().stream().map(String::valueOf).collect(Collectors.joining(","))+ "]";


        model.addAttribute("chartLabels",labels);
        model.addAttribute("chartData",data);

        return "administrator";
    }
    //@GetMapping("/director/new")
    //public String


    @GetMapping("/administrator/profile/{id}/editProfile")
    public String editUserFromAdmin(@PathVariable Long id,Model model) {
        Account user = accountService.findById(id);
        model.addAttribute("currentUser",user);
        model.addAttribute("isAdmin",true);
        model.addAttribute("editMode",true);
        model.addAttribute("fromAdmin",true);

        //Chart
        List<Review> reviews = reviewService.findByAuthor(user);

        float[] starValues = {0f, 0.5f , 1f, 1.5f, 2f, 2.5f, 3f, 3.5f, 4f, 4.5f, 5f};
        long[] counts = new long[starValues.length];

        for(int i = 0; i < starValues.length; i++){
            final float star= starValues[i];
            counts[i] = reviews.stream().filter(r -> r.getReviewStars() != null && Float.compare(r.getReviewStars(), star) ==0 ).count();
        }

        String chartData = "[" + Arrays.stream(counts).mapToObj(String::valueOf).collect(Collectors.joining(","))+ "]";

        model.addAttribute("chartData", chartData);


        return "profile";
    }

    @PostMapping("/administrator/profile/{id}/editProfile")
    public String updateUserFromAdmin(@PathVariable Long id, @RequestParam String accountName,@RequestParam String accountEmail,@RequestParam LocalDate accountBirthDate){
        Account user = accountService.findById(id);
        user.setAccountName(accountName);
        user.setAccountEmail(accountEmail);
        user.setAccountBirthDate(accountBirthDate);
        accountService.save(user);
        
        return"redirect:/administrator";
    }
    
    @PostMapping("/administrator/account/{id}/delete")
    public String deleteUsers(@PathVariable Long id) {
        accountService.delete(id);
        
        return "redirect:/administrator";
    }

    @GetMapping("/administrator/profile/{id}/review")
    public String editUsersReviewFromAdmin(@PathVariable Long id,Model model) {
        Account user = accountService.findById(id);
        model.addAttribute("currentUser",user);
        
        boolean isAdmin = user.getAccountRole() == Account.Role.ADMIN;
        List<Review> reviews;
        if (isAdmin) {

            reviews = reviewService.findAll();
        }else{
            reviews = reviewService.findByAuthor(user);
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("isAdmin", true);
        model.addAttribute("fromAdmin",true);

        return "myReviews";
    }

    @GetMapping("/administrator/profile/{id}/userlist")
    public String editUsersListsFromAdmin(@PathVariable Long id,Model model) {
        Account user = accountService.findById(id);
        model.addAttribute("currentUser",user);
        List<Lists> list = listsService.findAllListsByAuthor(user);

        model.addAttribute("lists", list);
        model.addAttribute("isAdmin", true);
        model.addAttribute("fromAdmin",true);

        return "myLists";
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
    public String saveMovie(Movie movie, @RequestParam String directorName, @RequestParam(required = false) List<String> genreIds, @RequestParam(required = false) List<String> platformsIds, @RequestParam(required = false) MultipartFile imageFile) throws IOException {    
        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = imageService.createImage(imageFile.getInputStream());
            movie.setFilmographyImage(image);
        }

        movie.setFilmographyDirector(directorService.getDirectorByName(directorName));
        movie.setFilmographyGenres(genreService.getGenresByName(genreIds));
        movie.setFilmographyPlatforms(filmographyService.toPlatformList(platformsIds));
        filmographyService.save(movie);

        // Add movie to system lists that match its genres
        List<es.code.urjc.practica2.model.Lists> systemLists = listsService.findAllSystemLists();
        for (es.code.urjc.practica2.model.Lists list : systemLists) {
            if (list.getListName().endsWith("- Series")) continue;
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
    public String updateMovie(@PathVariable Long id, Movie movie, @RequestParam String directorName, @RequestParam(required = false) List<String> genreIds, @RequestParam(required = false) List<String> platformsIds, @RequestParam(required = false) MultipartFile imageFile) throws IOException {    
        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = imageService.createImage(imageFile.getInputStream());
            movie.setFilmographyImage(image);
        }

        movie.setFilmographyDirector(directorService.getDirectorByName(directorName));
        movie.setFilmographyGenres(genreService.getGenresByName(genreIds));
        movie.setFilmographyPlatforms(filmographyService.toPlatformList(platformsIds));
        filmographyService.updateMovie(id, movie);
        return "redirect:/administrator";
    }

    @PostMapping("/movies/{id}/delete")
    public String deleteMovie(@PathVariable Long id){
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
    public String saveSeries(Serie serie, @RequestParam String directorName, @RequestParam(required = false) List<String> genreIds, @RequestParam(required = false) List<String> platformsIds, @RequestParam(required = false) MultipartFile imageFile) throws IOException {    
        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = imageService.createImage(imageFile.getInputStream());
            serie.setFilmographyImage(image);
        }

        serie.setFilmographyDirector(directorService.getDirectorByName(directorName));
        serie.setFilmographyGenres(genreService.getGenresByName(genreIds));
        serie.setFilmographyPlatforms(filmographyService.toPlatformList(platformsIds));
        filmographyService.save(serie);

        // Add serie to system lists that match its genres
        // ESTO ES LO CORRECTO AHORA
        List<es.code.urjc.practica2.model.Lists> systemLists = listsService.findAllSystemLists();
        for (es.code.urjc.practica2.model.Lists list : systemLists) {
            if (!list.getListName().endsWith("- Series")) continue;
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
    public String updateSerie(@PathVariable Long id, Serie serie, @RequestParam String directorName, @RequestParam(required = false) List<String> genreIds, @RequestParam(required = false) List<String> platformsIds, @RequestParam(required = false) MultipartFile imageFile) throws IOException {    
        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = imageService.createImage(imageFile.getInputStream());
            serie.setFilmographyImage(image);
        }

        serie.setFilmographyDirector(directorService.getDirectorByName(directorName));
        serie.setFilmographyGenres(genreService.getGenresByName(genreIds));
        serie.setFilmographyPlatforms(filmographyService.toPlatformList(platformsIds));
        filmographyService.updateSeries(id, serie);
        return "redirect:/administrator";
    }
    @PostMapping("/series/{id}/delete")
    public String deleteSerie(@PathVariable Long id){
        filmographyService.deleteMovie(id);
        return "redirect:/administrator";
    }

    private void addEmptyFilmographyAttributes(Model model){
        model.addAttribute("filmographyName", "");
        model.addAttribute("filmographyDirector", "");
        model.addAttribute("filmographyYear", "");
        model.addAttribute("filmographySynopsis", "");
        model.addAttribute("filmographyTrailerUrl", "");
        model.addAttribute("allGenres", buildGenreList(null));
        model.addAttribute("allPlatforms", buildPlatformList(null));
    }

    private void addFilmographyAttributes(Model model, Filmography f){
        model.addAttribute("filmographyId", f.getFilmographyId());
        model.addAttribute("filmographyImage", f.getFilmographyImage());
        model.addAttribute("filmographyName", f.getFilmographyName());
        model.addAttribute("filmographyDirector", f.getDirectorName());
        model.addAttribute("filmographyYear", f.getFilmographyYear());
        model.addAttribute("filmographySynopsis", f.getFilmographySynopsis());
        model.addAttribute("filmographyTrailerUrl", f.getFilmographyTrailerUrl());
        model.addAttribute("allGenres", buildGenreList(f));
        model.addAttribute("allPlatforms", buildPlatformList(f));
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
            case ACCION -> "Acción";
            case AVENTURA -> "Aventura";
            case CIENCIA_FICCION -> "Ciencia Ficción";
            case SUSPENSE -> "Suspense";
            case DRAMA -> "Drama";
            case MIEDO -> "Miedo";
            case COMEDIA -> "Comedia";
            case ROMANCE -> "Romance";
            case CRIMEN -> "Crimen";
        };
    }
    
}
