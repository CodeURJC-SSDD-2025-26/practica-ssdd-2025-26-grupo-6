package es.code.urjc.practica2.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        return reviewRepository.findById(Objects.requireNonNull(reviewId)).orElse(null);
    }

    public List<Review> findByAuthor(Account author) {
        return reviewRepository.findByReviewAuthor(author);
    }

    public List<Review> findAll(){
        return reviewRepository.findAll();
    }

    public String getChartData(List<Review> reviews){
        float[] starValues = { 0.5f, 1f, 1.5f, 2f, 2.5f, 3f, 3.5f, 4f, 4.5f, 5f };
        long[] counts = new long[starValues.length];

        for (int i = 0; i < starValues.length; i++) {
            final float star = starValues[i];
            counts[i] = reviews.stream()
                    .filter(r -> r.getReviewStars() != null && Float.compare(r.getReviewStars(), star) == 0).count();
        }

        return "[" + Arrays.stream(counts).mapToObj(String::valueOf).collect(Collectors.joining(",")) + "]";
    }

    public Review save(Review review) {
        Review saved = reviewRepository.save(Objects.requireNonNull(review));
        // Actualizar el promedio en la filmografía
        Filmography filmography = saved.getFilmography();
        filmography.updateAverageStars();
        filmographyRepository.save(filmography);
        return saved;
    }

    public Review update(Long reviewId, Float reviewStars, String reviewDescription) {
        Review review = reviewRepository.findById(Objects.requireNonNull(reviewId)).orElse(null);
        if (review != null) {
            review.setReviewStars(reviewStars);
            review.setReviewDescription(reviewDescription);
            return reviewRepository.save(review);
        }
        return null;
    }

    public void delete(Long reviewId) {
        Review review = reviewRepository.findById(Objects.requireNonNull(reviewId)).orElse(null);
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
}
