package es.code.urjc.practica2.model;

import jakarta.persistence.Entity;

@Entity
public class Serie extends Filmography{
    private int serieDuration; //number of seasons

    public Serie() {} //Default constructor for JPA

    public Serie(Long filmographyId, String filmographyName, float filmographyAverageStars, String filmographySynopsis, 
        int filmographyYear, Director filmographyDirector, String filmographyTrailerUrl, int serieDuration) {
        super(filmographyName, filmographyAverageStars, filmographySynopsis, filmographyYear, filmographyDirector, filmographyTrailerUrl);
        this.serieDuration = serieDuration;
    }

    public int getSerieDuration() {
        return serieDuration;
    }

    public void setSerieDuration(int serieDuration) {
        this.serieDuration = serieDuration;
    }
}
