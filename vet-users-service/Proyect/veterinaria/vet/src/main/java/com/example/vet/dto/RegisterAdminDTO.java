package com.example.vet.dto;

import com.example.vet.model.Role; // Asegúrate de tener el enum Role

public class RegisterAdminDTO {
    private String nombre;
    private String email;
    private String password;
    private Role role; // Campo para recibir el rol (ADMIN/CLIENT)

    // Constructor vacío
    public RegisterAdminDTO() {} 

    // --- GETTERS Y SETTERS MANUALES (OBLIGATORIOS) ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Role getRole() { return role; } // <--- EL MÉTODO CLAVE
    public void setRole(Role role) { this.role = role; }
}