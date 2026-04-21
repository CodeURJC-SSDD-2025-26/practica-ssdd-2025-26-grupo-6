package es.code.urjc.practica2.controller;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Image;
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.EmailService;
import es.code.urjc.practica2.service.ImageService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AutenticationController {
    @Autowired AccountService accountService;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired EmailService emailService;
    @Autowired ImageService imageService;

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
            System.out.println("correo no registrado con éxito."); // <--- AÑADE ESTO

            return "login";
        }

        String recoveryCode = String.format("%06d", new Random().nextInt(1000000));

        session.setAttribute("recoveryCode", recoveryCode);
        session.setAttribute("recoveryEmail", email);
        session.setMaxInactiveInterval(300);

        emailService.sendMail(email, "Código de recuperación - Palomix",
                "<h3>Tu código de recuperación es:</h3><h1>" + recoveryCode + "</h1>" +
                        "<p>Introduce este código junto a tu nueva contraseña en la web.</p>");

        model.addAttribute("message", "Código enviado con éxito.");
        System.out.println("Código enviado con éxito.");
        return "login";
    }

    @PostMapping("/restartPassword")
    public String restartPassword(@RequestParam String email,
            @RequestParam String code,
            @RequestParam String newPassword,
            HttpSession session,
            Model model) {

        String sessionCode = (String) session.getAttribute("recoveryCode");
        String sessionEmail = (String) session.getAttribute("recoveryEmail");

        if (sessionCode != null && sessionCode.equals(code) && sessionEmail.equals(email)) {

            Account account = accountService.findByEmail(email);
            if (account != null) {

                account.setAccountPassword(passwordEncoder.encode(newPassword));
                accountService.save(account);

                session.removeAttribute("recoveryCode");
                session.removeAttribute("recoveryEmail");

                model.addAttribute("message", "Contraseña actualizada correctamente.");
            }
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
    public String postSignUp(Model model,
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate) {

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

        Account newAccount = new Account(name, birthDate, email, Account.Role.USER, encodedPassword);
        Resource image = new ClassPathResource("/images/perfilNoReg.jpg");
       
        try {
             Image avatar = imageService.createImage(image.getInputStream());
            newAccount.setAccountAvatar(avatar);
        } catch (Exception e) {
            e.printStackTrace();
        }

        accountService.save(newAccount);

        emailService.sendMail(email,
                "Bienvenido a Palomix",
                "¡Hola! <br> Tu cuenta ha sido <b>creada con éxito</b>. A partir de ahora, podrás calificar todas las series y películas de nuestro catálogo, al igual, de crear listas con la filmografía que quieras. \nTe esperamos.");

        return "redirect:/login";
    }

}