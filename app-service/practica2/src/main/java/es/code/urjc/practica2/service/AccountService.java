package es.code.urjc.practica2.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.repository.AccountRepository;
import java.util.Objects;

import es.code.urjc.practica2.repository.ListsRepository;
import es.code.urjc.practica2.repository.ReviewRepository;
import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Image;
import es.code.urjc.practica2.model.Lists;
import es.code.urjc.practica2.model.Review;

@Service
public class AccountService {
    @Autowired private AccountRepository accountRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private ListsRepository listsRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ImageService imageService;
    @Autowired private EmailClientService emailClientService;

    public Account loginAccount(String email, String password) {
        // 1. Search for the account by email
        Account account = findByEmail(email);

        // 2. Compare the provided password with the stored hashed password
        if (account != null && passwordEncoder.matches(password, account.getAccountPassword())) {
            return account;
        }

        return null; // Authentication failed
    }

    public void registerAccount(String email, String name, String password, LocalDate birthDate){
        String encodedPassword = passwordEncoder.encode(password);
        Account newAccount = new Account(name, birthDate, email, Account.Role.USER, encodedPassword);

        try {
            Resource image = new ClassPathResource("/images/perfilNoReg.jpg");
            Image avatar = imageService.createImage(image.getInputStream());
            newAccount.setAccountAvatar(avatar);
        } catch (Exception e) {
            e.printStackTrace();
        }

        save(newAccount);
        emailClientService.sendMail(email,
                "Bienvenido a Palomix",
                "¡Hola! <br> Tu cuenta ha sido <b>creada con éxito</b>. A partir de ahora, podrás calificar todas las series y películas de nuestro catálogo, al igual, de crear listas con la filmografía que quieras. \nTe esperamos.");
    }

    public boolean existsAccountEmail(String email) {
        return accountRepository.existsByAccountEmail(email);
    }

    public Account updateAccount(Account old, Account young){

        if (young.getAccountName() != null) {
            old.setAccountName(young.getAccountName());
        }
        if (young.getAccountEmail() != null) {
            old.setAccountEmail(young.getAccountEmail());
        }
        if(young.getAccountBirthDate()!=null){
            old.setAccountBirthDate(young.getAccountBirthDate());
        }
        return old;
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

    public Page<Account> findAllPage(Pageable pageable) {
        return accountRepository.findAllPage(pageable);
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
