package es.code.urjc.practica2.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Lists;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.repository.ListsRepository;

@Service
public class ListsService {
    @Autowired
    private ListsRepository listsRepository;

    public List<Lists> findByOwner(Account owner) {
        return listsRepository.findByListOwner(owner);
    }

    public Lists findById(Long id) {
        return listsRepository.findById(id).orElse(null);
    }

    public Lists save(Lists list) {
        return listsRepository.save(list);
    }

    public void delete(Long id) {
        listsRepository.deleteById(id);
    }

   // 1. Best rated lists
    public List<Lists> getBestRatedLists() {
        return listsRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(this::getListAverageRating).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    // 2. Worst rated lists
    public List<Lists> getWorstRatedLists() {
        return listsRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(this::getListAverageRating))
                .limit(10)
                .collect(Collectors.toList());
    }

    // 3. Longest lists (most items)
    public List<Lists> getLongestLists() {
        return listsRepository.findAll().stream()
                .sorted((a, b) -> Integer.compare(
                        b.getFilmographyList().size(),
                        a.getFilmographyList().size()))
                .limit(10)
                .collect(Collectors.toList());
    }

    // 4. Lists with longest movies (average duration)
    public List<Lists> getLongestMoviesLists() {
        return listsRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(this::getAverageMovieDuration).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    // 5. Lists with series with most seasons
    public List<Lists> getSeriesWithMostSeasons() {
        return listsRepository.findAll().stream()
                .sorted(Comparator.comparingInt(this::getAverageSeriesSeasons).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    // Helper: average rating
    private double getListAverageRating(Lists list) {
        List<Review> allReviews = list.getFilmographyList().stream()
                .flatMap(f -> f.getFilmographyReviews().stream())
                .collect(Collectors.toList());

        if (allReviews.isEmpty()) return 0;

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

        if (movies.isEmpty()) return 0;

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

        if (series.isEmpty()) return 0;

        return (int) series.stream()
                .mapToInt(Serie::getSerieDuration)
                .average()
                .orElse(0);
    }
    public List<Lists> findAllSistemLists(){
        return listsRepository.findAll().stream().filter(l -> l.getListOwner() == null).toList();
    }
    public List<Lists> findAllUserList(){
       return listsRepository.findAll().stream().filter(l -> l.getListOwner() != null).toList();
    }
}
