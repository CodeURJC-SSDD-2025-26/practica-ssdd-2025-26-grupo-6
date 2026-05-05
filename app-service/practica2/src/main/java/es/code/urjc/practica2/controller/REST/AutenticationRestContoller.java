package es.code.urjc.practica2.controller.REST;

import es.code.urjc.practica2.security.jwt.AuthResponse;
import es.code.urjc.practica2.security.jwt.AuthResponse.Status;
import es.code.urjc.practica2.security.jwt.LoginRequest;
import es.code.urjc.practica2.security.jwt.AccountLoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AutenticationRestContoller {

    @Autowired
    private AccountLoginService AccountLoginService;

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
}