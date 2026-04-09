package es.code.urjc.practica2.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Genre.Genres;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.repository.FilmographyRepository;



@Service
public class FilmographyService {
    @Autowired private FilmographyRepository filmographyRepository;

    public Filmography findById(Long id) {
        return filmographyRepository.findById(Objects.requireNonNull(id)).orElse(null);
    }

    public Movie findMovieById(Long id) {
        return (Movie) filmographyRepository.findById(Objects.requireNonNull(id)).orElseThrow(() -> new RuntimeException("Película no encontrada"));
    }

    public Serie findSeriesById(Long id) {
        return (Serie) filmographyRepository.findById(Objects.requireNonNull(id)).orElseThrow(() -> new RuntimeException("Serie no encontrada"));
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

    public List<Filmography.Platforms> toPlatformList(List<String> platformIds) {
        if (platformIds == null) return new ArrayList<>();
        return platformIds.stream()
                .map(Filmography.Platforms::valueOf)
                .collect(java.util.stream.Collectors.toList());
    }

    public Filmography save(Filmography filmography) {
        return filmographyRepository.save(Objects.requireNonNull(filmography));
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
        if (updatedMovie.getFilmographyImage() != null) {
            existingMovie.setFilmographyImage(updatedMovie.getFilmographyImage());
        }

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
        if (updatedSerie.getFilmographyImage() != null) {
            existingSerie.setFilmographyImage(updatedSerie.getFilmographyImage());
        }

        return filmographyRepository.save(existingSerie);
    }

    public List<Movie> getRecentFilms(int limit) {
        return filmographyRepository.findTop10MoviesByYear()
                .stream()
                .limit(limit)
                .toList();
    }

    public List<Filmography> getFilmsByGenre(String genre) {
        Genres g = Genres.valueOf(genre.toUpperCase());
        return filmographyRepository.findByFilmographyGenres_Genres(g);
    }

}
