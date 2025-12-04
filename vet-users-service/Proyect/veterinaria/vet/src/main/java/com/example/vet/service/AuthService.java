package com.example.vet.service;

import com.example.vet.dto.RegisterAdminDTO; 
import com.example.vet.dto.UserLoginRequest;
import com.example.vet.dto.UserLoginResponse;
import com.example.vet.dto.UserResponse;
import com.example.vet.jwt.JwtService;
import com.example.vet.model.Role;
import com.example.vet.model.User;
import com.example.vet.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // 1. CONSTRUCTOR MANUAL
    public AuthService(UserRepository userRepository, 
                       PasswordEncoder passwordEncoder, 
                       JwtService jwtService, 
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // 2. REGISTRO SIN BUILDER
    public UserResponse register(RegisterAdminDTO request) {
        // Creamos el usuario manualmente
        User user = new User();
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // CORRECCIÓN FINAL: Asignar el rol que viene en el DTO
        user.setRole(request.getRole()); // <--- ESTO ARREGLA EL ROL

        userRepository.save(user);
        
        var jwtToken = jwtService.generateToken(user);
        
        // Respuesta manual
        UserResponse response = new UserResponse();
        response.setToken(jwtToken);
        return response;
    }

    // 3. LOGIN SIN BUILDER
    public UserLoginResponse login(UserLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
                
        var jwtToken = jwtService.generateToken(user);
        
        // Respuesta manual
        UserLoginResponse response = new UserLoginResponse();
        response.setToken(jwtToken);
        return response;
    }
}