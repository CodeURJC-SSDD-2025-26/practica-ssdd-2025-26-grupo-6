package es.code.urjc.practica2.service;

import es.code.urjc.practica2.repository.ListsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Genre.Genres;
import es.code.urjc.practica2.model.Genre;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.model.Director;
import es.code.urjc.practica2.repository.FilmographyRepository;

@Service
public class FilmographyService {
    private final ListsRepository listsRepository;
    @Autowired
    private FilmographyRepository filmographyRepository;

    FilmographyService(ListsRepository listsRepository) {
        this.listsRepository = listsRepository;
    }

    public Filmography findById(Long id) {
        return filmographyRepository.findById(Objects.requireNonNull(id)).orElse(null);
    }

    public Movie findMovieById(Long id) {
        return (Movie) filmographyRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));
    }

    public Serie findSeriesById(Long id) {
        return (Serie) filmographyRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new RuntimeException("Serie no encontrada"));
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

    public List<Movie> getRecentFilms(int limit) {
        return filmographyRepository.findAll().stream()
                .filter(f -> f instanceof Movie)
                .map(f -> (Movie) f)
                .sorted((m1, m2) -> Long.compare(m2.getFilmographyId(), m1.getFilmographyId()))
                .limit(limit)
                .toList();
    }

    public Page<Movie> getRecentFilms(Pageable pageable) {
        return filmographyRepository.findMoviesOrderByYearDesc(pageable);
    }

    public Page<Serie> findAllSeriesPaged(Pageable pageable) {
        return filmographyRepository.findAllSeriesPaged(pageable);
    }    

    public List<Filmography> getFilmsByGenre(String genre) {
        Genres g = Genres.valueOf(genre.toUpperCase());
        return filmographyRepository.findByFilmographyGenres_Genres(g);
    }

    public List<Filmography> findByTitleContaining(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return filmographyRepository.findByTitleContaining(name);
    }

    public List<Filmography> findFilmographyRelatedByTitleOrGenre(String query) {
        if (query == null || query.trim().isEmpty())
            return new ArrayList<>();

        List<Filmography> directMatches = filmographyRepository.findByTitleContaining(query);

        if (directMatches.isEmpty())
            return new ArrayList<>();

        Set<Genres> genresOfDirectMatches = directMatches.stream()
                .flatMap(f -> f.getFilmographyGenres().stream())
                .map(Genre::getGenres)
                .collect(Collectors.toSet());

        Set<Filmography> relatedByGenre = new HashSet<>();
        for (Genres genre : genresOfDirectMatches) {
            relatedByGenre.addAll(filmographyRepository.findByFilmographyGenres_Genres(genre));
        }

        Set<Long> directIds = directMatches.stream()
                .map(Filmography::getFilmographyId)
                .collect(Collectors.toSet());

        return relatedByGenre.stream()
                .filter(f -> !directIds.contains(f.getFilmographyId()))
                .collect(Collectors.toList());
    }

    public List<Filmography> findAllFilmography() {
        return filmographyRepository.findAll();
    }

    public List<Movie> findAllMovies() {
        return filmographyRepository.findAll().stream().filter(f -> f instanceof Movie).map(f -> (Movie) f).toList();
    }

    public Page<Movie> findAllMoviesPage(Pageable pageable) {
        return filmographyRepository.findAllMovies(pageable);
    }

    public List<Serie> findAllSeries() {
        return filmographyRepository.findAll().stream().filter(f -> f instanceof Serie).map(f -> (Serie) f).toList();
    }

    public Page<Serie> findAllSeriesPage(Pageable pageable) {
        return filmographyRepository.findAllSeries(pageable);
    }

    public List<Filmography> findByDirector(Director director) {
        return filmographyRepository.findByFilmographyDirector(director);
    }

    public Map<String, Long> countByGenre() {
        Map<String, Long> result = new LinkedHashMap<>();

        List<Filmography> all = filmographyRepository.findAll();

        for (Filmography f : all) {
            for (Genre g : f.getFilmographyGenres()) {
                String name = g.getGenres().name();
                if (result.containsKey(name)) {
                    result.put(name, result.get(name) + 1);
                } else {
                    result.put(name, 1L);
                }
            }

        }
        return result;
    }

    public void recalculateAllAverages() {
        filmographyRepository.findAll().forEach(f -> {
            filmographyRepository.findByIdWithReviews(f.getFilmographyId()).ifPresent(loaded -> {
                loaded.updateAverageStars();
                filmographyRepository.save(loaded);
            });
        });
    }

    public Filmography getByName(String name) {
        return filmographyRepository.findByFilmographyName(name);
    }

    public List<Filmography.Platforms> toPlatformList(List<String> platformIds) {
        if (platformIds == null)
            return new ArrayList<>();
        return platformIds.stream()
                .map(Filmography.Platforms::valueOf)
                .collect(java.util.stream.Collectors.toList());
    }

    public Filmography save(Filmography filmography) {
        if (filmography.getFilmographyGenres()==null || filmography.getFilmographyName()==null ||  filmography.getFilmographyTrailerUrl()==null || filmography.getFilmographySynopsis()==null
         || filmography.getFilmographyYear()==0) {
            return null;
        }
        
        return filmographyRepository.save(Objects.requireNonNull(filmography));
    }

    public Movie updateMovie(Long id, Filmography updatedMovie) {
        Movie existingMovie = findMovieById(id);

        if (updatedMovie.getFilmographyName() != null) {
            existingMovie.setFilmographyName(updatedMovie.getFilmographyName());
        }
        if (updatedMovie.getFilmographyDirector() != null) {
            existingMovie.setFilmographyDirector(updatedMovie.getFilmographyDirector());
        }
        if (updatedMovie.getFilmographyYear() != 0) {
            existingMovie.setFilmographyYear(updatedMovie.getFilmographyYear());
        }
        if (updatedMovie.getFilmographyGenres() != null) {
            existingMovie.setFilmographyGenres(updatedMovie.getFilmographyGenres());
        }
        if (updatedMovie.getFilmographyPlatforms() != null) {
            existingMovie.setFilmographyPlatforms(updatedMovie.getFilmographyPlatforms());
        }
        if (updatedMovie.getFilmographySynopsis() != null) {
            existingMovie.setFilmographySynopsis(updatedMovie.getFilmographySynopsis());
        }
        if (updatedMovie.getFilmographyTrailerUrl() != null) {
            existingMovie.setFilmographyTrailerUrl(updatedMovie.getFilmographyTrailerUrl());
        }
        if (updatedMovie.getFilmographyImage() != null) {
            existingMovie.setFilmographyImage(updatedMovie.getFilmographyImage());
        }
        if (updatedMovie.getFilmographyImage() != null) {
            existingMovie.setFilmographyImage(updatedMovie.getFilmographyImage());
        }

        return filmographyRepository.save(existingMovie);
    }

    public Serie updateSeries(Long id, Filmography updatedSerie) {
        Serie existingSerie = findSeriesById(id);

        if (updatedSerie.getFilmographyName() != null) {
            existingSerie.setFilmographyName(updatedSerie.getFilmographyName());
        }
        if (updatedSerie.getFilmographyDirector() != null) {
            existingSerie.setFilmographyDirector(updatedSerie.getFilmographyDirector());
        }
        if (updatedSerie.getFilmographyYear() != 0) {
            existingSerie.setFilmographyYear(updatedSerie.getFilmographyYear());
        }
        if (updatedSerie.getFilmographyGenres() != null) {
            existingSerie.setFilmographyGenres(updatedSerie.getFilmographyGenres());
        }
        if (updatedSerie.getFilmographyPlatforms() != null) {
            existingSerie.setFilmographyPlatforms(updatedSerie.getFilmographyPlatforms());
        }
        if (updatedSerie.getFilmographySynopsis() != null) {
            existingSerie.setFilmographySynopsis(updatedSerie.getFilmographySynopsis());
        }
        if (updatedSerie.getFilmographyTrailerUrl() != null) {
            existingSerie.setFilmographyTrailerUrl(updatedSerie.getFilmographyTrailerUrl());
        }
        if (updatedSerie.getFilmographyImage() != null) {
            existingSerie.setFilmographyImage(updatedSerie.getFilmographyImage());
        }
        if (updatedSerie.getFilmographyImage() != null) {
            existingSerie.setFilmographyImage(updatedSerie.getFilmographyImage());
        }

        return filmographyRepository.save(existingSerie);
    }

    public void deleteMovie(Long id) {
        listsRepository.findAll().forEach(list -> {
            list.getFilmographyList().removeIf(f -> f.getFilmographyId().equals(id));
            listsRepository.save(list);
        });
        filmographyRepository.deleteById(Objects.requireNonNull(id));
    }

    public void deleteSerie(Long id) {
        listsRepository.findAll().forEach(list -> {
            list.getFilmographyList().removeIf(f -> f.getFilmographyId().equals(id));
            listsRepository.save(list);
        });
        filmographyRepository.deleteById(Objects.requireNonNull(id));
    }

    public List<Map<String, Object>> builtStartsList(float avg) {
        List<Map<String, Object>> startsList = new ArrayList<>();
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
            startsList.add(star);
        }
        return startsList;
    }

    public Map<String, Object> search(String query) {
        List<Filmography> directResults = findByTitleContaining(query);

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
            byGenre = findByGenre(genreSearched);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        Set<Filmography> uniqueResults = new HashSet<>(directResults);
        uniqueResults.addAll(byGenre);

        List<Movie> movies = uniqueResults.stream()
                .filter(f -> f instanceof Movie).map(f -> (Movie) f).toList();

        List<Serie> series = uniqueResults.stream()
                .filter(f -> f instanceof Serie).map(f -> (Serie) f).toList();

        List<Filmography> relatedFilms = findFilmographyRelatedByTitleOrGenre(query);
        relatedFilms.removeAll(new ArrayList<>(uniqueResults));

        Map<String, Object> result = new HashMap<>();
        result.put("movies", movies);
        result.put("series", series);
        result.put("related", relatedFilms);
        result.put("noResults", movies.isEmpty() && series.isEmpty() && relatedFilms.isEmpty());
        return result;
    }

}