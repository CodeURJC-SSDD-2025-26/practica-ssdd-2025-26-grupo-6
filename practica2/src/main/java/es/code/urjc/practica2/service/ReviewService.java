package es.code.urjc.practica2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.repository.FilmographyRepository;
import es.code.urjc.practica2.repository.ReviewRepository;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FilmographyRepository filmographyRepository;

    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    public Review save(Review review) {
        Review saved = reviewRepository.save(review);
        // Actualizar el promedio en la filmografía
        Filmography filmography = saved.getFilmography();
        filmography.updateAverageStars();
        filmographyRepository.save(filmography);
        return saved;
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
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null) {
            Filmography filmography = review.getFilmography();
            reviewRepository.deleteById(reviewId);
            // Recargar para que las reviews estén actualizadas antes de recalcular
            filmography = filmographyRepository.findByIdWithReviews(filmography.getFilmographyId())
                    .orElse(filmography);
            filmography.updateAverageStars();
            filmographyRepository.save(filmography);
        }
    }

    public List<Review> findByAuthor(Account author) {
        return reviewRepository.findByReviewAuthor(author);
    }
}
