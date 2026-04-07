package es.code.urjc.practica2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Lists;

public interface ListsRepository extends JpaRepository<Lists, Long> {
    List<Lists> findByListOwner(Account listOwner);
}
