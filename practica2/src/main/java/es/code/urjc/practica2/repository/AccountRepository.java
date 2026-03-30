package es.code.urjc.practica2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.code.urjc.practica2.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{
    Optional<Account> findByAccountEmail(String accountEmail);
         
    boolean existsByAccountEmail(String accountEmail);
}
