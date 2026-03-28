package es.code.urjc.practica2.model;

import jakarta.persistence.Entity;

@Entity
public class Serie extends Filmography{
    private int serieDuration; //number of seasons

    public Serie(Long filmographyId, String filmographyName, float averageStars, Platforms platforms, String synopsis,
            int filmographyYear, int serieDuration) {
        super(filmographyId, filmographyName, averageStars, platforms, synopsis, filmographyYear);
        this.serieDuration = serieDuration;
    }

    public int getSerieDuration() {
        return serieDuration;
    }

    public void setSerieDuration(int serieDuration) {
        this.serieDuration = serieDuration;
    }
}
