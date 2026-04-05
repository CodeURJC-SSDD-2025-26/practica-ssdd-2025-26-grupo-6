package es.code.urjc.practica2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Director;
import es.code.urjc.practica2.model.Genre;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.repository.DirectorRepository;
import es.code.urjc.practica2.repository.GenreRepository;
import es.code.urjc.practica2.service.FilmographyService;

@Controller
public class AdministratorController {
    @Autowired
    private FilmographyService filmographyService;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @GetMapping("/administrator")
    public String administrator(Model model) {
        return "administrator";
    }

    // INCLUDING/MODIFYING FILMOGRAPHIES
    @GetMapping("/movies/new")
    public String newMovie(Model model) {
        model.addAttribute("filmography", new Movie());
        model.addAttribute("isSeries", false);
        model.addAttribute("filmographyName", "");
        model.addAttribute("filmographyDirector", "");
        model.addAttribute("filmographyYear", "");
        model.addAttribute("filmographySynopsis", "");
        model.addAttribute("filmographyTrailerUrl", "");
        model.addAttribute("movieDuration", "");

        model.addAttribute("allGenres", buildGenreList(null));
        model.addAttribute("allPlatforms", buildPlatformList(null));

        return "filmographyForm";
    }

    @PostMapping("/movies/new")
    public String saveMovie(Movie movie, @RequestParam String directorName, @RequestParam(required = false) List<String> genreIds, @RequestParam(required = false) List<String> platforms) {
        Director director = directorRepository.findByDirectorName(directorName).orElseGet(() -> directorRepository.save(new Director(directorName, "")));

        List<Genre> genres = new ArrayList<>();
        if (genreIds != null) {
            for (String genreName : genreIds) {
                Genre.Genres genreEnum = Genre.Genres.valueOf(genreName);
                Genre genre = genreRepository.findByGenres(genreEnum)
                        .orElseThrow(() -> new RuntimeException("Genre not found: " + genreName));
                genres.add(genre);
            }
        }

        List<Filmography.Platforms> platformList = new ArrayList<>();
        if (platforms != null) {
            for (String platform : platforms) {
                platformList.add(Filmography.Platforms.valueOf(platform));
            }
        }

        movie.setFilmographyDirector(director);
        movie.setFilmographyGenres(genres);
        movie.setFilmographyPlatforms(platformList);

        filmographyService.save(movie);

        return "redirect:/administrator";
    }
    
    @GetMapping("/movies/{id}/edit")
    public String editMovie(@PathVariable Long id, Model model) {
        Movie movie = filmographyService.findMovieById(id);
        model.addAttribute("filmographyId", movie.getFilmographyId());
        model.addAttribute("isSeries", false);
        model.addAttribute("filmographyName", movie.getFilmographyName());
        model.addAttribute("filmographyDirector", movie.getDirectorName());
        model.addAttribute("filmographyYear", movie.getFilmographyYear());
        model.addAttribute("filmographySynopsis", movie.getFilmographySynopsis());
        model.addAttribute("filmographyTrailerUrl", movie.getFilmographyTrailerUrl());
        model.addAttribute("movieDuration", movie.getMovieDuration());

        model.addAttribute("allGenres", buildGenreList(movie));
        model.addAttribute("allPlatforms", buildPlatformList(movie));

        return "filmographyForm";
    }

    @PostMapping("/movies/{id}/edit")
    public String updateMovie(@PathVariable Long id,
                            Movie movie,
                            @RequestParam String directorName,
                            @RequestParam(required = false) List<String> genreIds,
                            @RequestParam(required = false) List<String> platforms) {

        Director director = directorRepository.findByDirectorName(directorName)
                .orElseGet(() -> directorRepository.save(new Director(directorName, "")));

        List<Genre> genres = new ArrayList<>();
        if (genreIds != null) {
            for (String genreName : genreIds) {
                Genre genre = genreRepository.findByGenres(Genre.Genres.valueOf(genreName))
                        .orElseThrow(() -> new RuntimeException("Genre not found: " + genreName));
                genres.add(genre);
            }
        }

        List<Filmography.Platforms> platformList = new ArrayList<>();
        if (platforms != null) {
            for (String platform : platforms) {
                platformList.add(Filmography.Platforms.valueOf(platform));
            }
        }

        movie.setFilmographyDirector(director);
        movie.setFilmographyGenres(genres);
        movie.setFilmographyPlatforms(platformList);

        filmographyService.updateMovie(id, movie);

        return "redirect:/administrator";
    }

    @GetMapping("/series/new")
    public String newSeries(Model model) {
        model.addAttribute("filmography", new Serie());
        model.addAttribute("isSeries", true);

        model.addAttribute("filmographyName", "");
        model.addAttribute("filmographyDirector", "");
        model.addAttribute("filmographyYear", "");
        model.addAttribute("filmographySynopsis", "");
        model.addAttribute("filmographyTrailerUrl", "");
        model.addAttribute("serieDuration", "");

        model.addAttribute("allGenres", buildGenreList(null));
        model.addAttribute("allPlatforms", buildPlatformList(null));
        return "filmographyForm";
    }

    @PostMapping("/series/new")
    public String saveSeries(Serie serie, @RequestParam String directorName, @RequestParam(required = false) List<String> genreIds, @RequestParam(required = false) List<String> platforms) {
        Director director = directorRepository.findByDirectorName(directorName).orElseGet(() -> directorRepository.save(new Director(directorName, "")));

        List<Genre> genres = new ArrayList<>();
        if (genreIds != null) {
            for (String genreName : genreIds) {
                Genre.Genres genreEnum = Genre.Genres.valueOf(genreName);
                Genre genre = genreRepository.findByGenres(genreEnum)
                        .orElseThrow(() -> new RuntimeException("Genre not found: " + genreName));
                genres.add(genre);
            }
        }

        List<Filmography.Platforms> platformList = new ArrayList<>();
        if (platforms != null) {
            for (String platform : platforms) {
                platformList.add(Filmography.Platforms.valueOf(platform));
            }
        }
        
        serie.setFilmographyDirector(director);
        serie.setFilmographyGenres(genres);
        serie.setFilmographyPlatforms(platformList);

        filmographyService.save(serie);

        return "redirect:/administrator";
    }

    @GetMapping("/series/{id}/edit")
    public String editSeries(@PathVariable Long id, Model model) {
        Serie serie = filmographyService.findSeriesById(id);
        model.addAttribute("filmography", filmographyService.findById(id));
        model.addAttribute("isSeries", true);

        model.addAttribute("filmographyName", serie.getFilmographyName());
        model.addAttribute("filmographyDirector", serie.getDirectorName());
        model.addAttribute("filmographyYear", serie.getFilmographyYear());
        model.addAttribute("filmographySynopsis", serie.getFilmographySynopsis());
        model.addAttribute("filmographyTrailerUrl", serie.getFilmographyTrailerUrl());
        model.addAttribute("serieDuration", serie.getSerieDuration());

        model.addAttribute("allGenres", buildGenreList(serie));
        model.addAttribute("allPlatforms", buildPlatformList(serie));

        return "filmographyForm";
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
}
