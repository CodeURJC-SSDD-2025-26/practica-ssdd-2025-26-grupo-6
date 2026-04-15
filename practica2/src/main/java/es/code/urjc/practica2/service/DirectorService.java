package es.code.urjc.practica2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.model.Director;
import es.code.urjc.practica2.repository.DirectorRepository;


@Service
public class DirectorService {
    @Autowired DirectorRepository directorRepository;

    public Director getDirectorByName(String name){
        return directorRepository.findByDirectorName(name).orElseGet(() -> directorRepository.save(new Director(name, "")));
    }
    public List<Director> findAll(){
        return directorRepository.findAll();
    }
}
