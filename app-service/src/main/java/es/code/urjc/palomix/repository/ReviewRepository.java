package es.code.urjc.palomix.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.code.urjc.palomix.model.Account;
import es.code.urjc.palomix.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReviewAuthor(Account reviewAuthor);

    @Query("SELECT r FROM Review r WHERE r.reviewAuthor.accountName = :name")
    Page<Review> findByReviewAuthorName(@Param("name") String name, Pageable pageable);

    @Query("SELECT r FROM Review r")
    Page<Review> findAllPage(Pageable pageable);
}
