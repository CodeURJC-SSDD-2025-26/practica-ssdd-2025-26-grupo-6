package es.code.urjc.practica2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Review {
    @Id
    private Long reviewId;

    private Float reviewStars;
    private String review;
    
    public Review(Long reviewId, Float stars, String review) {
        this.reviewId = reviewId;
        this.reviewStars = stars;
        this.review = review;
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

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }  
}
