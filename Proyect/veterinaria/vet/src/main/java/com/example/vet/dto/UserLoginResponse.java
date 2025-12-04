package com.example.vet.dto;

public class UserLoginResponse {

    private String email;
    private String role;
    private String token;
    private long expiresIn;
    private boolean hasProfile; // <--- NUEVO CAMPO

    public UserLoginResponse() {}

    // Constructor actualizado
    public UserLoginResponse(String email, String role, String token, long expiresIn, boolean hasProfile) {
        this.email = email;
        this.role = role;
        this.token = token;
        this.expiresIn = expiresIn;
        this.hasProfile = hasProfile;
    }

    // Getters y Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
    
    // Nuevo Getter y Setter
    public boolean isHasProfile() { return hasProfile; }
    public void setHasProfile(boolean hasProfile) { this.hasProfile = hasProfile; }
}