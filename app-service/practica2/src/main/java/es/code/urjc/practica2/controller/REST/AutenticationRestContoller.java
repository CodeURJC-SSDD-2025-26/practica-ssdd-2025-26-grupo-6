package es.code.urjc.practica2.controller.rest;

import es.code.urjc.practica2.security.jwt.AuthResponse;
import es.code.urjc.practica2.security.jwt.AuthResponse.Status;
import es.code.urjc.practica2.security.jwt.LoginRequest;
import es.code.urjc.practica2.security.jwt.AccountLoginService;
import es.code.urjc.practica2.service.AccountService;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AutenticationRestContoller {

    @Autowired private AccountLoginService AccountLoginService;
    @Autowired private AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {

        return AccountLoginService.login(response, loginRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue(name = "RefreshToken", required = false) String refreshToken,
            HttpServletResponse response) {

        return AccountLoginService.refresh(response, refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletResponse response) {
        return ResponseEntity.ok(new AuthResponse(Status.SUCCESS, AccountLoginService.logout(response)));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        if (accountService.existsAccountEmail(signUpRequest.accountEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        if (accountService.existsAccountName(signUpRequest.accountName())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        if (!signUpRequest.accountPassword().equals(signUpRequest.confirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        accountService.registerAccount(
            signUpRequest.accountEmail(),
            signUpRequest.accountName(),
            signUpRequest.accountPassword(),
            signUpRequest.accountBirthDate()
        );

        return ResponseEntity.status(201).body("User registered successfully");
    }


    public record SignUpRequest(
        String accountName,
        String accountEmail,
        String accountPassword,
        String confirmPassword,
        LocalDate accountBirthDate
    ) {}
}