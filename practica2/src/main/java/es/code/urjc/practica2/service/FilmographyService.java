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

    public Filmography save(Filmography filmography) {
        return filmographyRepository.save(filmography);
    }

    public Filmography findByIdWithReviews(Long id) {
    return filmographyRepository.findByIdWithReviews(id)
            .orElseThrow(() -> new RuntimeException("Filmography not found"));
}
}
