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
    import es.code.urjc.practica2.service.EmailService;

    @Controller
    public class AutenticationController {
        @Autowired private AccountService accountService;
        @Autowired private PasswordEncoder passwordEncoder;
        @Autowired private EmailService emailService;

        @GetMapping("/login")
        public String login(Model model, @RequestParam(value = "error", required = false) String error) {
            if (error != null) {
                model.addAttribute("loginError", true);
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