package es.code.urjc.palomix.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import es.code.urjc.palomix.model.Director;
import es.code.urjc.palomix.model.Filmography;
import es.code.urjc.palomix.repository.DirectorRepository;
import es.code.urjc.palomix.repository.FilmographyRepository;


@Service
public class DirectorService {
    @Autowired DirectorRepository directorRepository;
    @Autowired FilmographyRepository filmographyRepository;
    
    @Autowired
    private FilmographyService filmographyService;

    public Director getDirectorByName(String name){
        return directorRepository.findByDirectorName(name).orElseGet(() -> directorRepository.save(new Director(name, "")));
    }

    public Director getDirectorByNamePage(String name){
        return directorRepository.findByDirectorName(name).orElse(null);
    }

    public List<Director> findAll(){
        return directorRepository.findAll();
    }

    public Page<Director> findAllPage(Pageable pageable){
        return directorRepository.findAllPage(pageable);
    }
    
    public Director findById(Long id){
        if(id != null){
            return directorRepository.findById(id).orElse(null);
        }
        return null;
    }

    public List<Director> findAllSorted() {
        return directorRepository.findAll(Sort.by(Sort.Direction.ASC, "directorName"));
    }

    public Director save(Director director){
        if(director != null){
            directorRepository.save(director);
        }
        return director;
    }

    public void delete(Long id){
        if(id != null){
            Director director= directorRepository.findById(id).orElse(null);

            if(director != null){
                List<Filmography> filmographies= filmographyRepository.findByFilmographyDirector(director);

                for(Filmography f : filmographies){
                    f.setFilmographyDirector(null);
                    filmographyRepository.save(f);
                }
                directorRepository.delete(director);
            }
        }
    }

    public void deleteWithFilmographies(Long id) {
        Director director = findById(id);
        List<Filmography> filmographies = filmographyService.findByDirector(director);
        for (Filmography f : filmographies) {
            f.setFilmographyDirector(null);
            filmographyService.save(f);
        }
        delete(id);
    }
}
