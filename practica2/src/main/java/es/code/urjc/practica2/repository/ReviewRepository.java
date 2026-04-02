package es.code.urjc.practica2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
    
}
