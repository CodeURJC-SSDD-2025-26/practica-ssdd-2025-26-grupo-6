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

import org.springframework.beans.factory.annotation.Autowired;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Genre;
import es.code.urjc.practica2.model.Image;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.service.FilmographyService;
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.DirectorService;
import es.code.urjc.practica2.service.GenreService;
import es.code.urjc.practica2.service.ImageService;
import es.code.urjc.practica2.service.ListsService;


@Controller
public class AdministratorController {
    @Autowired private FilmographyService filmographyService;
    @Autowired private DirectorService directorService;
    @Autowired private GenreService genreService;
    @Autowired private ImageService imageService;
    @Autowired private AccountService accountService;
    @Autowired private ListsService listsService;

    @GetMapping("/administrator")
    public String administrator(Model model) {
        model.addAttribute("users", accountService.findAll());
        model.addAttribute("movies", filmographyService.findAllMovies());
        model.addAttribute("series", filmographyService.findAllSeries());
        model.addAttribute("systemLists", listsService.findAllSistemLists());
        model.addAttribute("usersLists", listsService.findAllUserList());

        return "administrator";
    }


    @GetMapping("/administrator/profile/{id}/editProfile")
    public String editUserFromAdmin(@PathVariable Long id,Model model) {
        Account user = accountService.findById(id);
        model.addAttribute("currentUser",user);
        model.addAttribute("isAdmin",true);
        model.addAttribute("editMode",true);
        model.addAttribute("fromAdmin",true);

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


    @PostMapping("/administrator/lists/{id}/delete")
    public String deleteUserLists(@PathVariable Long id) {
        listsService.delete(id);
        
        return "redirect:/administrator";
    }
    
}
