package es.code.urjc.palomix.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import es.code.urjc.palomix.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long>{
    public Optional<Genre> findByGenres(Genre.Genres genres);
}
