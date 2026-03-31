package es.code.urjc.practica2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reviewId;

    private Float reviewStars;
    private String reviewDescription;

    @ManyToOne
    @JoinColumn(name = "filmography_id")
    private Filmography filmography;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account reviewAuthor;

    public Review() {} //Default constructor for JPA
    
    public Review(Float reviewStars, String reviewDescription, Account reviewAuthor, Filmography filmography) {
        this.reviewStars = reviewStars;
        this.reviewDescription = reviewDescription;
        this.reviewAuthor = reviewAuthor;
        this.filmography = filmography;
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

    public String getReviewDescription() {
        return reviewDescription;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    public Filmography getFilmography() {
        return filmography;
    }

    public void setFilmography(Filmography filmography) {
        this.filmography = filmography;
    }

    public Account getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(Account reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    
}
