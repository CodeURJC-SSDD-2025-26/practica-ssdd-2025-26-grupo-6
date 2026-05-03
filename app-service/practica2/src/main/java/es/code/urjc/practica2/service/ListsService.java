package es.code.urjc.practica2.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Genre;
import es.code.urjc.practica2.model.Lists;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.repository.ListsRepository;
import es.code.urjc.practica2.repository.FilmographyRepository;

@Service
public class ListsService {
    @Autowired
    private ListsRepository listsRepository;

    @Autowired
    private FilmographyRepository filmographyRepository;

    @Autowired 
    private AccountService accountService;

    public List<Lists> findByOwner(Account owner) {
        return listsRepository.findByListOwner(owner);
    }

    public Lists findById(Long id) {
        if (id == null) {
            return null;
        }
        return listsRepository.findById(id).orElse(null);
    }

    public Lists save(String listName, Lists.Types type, Account owner, List<Filmography> films) {
        Lists list = new Lists(listName, films, type);
        list.setListOwner(owner);
        listsRepository.save(list);
        return list;
    }

    public Lists save(@NonNull Lists list) {
        return listsRepository.save(list);
    }


    public void delete(Long id) {
        if (id != null) {
            listsRepository.deleteById(id);
        }
    }

    private Map<String, Object> convertToMap(Lists list) {
        Map<String, Object> map = new HashMap<>();

        map.put("listsId", list.getListsId());
        map.put("listName", list.getListName());
        map.put("size", list.getFilmographyList().size());
        map.put("authorName", list.getListOwner() != null ? list.getListOwner().getAccountName() : "Sistema");

        if (!list.getFilmographyList().isEmpty()) {
            Filmography first = list.getFilmographyList().get(0);
            map.put("firstImage", first.getFilmographyImageUrl());
        } else {
            map.put("firstImage", "/images/placeholder.png"); // opcional
        }

        return map;
    }

