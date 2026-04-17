package es.code.urjc.practica2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.model.Director;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.repository.DirectorRepository;
import es.code.urjc.practica2.repository.FilmographyRepository;


@Service
public class DirectorService {
    @Autowired DirectorRepository directorRepository;

    @Autowired FilmographyRepository filmographyRepository;

    public Director getDirectorByName(String name){
        return directorRepository.findByDirectorName(name).orElseGet(() -> directorRepository.save(new Director(name, "")));
    }
    public List<Director> findAll(){
        return directorRepository.findAll();
    }
    public void delete(Long id){
        Director director= directorRepository.findById(id).orElse(null);


        List<Filmography> filmographies= filmographyRepository.findByFilmographyDirector(director);

        for(Filmography f : filmographies){
            f.setFilmographyDirector(null);
            filmographyRepository.save(f);
        }
        directorRepository.delete(director);


    }
    public Director findById(Long Id){
        return directorRepository.findById(Id).orElse(null);
    }
    public void save(Director director){
        directorRepository.save(director);
    }
}
