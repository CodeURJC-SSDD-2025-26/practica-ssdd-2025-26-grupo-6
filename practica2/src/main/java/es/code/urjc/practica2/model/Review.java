package es.code.urjc.practica2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reviewId;

    private Float reviewStars;
    private String reviewSynopsis;

    @ManyToOne
    private Filmography filmography;

    public Review() {} //Default constructor for JPA
    
    public Review(Long reviewId, Float stars, String reviewSynopsis) {
        this.reviewId = reviewId;
        this.reviewStars = stars;
        this.reviewSynopsis = reviewSynopsis;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Float getReviewStars() {
        return reviewStars;
    }

    public void setReviewStars(Float reviewStars) {
        this.reviewStars = reviewStars;
    }

    public String getReviewSynopsis() {
        return reviewSynopsis;
    }

    public void setReviewSynopsis(String reviewSynopsis) {
        this.reviewSynopsis = reviewSynopsis;
    } 
    
    public Filmography getFilmography() {
        return filmography;
    }

    public void setFilmography(Filmography filmography) {
        this.filmography = filmography;
    }
}
