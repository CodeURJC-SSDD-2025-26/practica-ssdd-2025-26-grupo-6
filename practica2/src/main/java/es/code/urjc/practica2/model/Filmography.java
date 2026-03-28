package es.code.urjc.practica2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Filmography {
    @Id
    private Long filmographyId;

    private String filmographyName;
    private float filmographyAverageStars;
    private Platforms filmographyPlatforms;
    private String filmographySynopsis;
    private int filmographyYear;

    public enum Platforms{
        DISNEY,
        NETFLIX,
        PRIMEVIDEO,
        HBOMAX,
        MOVISTARPLUS
    }

    public Filmography(Long filmographyId, String filmographyName, float filmographyAverageStars,
            Platforms filmographyPlatforms, String filmographySynopsis, int filmographyYear) {
        this.filmographyId = filmographyId;
        this.filmographyName = filmographyName;
        this.filmographyAverageStars = filmographyAverageStars;
        this.filmographyPlatforms = filmographyPlatforms;
        this.filmographySynopsis = filmographySynopsis;
        this.filmographyYear = filmographyYear;
    }

    public Long getFilmographyId() {
        return filmographyId;
    }

    public void setFilmographyId(Long filmographyId) {
        this.filmographyId = filmographyId;
    }

    public String getFilmographyName() {
        return filmographyName;
    }

    public void setFilmographyName(String filmographyName) {
        this.filmographyName = filmographyName;
    }

    public float getFilmographyAverageStars() {
        return filmographyAverageStars;
    }

    public void setFilmographyAverageStars(float filmographyAverageStars) {
        this.filmographyAverageStars = filmographyAverageStars;
    }

    public Platforms getFilmographyPlatforms() {
        return filmographyPlatforms;
    }

    public void setFilmographyPlatforms(Platforms filmographyPlatforms) {
        this.filmographyPlatforms = filmographyPlatforms;
    }

    public String getFilmographySynopsis() {
        return filmographySynopsis;
    }

    public void setFilmographySynopsis(String filmographySynopsis) {
        this.filmographySynopsis = filmographySynopsis;
    }

    public int getFilmographyYear() {
        return filmographyYear;
    }

    public void setFilmographyYear(int filmographyYear) {
        this.filmographyYear = filmographyYear;
    }

    
}
