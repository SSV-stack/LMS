package in.maven.ark.lms.auth.controller;

import in.maven.ark.lms.auth.dto.LoginRequest;
import in.maven.ark.lms.auth.dto.LoginResponse;
import in.maven.ark.lms.auth.dto.RegisterRequest;
import in.maven.ark.lms.auth.service.AuthService;
import in.maven.ark.lms.common.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = authService.register(registerRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix
        String jwtToken = token.substring(7);
        LoginResponse response = authService.refreshToken(jwtToken);
        return ResponseEntity.ok(response);
    }
}
