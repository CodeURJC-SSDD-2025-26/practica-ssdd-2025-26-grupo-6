package es.code.urjc.practica2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Genre;
import es.code.urjc.practica2.model.Genre.Genres;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Serie;

public interface FilmographyRepository extends JpaRepository<Filmography, Long>{
    @Query("SELECT f FROM Movie f")
    List<Movie> findAllMovies();

    @Query("SELECT s FROM Serie s")
    List<Serie> findAllSeries();

    List<Filmography> findByFilmographyGenres_Genres(Genres genre);
    @Query("SELECT m FROM Movie m ORDER BY m.filmographyYear DESC")
    List<Movie> findTop10MoviesByYear();


}
