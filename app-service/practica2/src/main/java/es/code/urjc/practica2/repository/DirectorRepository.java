package es.code.urjc.practica2.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import es.code.urjc.practica2.model.Director;

public interface DirectorRepository extends JpaRepository<Director, Long>{
    public Optional<Director> findByDirectorName(String directorName);
}