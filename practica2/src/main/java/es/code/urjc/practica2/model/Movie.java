package es.code.urjc.practica2.model;

import jakarta.persistence.Entity;

@Entity
public class Movie extends Filmography{
    private int movieDuration; //minutes

    public Movie(Long filmographyId, String filmographyName, float averageStars, Platforms platforms, String synopsis,
            int filmographyYear, int movieDuration) {
        super(filmographyId, filmographyName, averageStars, platforms, synopsis, filmographyYear);
        this.movieDuration = movieDuration;
    }

    public int getMovieDuration() {
        return movieDuration;
    }

    public void setMovieDuration(int movieDuration) {
        this.movieDuration = movieDuration;
    }
}
