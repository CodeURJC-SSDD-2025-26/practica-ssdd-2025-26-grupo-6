package es.code.urjc.practica2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.repository.ReviewRepository;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public Review update(Long reviewId, Float reviewStars, String reviewDescription) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null) {
            review.setReviewStars(reviewStars);
            review.setReviewDescription(reviewDescription);
            return reviewRepository.save(review);
        }
        return null;
    }

    public void delete(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public List<Review> findByAuthor(Account author) {
        return reviewRepository.findByReviewAuthor(author);
    }
}
