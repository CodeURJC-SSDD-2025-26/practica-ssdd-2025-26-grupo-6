package es.code.urjc.practica2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Genre.Genres;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.model.Director;

public interface FilmographyRepository extends JpaRepository<Filmography, Long> {
    @Query("SELECT f FROM Movie f")
    List<Movie> findAllMovies();

    @Query("SELECT s FROM Serie s")
    List<Serie> findAllSeries();

    @Query("SELECT f FROM Filmography f LEFT JOIN FETCH f.filmographyReviews WHERE f.filmographyId = :id")
    Optional<Filmography> findByIdWithReviews(@Param("id") Long id);

    List<Filmography> findByFilmographyGenres_Genres(Genres genre);

    @Query("SELECT m FROM Movie m ORDER BY m.filmographyYear DESC")
    List<Movie> findTop10MoviesByYear();

    @Query("SELECT f FROM Filmography f WHERE LOWER(f.filmographyName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Filmography> findByTitleContaining(@Param("name") String filmographyName);

    List<Filmography> findByFilmographyDirector(Director director);

    Filmography findByFilmographyName(String filmographyName);

    @Query("SELECT m FROM Movie m")
    Page<Movie> findAllMovies(Pageable pageable);

    @Query("SELECT s FROM Serie s")
    Page<Serie> findAllSeries(Pageable pageable);
}
