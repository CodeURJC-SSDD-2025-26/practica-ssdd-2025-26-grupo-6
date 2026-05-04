package es.code.urjc.practica2.controller.web;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.EmailService;
import es.code.urjc.practica2.service.ImageService;
import es.code.urjc.practica2.service.PasswordRecoveryService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AutenticationController {
    @Value("${app.recovery.expiry-seconds}")
    private int recoveryExpirySeconds;

    @Autowired AccountService accountService;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired EmailService emailService;
    @Autowired ImageService imageService;
    @Autowired PasswordRecoveryService passwordRecoveryService;

    @GetMapping("/")
    public String start(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        return "login";
    }

    @GetMapping("/login")
    public String login(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        return "login";
    }

    @PostMapping("/sendRecoveryEmail")
    public String sendRecoveryEmail(@RequestParam String email, HttpSession session, Model model) {

        if (!accountService.existsAccountEmail(email)) {
            model.addAttribute("error", "El correo no está registrado.");
            return "login";
        }

        String recoveryCode = passwordRecoveryService.generateAndSendRecoveryCode(email);

        session.setAttribute("recoveryCode", recoveryCode);
        session.setAttribute("recoveryEmail", email);
        session.setMaxInactiveInterval(recoveryExpirySeconds);

        model.addAttribute("message", "Código enviado con éxito.");
        return "login";
    }

    @PostMapping("/restartPassword")
    public String restartPassword(@RequestParam String email, @RequestParam String code, @RequestParam String newPassword, 
        HttpSession session, Model model) {

        String sessionCode = (String) session.getAttribute("recoveryCode");
        String sessionEmail = (String) session.getAttribute("recoveryEmail");

        boolean success = passwordRecoveryService.verifyAndResetPassword(sessionCode, sessionEmail, sessionCode, sessionEmail, newPassword);
        
        if (success) {
            session.removeAttribute("recoveryCode"); 
            session.removeAttribute("recoveryEmail");
            model.addAttribute("message", "Contraseña actualizada correctamente.");
        } else {
            model.addAttribute("error", "El código es incorrecto, ha expirado o el email no coincide.");
        }

        return "login";
    }

    // SIGN UP METHODS
    @GetMapping("/signUp")
    public String signUp(Model model) {
        return "signUp";
    }

    @PostMapping("/signUp")
    public String postSignUp(Model model, @RequestParam String email,@RequestParam String name, @RequestParam String password,
        @RequestParam String confirmPassword, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate) {

        boolean hasError = false;

        // Check if email is taken
        if (accountService.existsAccountEmail(email)) {
            model.addAttribute("errorE", "Este correo electrónico ya está registrado.");
            hasError = true;
        }

        // Check if name is taken
        if (accountService.existsAccountName(name)) {
            model.addAttribute("errorN", "El nombre de usuario ya está en uso.");
            hasError = true;
        }

        // Check passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorP", "Las contraseñas no coinciden.");
            hasError = true;
        }

        // Validate date
        if (birthDate.isAfter(LocalDate.now())) {
            model.addAttribute("errorD", "Fecha inválida");
            hasError = true;
        }

        if (hasError) {
            return "signUp";
        }

        String encodedPassword = passwordEncoder.encode(password);
        accountService.registerAccount(email, name, encodedPassword, birthDate);

        return "redirect:/login";
    }
}