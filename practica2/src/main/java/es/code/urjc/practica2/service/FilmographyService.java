package es.code.urjc.practica2.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Filmography findByIdWithReviews(Long id) {
        return filmographyRepository.findByIdWithReviews(id)
                .orElseThrow(() -> new RuntimeException("Filmography not found"));
    }

    public void recalculateAllAverages() {
        filmographyRepository.findAll().forEach(f -> {
            filmographyRepository.findByIdWithReviews(f.getFilmographyId()).ifPresent(loaded -> {
                loaded.updateAverageStars();
                filmographyRepository.save(loaded);
            });
        });
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

    public Serie updateSeries(Long id, Filmography updatedSerie) {
        Serie existingSerie = findSeriesById(id);

        existingSerie.setFilmographyName(updatedSerie.getFilmographyName());
        existingSerie.setFilmographyDirector(updatedSerie.getFilmographyDirector());
        existingSerie.setFilmographyYear(updatedSerie.getFilmographyYear());
        existingSerie.setSerieDuration(((Serie) updatedSerie).getSerieDuration());
        existingSerie.setFilmographyGenres(updatedSerie.getFilmographyGenres());
        existingSerie.setFilmographyPlatforms(updatedSerie.getFilmographyPlatforms());
        existingSerie.setFilmographySynopsis(updatedSerie.getFilmographySynopsis());
        existingSerie.setFilmographyTrailerUrl(updatedSerie.getFilmographyTrailerUrl());

        return filmographyRepository.save(existingSerie);
    }
}
