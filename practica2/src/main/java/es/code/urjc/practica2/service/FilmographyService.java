package es.code.urjc.practica2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.repository.FilmographyRepository;

@Service
public class FilmographyService {
    @Autowired
    private FilmographyRepository filmographyRepository;
    
}