    // Best rated lists
    public List<Map<String, Object>> getBestRatedLists() {
        return findAllUserList().stream()
                .filter(l -> !l.getFilmographyList().isEmpty())
                .sorted(Comparator.comparingDouble(this::getListAverageRating).reversed())
                .limit(10)
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    // Worst rated lists
    public List<Map<String, Object>> getWorstRatedLists() {
        return findAllUserList().stream()
                .filter(l -> !l.getFilmographyList().isEmpty())
                .sorted(Comparator.comparingDouble(this::getListAverageRating))
                .limit(10)
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    // Longest lists (most items)
    public List<Map<String, Object>> getLongestLists() {
        return findAllUserList().stream()
                .filter(l -> !l.getFilmographyList().isEmpty())
                .sorted((a, b) -> Integer.compare(
                        b.getFilmographyList().size(),
                        a.getFilmographyList().size()))
                .limit(10)
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    // Lists with longest movies (average duration)
    public List<Map<String, Object>> getLongestMoviesLists() {
        return findAllUserList().stream()
                .filter(l -> !l.getFilmographyList().isEmpty())
                .sorted(Comparator.comparingDouble(this::getAverageMovieDuration).reversed())
                .limit(10)
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    // Lists with series with most seasons
    public List<Map<String, Object>> getSeriesWithMostSeasons() {
        return findAllUserList().stream()
                .filter(l -> !l.getFilmographyList().isEmpty())
                .sorted(Comparator.comparingInt(this::getAverageSeriesSeasons).reversed())
                .limit(10)
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    // Helper: average rating
    private double getListAverageRating(Lists list) {
        List<Review> allReviews = list.getFilmographyList().stream()
                .flatMap(f -> f.getFilmographyReviews().stream())
                .collect(Collectors.toList());

        if (allReviews.isEmpty())
            return 0;

        return allReviews.stream()
                .mapToDouble(Review::getReviewStars)
                .average()
                .orElse(0);
    }

    // Helper: average movie duration
    private double getAverageMovieDuration(Lists list) {
        List<Movie> movies = list.getFilmographyList().stream()
                .filter(f -> f instanceof Movie)
                .map(f -> (Movie) f)
                .collect(Collectors.toList());

        if (movies.isEmpty())
            return 0;

        return movies.stream()
                .mapToDouble(Movie::getMovieDuration)
                .average()
                .orElse(0);
    }

    // Helper: average series seasons
    private int getAverageSeriesSeasons(Lists list) {
        List<Serie> series = list.getFilmographyList().stream()
                .filter(f -> f instanceof Serie)
                .map(f -> (Serie) f)
                .collect(Collectors.toList());

        if (series.isEmpty())
            return 0;

        return (int) series.stream()
                .mapToInt(Serie::getSerieDuration)
                .average()
                .orElse(0);
    }

    public List<Lists> findAllSystemLists() {
        return listsRepository.findByListOwnerIsNull();
    }

    public List<Lists> findAllUserList() {
        return listsRepository.findAll().stream()
                .filter(l -> l.getListOwner() != null && l.getListOwner().getAccountRole() != Account.Role.ADMIN)
                .toList();
    }

    public List<Lists> findAllListsByAuthor(Account user) {
        boolean isAdmin = user.getAccountRole() == Account.Role.ADMIN;
        if (isAdmin) {
            return listsRepository.findAll().stream().filter(l -> l.getListOwner() == null).toList();
        } else {
            return listsRepository.findAll().stream().filter(l -> l.getListOwner() == user).toList();
        }
    }

    // Add filmography to system lists that match its genres
    public void addMovieToSystemLists(Movie movie) {
        List<Lists> systemLists = findAllSystemLists();
        for (Lists list : systemLists) {
            if (list.getListName().endsWith("- Series")) continue;
            boolean matches = movie.getFilmographyGenres().stream()
                    .anyMatch(g -> formatGenre(g.getGenres()).equals(list.getListName()));
            if (matches && !list.getFilmographyList().contains(movie)) {
                list.getFilmographyList().add(movie);
                save(list);
            }
        }
    }

    public void addSeriesToSystemLists(Serie serie) {
        List<Lists> systemLists = findAllSystemLists();
        for (Lists list : systemLists) {
            if (!list.getListName().endsWith("- Series")) continue;
            String listGenre = list.getListName().replace(" - Series", "");
            boolean matches = serie.getFilmographyGenres().stream()
                    .anyMatch(g -> formatGenre(g.getGenres()).equals(listGenre));
            if (matches && !list.getFilmographyList().contains(serie)) {
                list.getFilmographyList().add(serie);
                save(list);
            }
        }
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
    public List<Map<String, Object>> getMovieSections() {
        List<Map<String, Object>> movieSections = new ArrayList<>();
        for (Lists list : findAllSystemLists()) {
            if (list.getType().equals(Lists.Types.MOVIE)) {
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
        }
        return movieSections;
    }
    public List<Map<String, Object>> getSeriesSections() {
        List<Map<String, Object>> seriesSections = new ArrayList<>();
        for (Lists list : findAllSystemLists()) {
            if (list.getType().equals(Lists.Types.SERIE)) {
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
        }
        return seriesSections;
    }
    public List<Map<String, Object>> getUserListsWithCheck(Account currentUser, Filmography filmography) {
        List<Lists> listsToShow;
        if (currentUser.getAccountRole() == Account.Role.ADMIN) {
            listsToShow = findAllSystemLists();
        } else {
            listsToShow = currentUser.getAccountLists();
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Lists list : listsToShow) {
            Map<String, Object> listMap = new HashMap<>();
            listMap.put("listsId", list.getListsId());
            listMap.put("listName", list.getListName());
            listMap.put("checked", list.getFilmographyList().contains(filmography));
            result.add(listMap);
        }
        return result;
    }
    public void updateFilmographyInUserLists(@NonNull Long filmographyId, List<Long> checkedIds, String userEmail) {
        Filmography filmography = filmographyRepository.findById(filmographyId).orElseThrow();
        Account currentUser = accountService.findByEmail(userEmail);

        List<Lists> listsToUpdate;
        if (currentUser.getAccountRole() == Account.Role.ADMIN) {
            listsToUpdate = findAllSystemLists();
        } else {
            listsToUpdate = currentUser.getAccountLists();
        }

        for (Lists list : listsToUpdate) {
            boolean isChecked = checkedIds != null && checkedIds.contains(list.getListsId());
            boolean alreadyContains = list.getFilmographyList().contains(filmography);

            if (isChecked && !alreadyContains) {
                list.getFilmographyList().add(filmography);
                save(list);
            } else if (!isChecked && alreadyContains) {
                list.getFilmographyList().remove(filmography);
                save(list);
            }
        }
    }

    public List<Map<String, Object>> getAllListSections() {
        List<Map<String, Object>> sections = new ArrayList<>();

        sections.add(Map.of("sectionTitle", "Listas Mejor Valoradas",   "lists", getBestRatedLists()));
        sections.add(Map.of("sectionTitle", "Listas Peor Valoradas",    "lists", getWorstRatedLists()));
        sections.add(Map.of("sectionTitle", "Listas Más Largas",        "lists", getLongestLists()));
        sections.add(Map.of("sectionTitle", "Listas Películas Más Largas", "lists", getLongestMoviesLists()));
        sections.add(Map.of("sectionTitle", "Listas Series con Más Temporadas", "lists", getSeriesWithMostSeasons()));

        return sections;
    }

}