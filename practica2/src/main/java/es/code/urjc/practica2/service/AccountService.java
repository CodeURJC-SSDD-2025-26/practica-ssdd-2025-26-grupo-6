package es.code.urjc.practica2.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.repository.AccountRepository;
import es.code.urjc.practica2.model.Account;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account loginAccount(String email, String password) {
        Optional<Account> account = accountRepository.findByAccountEmail(email);

        if (account.isPresent() && account.get().getAccountPassword().equals(email)) {
            return account.get();
        }
        return null;
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
        // Temporary: return alice from the database until Spring Security is implemented
        return accountRepository.findByAccountName("alice")
                .orElseThrow(() -> new RuntimeException("Default user not found"));
    }
}
