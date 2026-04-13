package es.code.urjc.practica2.controller;

import java.time.LocalDate;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.EmailService;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AutenticationController {
    @Autowired
    AccountService accountService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EmailService emailService;

    @GetMapping("/login")
    public String login(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        return "login";
    }

    // restarPassword
    // 1. Acción para generar el código y enviar el correo (Se mantiene similar)
    @PostMapping("/sendRecoveryEmail")
    public String sendRecoveryEmail(@RequestParam String email, HttpSession session, Model model) {

        if (!accountService.existsAccountEmail(email)) {
            model.addAttribute("error", "El correo no está registrado.");
            System.out.println("correo no registrado con éxito."); // <--- AÑADE ESTO

            return "login";
        }

        // Generamos el código de 6 dígitos
        String recoveryCode = String.format("%06d", new Random().nextInt(1000000));

        // Guardamos en la SESIÓN el código y el email asociado
        session.setAttribute("recoveryCode", recoveryCode);
        session.setAttribute("recoveryEmail", email);
        session.setMaxInactiveInterval(300); // 5 minutos de validez

        emailService.sendMail(email, "Código de recuperación - Palomix",
                "<h3>Tu código de recuperación es:</h3><h1>" + recoveryCode + "</h1>" +
                        "<p>Introduce este código junto a tu nueva contraseña en la web.</p>");
                        
        model.addAttribute("message", "Código enviado con éxito.");
        System.out.println("Código enviado con éxito."); // <--- AÑADE ESTO
        return "login";
    }

    // 2. Acción que VALIDA el código y CAMBIA la contraseña a la vez
    @PostMapping("/restartPassword")
    public String restartPassword(@RequestParam String email,
            @RequestParam String code,
            @RequestParam String newPassword,
            HttpSession session,
            Model model) {

        String sessionCode = (String) session.getAttribute("recoveryCode");
        String sessionEmail = (String) session.getAttribute("recoveryEmail");

        // Validamos: 1. Que haya código en sesión, 2. Que coincida el código, 3. Que
        // coincida el email
        if (sessionCode != null && sessionCode.equals(code) && sessionEmail.equals(email)) {

            // Buscamos la cuenta
            Account account = accountService.findByEmail(email);
            if (account != null) {
                // Encriptamos y guardamos la nueva contraseña
                account.setAccountPassword(passwordEncoder.encode(newPassword));
                accountService.save(account);

                // Limpiamos la sesión por seguridad
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
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam(required = false) String role) {

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

        Account.Role userRole = (role != null) ? Account.Role.ADMIN : Account.Role.USER;

        String encodedPassword = passwordEncoder.encode(password);

        Account newAccount = new Account(name, birthDate, email, userRole, encodedPassword);
        accountService.save(newAccount);

        emailService.sendMail(email,
                "Bienvenido a Palomix",
                "¡Hola! <br> Tu cuenta ha sido <b>creada con éxito</b>. A partir de ahora, podrás calificar todas las series y películas de nuestro catálogo, al igual, de crear listas con la filmografía que quieras. \nTe esperamos.");

        return "redirect:/login";
    }

}