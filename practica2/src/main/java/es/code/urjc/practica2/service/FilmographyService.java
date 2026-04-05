package es.code.urjc.practica2.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import es.code.urjc.practica2.model.Filmography;
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

    public Filmography findByIdWithReviews(Long id) {
        return filmographyRepository.findByIdWithReviews(id)
                .orElseThrow(() -> new RuntimeException("Filmography not found"));
    }

    public Filmography save(Filmography filmography) {
        return filmographyRepository.save(filmography);
    }

    public Movie updateMovie(Long id, Filmography updatedMovie) {
        Movie existingMovie = findMovieById(id);

        existingMovie.setFilmographyName(updatedMovie.getFilmographyName());
        existingMovie.setFilmographyDirector(updatedMovie.getFilmographyDirector());
        existingMovie.setFilmographyYear(updatedMovie.getFilmographyYear());
        existingMovie.setMovieDuration(((Movie) updatedMovie).getMovieDuration());
        existingMovie.setFilmographyGenres(updatedMovie.getFilmographyGenres());
        existingMovie.setFilmographyPlatforms(updatedMovie.getFilmographyPlatforms());
        existingMovie.setFilmographySynopsis(updatedMovie.getFilmographySynopsis());
        existingMovie.setFilmographyTrailerUrl(updatedMovie.getFilmographyTrailerUrl());

        return filmographyRepository.save(existingMovie);
    }
}
