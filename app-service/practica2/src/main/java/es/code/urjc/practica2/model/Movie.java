package es.code.urjc.practica2.model;

import jakarta.persistence.Entity;

@Entity
public class Movie extends Filmography{
    private int movieDuration; //minutes

    public Movie() {} //Default constructor for JPA

    public Movie(Long filmographyId, String filmographyName, float filmographyAverageStars, String filmographySynopsis, 
        int filmographyYear, Director filmographyDirector, String filmographyTrailerUrl, int movieDuration) {
        super(filmographyName, filmographyAverageStars, filmographySynopsis, filmographyYear, filmographyDirector, filmographyTrailerUrl);
        this.movieDuration = movieDuration;
    }

    public int getMovieDuration() {
        return movieDuration;
    }

    public void setMovieDuration(int movieDuration) {
        this.movieDuration = movieDuration;
    }
}
