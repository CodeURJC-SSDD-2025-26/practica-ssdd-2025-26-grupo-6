package es.code.urjc.practica2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.repository.AccountRepository;
import java.util.Objects;
import es.code.urjc.practica2.model.Account;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account loginAccount(String email, String password) {
        // 1. Search for the account by email
        Account account = accountRepository.findByAccountEmail(email).orElse(null);

        // 2. Compare the provided password with the stored hashed password
        if (account != null && passwordEncoder.matches(password, account.getAccountPassword())) {
            return account;
        }

        return null; // Authentication failed
    }

    public boolean existsAccountEmail(String email) {
        return accountRepository.existsByAccountEmail(email);
    }

    public boolean existsAccountName(String name) {
        return accountRepository.existsByAccountName(name);
    }

    public Account save(Account account) {
        return accountRepository.save(Objects.requireNonNull(account));
    }

    public Account findById(Long id) {
        return accountRepository.findById(Objects.requireNonNull(id)).orElse(null);
    }

    public Account findByName(String name) {
        return accountRepository.findByAccountName(name).orElse(null);
    }
}
