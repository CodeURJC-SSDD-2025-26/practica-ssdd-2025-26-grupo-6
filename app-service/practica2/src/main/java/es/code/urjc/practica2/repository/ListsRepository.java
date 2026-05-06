package es.code.urjc.practica2.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Lists;

public interface ListsRepository extends JpaRepository<Lists, Long> {
    List<Lists> findByListOwner(Account listOwner);
    List<Lists> findByListOwnerIsNull();

    @Query("SELECT l FROM Lists l")
    Page<Lists> findAllPage(Pageable pageable);



   @Query("SELECT l FROM Lists l WHERE l.listOwner = :user")
    Page<Lists> findAllByAccount(@Param("user") Account user, Pageable pageable);

    Page<Lists> findByListOwnerIsNull(Pageable pageable);

}
