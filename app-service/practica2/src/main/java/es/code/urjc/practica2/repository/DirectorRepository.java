package es.code.urjc.practica2.repository;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.code.urjc.practica2.model.Director;

public interface DirectorRepository extends JpaRepository<Director, Long>{
    public Optional<Director> findByDirectorName(String directorName);

    @Query("SELECT f FROM Director f")
    public Page<Director> findAllPage(Pageable pageable);
}