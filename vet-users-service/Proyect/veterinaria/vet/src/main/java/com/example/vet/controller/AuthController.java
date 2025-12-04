package com.example.vet.controller;

import com.example.vet.dto.RegisterAdminDTO;
import com.example.vet.dto.UserLoginRequest;
import com.example.vet.dto.UserLoginResponse;
import com.example.vet.dto.UserResponse;
import com.example.vet.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // CONSTRUCTOR MANUAL (Sin Lombok)
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody RegisterAdminDTO request // Usamos el DTO correcto
    ) {
        // Llamamos al método "register" que SÍ existe en el servicio
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> authenticate(
            @RequestBody UserLoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }
}