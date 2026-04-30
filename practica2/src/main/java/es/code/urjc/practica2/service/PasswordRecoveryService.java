package es.code.urjc.practica2.service;

import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;



@Service
public class PasswordRecoveryService {
    @Autowired EmailService emailService;
    public String generateAndSendRecoveryCode(String email){
        String recoveryCode = String.format("%06d", new Random().nextInt(1000000));
        emailService.sendMail(email, "Código de recuperación - Palomix",
                "<h3>Tu código de recuperación es:</h3><h1>" + recoveryCode + "</h1>" +
                        "<p>Introduce este código junto a tu nueva contraseña en la web.</p>");
        return recoveryCode;
    }
}
