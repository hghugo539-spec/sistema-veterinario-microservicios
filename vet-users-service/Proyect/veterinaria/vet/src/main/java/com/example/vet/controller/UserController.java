package com.example.vet.controller;

import com.example.vet.service.UserService;
// BORRA CUALQUIER IMPORT DE LOMBOK AQUÍ
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
// BORRA @RequiredArgsConstructor si lo tenías
public class UserController {

    private final UserService userService;

    // CONSTRUCTOR MANUAL (Solución al error de inicialización)
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}/name")
    public ResponseEntity<String> getUsernameForClient(@PathVariable Integer userId) {
        try {
            // Llama al servicio para obtener el nombre
            String username = userService.getUsernameById(userId); 
            return ResponseEntity.ok(username);
        } catch (RuntimeException e) {
            // Devuelve NOT FOUND si el ID no existe
            return ResponseEntity.notFound().build(); 
        }
    }
}