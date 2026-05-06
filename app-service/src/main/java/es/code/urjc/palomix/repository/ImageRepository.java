package es.code.urjc.palomix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.code.urjc.palomix.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}