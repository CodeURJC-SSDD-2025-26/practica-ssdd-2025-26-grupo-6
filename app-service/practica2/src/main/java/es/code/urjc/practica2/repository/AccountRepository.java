package es.code.urjc.practica2.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.code.urjc.practica2.model.Account;


public interface AccountRepository extends JpaRepository<Account, Long>{
    Optional<Account> findByAccountEmail(String accountEmail);
    Optional<Account> findByAccountName(String accountName);
    boolean existsByAccountEmail(String accountEmail);
    boolean existsByAccountName(String accountName);

    @Query("SELECT f FROM Account f")
    Page<Account> findAllPage(Pageable pageable);
}
