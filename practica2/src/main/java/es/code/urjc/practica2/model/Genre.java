package es.code.urjc.practica2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Genre {
    @Id
    private Long genreId;

    private Genres genres;

    public enum Genres {
        ACCION,
        ROMANCE,
        AVENTURA,
        MIEDO,
        CIENCIA_FICCION,
        SUSPENSE,
        DRAMA,
        COMEDIA
    }

    public Genre(Long genreId, Genres genres) {
        this.genreId = genreId;
        this.genres = genres;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public Genres getGenres() {
        return genres;
    }

    public void setGenres(Genres genres) {
        this.genres = genres;
    }
}
