package es.code.urjc.practica2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.repository.FilmographyRepository;

@Service
public class FilmographyService {
    @Autowired
    private static FilmographyRepository filmographyRepository;

    public static Filmography findById(Long id) {
        return filmographyRepository.findById(id).orElse(null);
    }
}
