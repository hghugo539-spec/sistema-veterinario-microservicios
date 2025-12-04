package com.example.vet.dto;

public class UserResponse {

    private String email;
    private String role;

    // Constructores
    public UserResponse() {
    }

    public UserResponse(String email, String role) {
        this.email = email;
        this.role = role;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}