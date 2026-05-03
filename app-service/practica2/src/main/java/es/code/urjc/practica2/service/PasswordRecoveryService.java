package es.code.urjc.practica2.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import es.code.urjc.practica2.model.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;



@Service
public class PasswordRecoveryService {
    @Autowired EmailService emailService;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired AccountService accountService;

    public String generateAndSendRecoveryCode(String email){
        String recoveryCode = String.format("%06d", new Random().nextInt(1000000));
        emailService.sendMail(email, "Código de recuperación - Palomix",
                "<h3>Tu código de recuperación es:</h3><h1>" + recoveryCode + "</h1>" +
                        "<p>Introduce este código junto a tu nueva contraseña en la web.</p>");
        return recoveryCode;
    }

    public boolean verifyAndResetPassword(String sessionCode, String sessionEmail, String code, String email, String newPassword){
         if (sessionCode == null || !sessionCode.equals(code) || !sessionEmail.equals(email)) {
            return false;
         }

        Account account = accountService.findByEmail(email);
        if (account == null) return false;        

        account.setAccountPassword(passwordEncoder.encode(newPassword));
        accountService.save(account);

        return true;
    }
}
