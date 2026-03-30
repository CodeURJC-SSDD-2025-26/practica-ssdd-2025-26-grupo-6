package es.code.urjc.practica2.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.repository.AccountRepository;
import es.code.urjc.practica2.model.Account;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;


    public Account loginAccount(String email, String password){
        Optional<Account> account = accountRepository.findByAccountEmail(email);

        if(account.isPresent() && account.get().getAccountPassword().equals(email)){
            return account.get();
        }
        return null;
    }

    public boolean existsAccount(String email){
        return accountRepository.existsByAccountEmail(email);
    }

    public Account signUpAccount(Account account){
        return accountRepository.save(account);
    }
}
