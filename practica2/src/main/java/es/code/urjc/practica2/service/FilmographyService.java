package es.code.urjc.practica2.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import org.springframework.stereotype.Service;

import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Genre.Genres;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.repository.FilmographyRepository;

@Service
public class FilmographyService {
    @Autowired
    private FilmographyRepository filmographyRepository;

    public Filmography findById(Long id) {
        return filmographyRepository.findById(id).orElse(null);
    }

    public Movie findMovieById(Long id) {
        return (Movie) filmographyRepository.findById(id).orElseThrow(() -> new RuntimeException("Película no encontrada"));
    }

    public Serie findSeriesById(Long id) {
        return (Serie) filmographyRepository.findById(id).orElseThrow(() -> new RuntimeException("Serie no encontrada"));
    }

    public List<Filmography> findByGenre(Genres genre) {
        return filmographyRepository.findByFilmographyGenres_Genres(genre);
    }

    public List<Movie> findTop10MoviesByYear() {
        return filmographyRepository.findTop10MoviesByYear();
    }



    public List<Serie> findSeriesByGenre(Genres genre) {
    return filmographyRepository.findByFilmographyGenres_Genres(genre)
            .stream()
            .filter(f -> f instanceof Serie)
            .map(f -> (Serie) f)
            .toList();
    }

    public List<Movie> findMoviesByGenre(Genres genre) {
        return filmographyRepository.findByFilmographyGenres_Genres(genre)
                .stream()
                .filter(f -> f instanceof Movie)
                .map(f -> (Movie) f)
                .toList();
    }

    public Filmography save(Filmography filmography) {
        return filmographyRepository.save(filmography);
    }
}
