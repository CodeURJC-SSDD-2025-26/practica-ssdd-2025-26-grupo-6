package es.code.urjc.practica2.model;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

@Entity
//This way we can have a table for movies and another for series, but they will share the common attributes in the filmography table
@Inheritance(strategy = InheritanceType.JOINED) 
public class Filmography {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long filmographyId;

    private String filmographyName;
    private float filmographyAverageStars;
    @Column(columnDefinition = "TEXT")
    private String filmographySynopsis;
    private int filmographyYear;
    private String filmographyTrailerUrl;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Image filmographyImage;

    //We can have more than one platform for each filmography, so we use a list of platforms the rest is for SQL
    @Enumerated(EnumType.STRING)
    @ElementCollection
    @CollectionTable(name = "filmography_platforms", joinColumns = @JoinColumn(name = "filmography_id"))
    @Column(name = "platform")
    private List<Platforms> filmographyPlatforms = new ArrayList<>();

    //A filmography can have only one director but a director can have many filmographies
    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director filmographyDirector;

    //A filmography can have many genres and a genre can be in many filmographies
    @ManyToMany
    @JoinTable(
        name = "filmography_genres",
        joinColumns = @JoinColumn(name = "filmography_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> filmographyGenres = new ArrayList<>();

    //A filmography can have many reviews but a review can only be for one filmography, if a filmography is deleted, its reviews are also deleted
    @OneToMany(mappedBy = "filmography", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> filmographyReviews = new ArrayList<>();

    public enum Platforms{
        DISNEY,
        NETFLIX,
        PRIMEVIDEO,
        HBOMAX,
        MOVISTARPLUS
    }

    public Filmography() {} //Default constructor for JPA

    public Filmography(String filmographyName, float filmographyAverageStars, String filmographySynopsis, int filmographyYear, Director filmographyDirector, String filmographyTrailerUrl) {
        this.filmographyName = filmographyName;
        this.filmographyAverageStars = filmographyAverageStars;
        this.filmographySynopsis = filmographySynopsis;
        this.filmographyYear = filmographyYear;
        this.filmographyDirector = filmographyDirector;
        this.filmographyTrailerUrl = filmographyTrailerUrl;
    }

    // Recalculating average rating when reviews change
    public void updateAverageStars() {
        if (this.filmographyReviews == null || this.filmographyReviews.isEmpty()) {
            this.filmographyAverageStars = 0f;
        } else {
            float sum = 0;
            for (Review r : filmographyReviews) {
                sum += r.getReviewStars();
            }
            this.filmographyAverageStars = sum / filmographyReviews.size();
        }
    }

    public String getDirectorName() {
        return filmographyDirector != null ? filmographyDirector.getDirectorName() : "";
    }
    
    // Getters and setters
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

    public List<Platforms> getFilmographyPlatforms() {
        return filmographyPlatforms;
    }

    public void setFilmographyPlatforms(List<Platforms> filmographyPlatforms) {
        this.filmographyPlatforms = filmographyPlatforms;
    }

    public Director getFilmographyDirector() {
        return filmographyDirector;
    }

    public void setFilmographyDirector(Director filmographyDirector) {
        this.filmographyDirector = filmographyDirector;
    }

    public List<Genre> getFilmographyGenres() {
        return filmographyGenres;
    }

    public void setFilmographyGenres(List<Genre> filmographyGenres) {
        this.filmographyGenres = filmographyGenres;
    }

    public List<Review> getFilmographyReviews() {
        return filmographyReviews;
    }

    public void setFilmographyReviews(List<Review> filmographyReviews) {
        this.filmographyReviews = filmographyReviews;
    }

    public String getFilmographyTrailerUrl() {
        return filmographyTrailerUrl;
    }

    public void setFilmographyTrailerUrl(String filmographyTrailerUrl) {
        this.filmographyTrailerUrl = filmographyTrailerUrl;
    }

    public Image getFilmographyImage() {
        return filmographyImage;
    }

    public void setFilmographyImage(Image filmographyImage) {
        this.filmographyImage = filmographyImage;
    }

    public String getFilmographyGenre() {
        if (filmographyGenres == null || filmographyGenres.isEmpty()) {
            return null;
        }
        return filmographyGenres.get(0).getGenres().name();
    }

    public String getFilmographyImageUrl() {
        if (filmographyImage == null || filmographyImage.getImageId() == null) {
            return null;
        }
        return "/img/" + filmographyImage.getImageId();
    }

}
