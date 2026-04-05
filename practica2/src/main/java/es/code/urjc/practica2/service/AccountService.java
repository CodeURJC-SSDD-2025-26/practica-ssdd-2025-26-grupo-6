package es.code.urjc.practica2.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.repository.AccountRepository;
import es.code.urjc.practica2.model.Account;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account loginAccount(String email, String password) {
        // 1. Buscamos al usuario solo por email
        Account account = accountRepository.findByAccountEmail(email).orElse(null);

        // 2. Comparamos la contraseña usando matches()
        if (account != null && passwordEncoder.matches(password, account.getAccountPassword())) {
            return account;
        }

        return null; // Fallo de autenticación
    }

    public boolean existsAccountEmail(String email) {
        return accountRepository.existsByAccountEmail(email);
    }

    public boolean existsAccountName(String name) {
        return accountRepository.existsByAccountName(name);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Account getCurrentUser() {
        // When bbdd is implemented, we will get the current user from the session and
        // return it, for now we return a dummy user
        return new Account("dummy", LocalDate.now(), "dummy", Account.Role.USER, "dummy");
    }
}
