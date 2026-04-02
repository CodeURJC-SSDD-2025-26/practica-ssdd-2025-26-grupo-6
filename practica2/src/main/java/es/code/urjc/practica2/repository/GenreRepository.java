package es.code.urjc.practica2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.code.urjc.practica2.model.Genre;
import es.code.urjc.practica2.model.Lists;

public interface GenreRepository extends JpaRepository<Genre, Long>{
    
}
