package es.code.urjc.practica2.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import es.code.urjc.practica2.model.Genre;
import es.code.urjc.practica2.repository.GenreRepository;

@Service
public class GenreService {
    @Autowired GenreRepository genreRepository;

    public List<Genre> getGenresByName(List<String> genreNames){
        if (genreNames == null) return new ArrayList<>();
        
    return genreNames.stream().map(name -> genreRepository.findByGenres(Genre.Genres.valueOf(name))
                    .orElseThrow(() -> new RuntimeException("Genre not found: " + name)))
            .collect(java.util.stream.Collectors.toList());
        
    }
}
