package com.example.vet.controller;

import com.example.vet.dto.UserLoginRequest;
import com.example.vet.dto.UserLoginResponse;
import com.example.vet.dto.UserRequest;
import com.example.vet.dto.UserResponse;
import com.example.vet.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest request) {
        UserLoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        
        // --- CORRECCIÓN PARA EL VIDEO ---
        // Solo asignamos ROLE_CLIENT si tú NO especificaste un rol en Postman.
        if (request.getRole() == null || request.getRole().isEmpty()) {
            request.setRole("ROLE_CLIENT");
        }
        // Si en Postman pusiste "ROLE_ADMIN", el 'if' de arriba se salta 
        // y se respeta tu decisión.

        UserResponse response = authService.registerUser(request);
        return ResponseEntity.ok(response);
    }
}