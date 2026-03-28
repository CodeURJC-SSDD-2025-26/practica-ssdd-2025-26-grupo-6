package es.code.urjc.practica2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.code.urjc.practica2.model.Director;

public interface DirectorRepository extends JpaRepository<Director, Long>{

}