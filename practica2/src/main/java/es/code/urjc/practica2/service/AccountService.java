package es.code.urjc.practica2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.repository.AccountRepository;
import java.util.Objects;

import es.code.urjc.practica2.repository.ListsRepository;
import es.code.urjc.practica2.repository.ReviewRepository;
import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Lists;
import es.code.urjc.practica2.model.Review;

@Service
public class AccountService {
    @Autowired private AccountRepository accountRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private ListsRepository listsRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public Account loginAccount(String email, String password) {
        // 1. Search for the account by email
        Account account = findByEmail(email);

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

    public Account findById(Long id) {
        return accountRepository.findById(Objects.requireNonNull(id)).orElse(null);
    }

    public Account findByName(String name) {
        return accountRepository.findByAccountName(name).orElse(null);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findByEmail(String email) {
        return accountRepository.findByAccountEmail(email).orElse(null);
    }

    public Account save(Account account) {
        return accountRepository.save(Objects.requireNonNull(account));
    }

    public void delete(Long id) {
        Account user = accountRepository.findById(Objects.requireNonNull(id)).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Review> reviews = reviewRepository.findByReviewAuthor(user);
        if (reviews != null) {
            reviewRepository.deleteAll(reviews);
        }

        List<Lists> lists = listsRepository.findByListOwner(user);
        if (lists != null) {
            listsRepository.deleteAll(lists);
        }

        accountRepository.deleteById(id);
    }
}
