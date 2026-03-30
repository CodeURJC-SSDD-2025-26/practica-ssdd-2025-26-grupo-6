package es.code.urjc.practica2.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AutenticationController {

    @Autowired
    AccountService accountService;

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
            session.setMaxInactiveInterval(60 * 5); // 5 minutes inactive

            Cookie cookie = new Cookie("userEmail", email);
            if (remember != null) { // If checkbox checked
                cookie.setMaxAge(7 * 24 * 60 * 60); // Leasts 7 days
            } else {
                cookie.setMaxAge(0); // If checkbox not marcked, then erase it
            }
            cookie.setPath("/"); // Available in every URL
            response.addCookie(cookie);

            return "redirect:/principal";
        }

        model.addAttribute("error", "Email o Contraseña incorrecta");
        return "login";
    }

    // SIGN UP METHODS
    @GetMapping("/signUp")
    public String signUp(Model model) {
        return "signUp";
    }

    @PostMapping("/signUp")
    public String postSignUp(Account account, @RequestParam String confirmPassword, Model model) {

        // check passwords matches
        if (!account.getAccountPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "signUp";
        }

        // check if email is taken
        if (accountService.existsAccount(account.getAccountEmail())) {
            model.addAttribute("error", "El correo ya está registrado");
            return "signUp";
        }

        // save account
        accountService.signUpAccount(account);
        return "redirect:/login";
    }

}