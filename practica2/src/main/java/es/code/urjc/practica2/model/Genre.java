package es.code.urjc.practica2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Genre {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long genreId;

    @Enumerated(EnumType.STRING)
    private Genres genres;

    public enum Genres {
        ACCION,
        ROMANCE,
        AVENTURA,
        MIEDO,
        CIENCIA_FICCION,
        SUSPENSE,
        DRAMA,
        COMEDIA,
        CRIMEN;

        public String getDisplayName() {
            String name = this.name().replace("_", " ").toLowerCase();
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
    }

    public Genre() {} //Default constructor for JPA

    public Genre(Genres genres) {
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
