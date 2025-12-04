package com.example.vet.service;

import com.example.vet.repository.UserRepository;
import com.example.vet.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    // CONSTRUCTOR MANUAL
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Método que el UserController llama para el Feign Client
    public String getUsernameById(Integer userId) {
        // Busca el usuario por ID en la BD
        User user = userRepository.findById(userId) 
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            
        // El nombre es el campo que necesita el Clinic Service
        return user.getNombre(); 
    }
    
    // NOTA: Si tenías otros métodos de UserService, agrégalos aquí.
}