package es.code.urjc.practica2.controller;

import java.time.LocalDate;

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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AutenticationController {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    // LOGIN METHODS
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        // Search for userEmail cookie
        String rememberedEmail = "";
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("userEmail".equals(cookie.getName())) {
                    rememberedEmail = cookie.getValue();
                }
            }
        }
        model.addAttribute("rememberedEmail", rememberedEmail);
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String remember,
            HttpSession session,
            HttpServletResponse response,
            Model model) {

        Account account = accountService.loginAccount(email, password);

        if (account != null) {
            session.setAttribute("user", account);
            session.setAttribute("userId", account.getAccountId());
            session.setAttribute("isAdmin", account.getAccountRole() == Account.Role.ADMIN);

            session.setMaxInactiveInterval(60*60);

            Cookie cookie = new Cookie("userEmail", email);
            cookie.setPath("/");
            if (remember != null) {
                cookie.setMaxAge(7 * 24 * 60 * 60);
            } else {
                cookie.setMaxAge(0);
            }
            response.addCookie(cookie);

            return "redirect:/principal";
        }

        model.addAttribute("error", "Email o Contraseña incorrecta");
        return "login";
    }

    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect: /login";
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

        return "redirect:/login";
    }

}