package es.code.urjc.practica2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.code.urjc.practica2.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}