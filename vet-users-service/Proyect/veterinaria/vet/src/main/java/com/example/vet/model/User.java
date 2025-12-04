package com.example.vet.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // --- CONSTRUCTORES MANUALES ---
    public User() {} 

    public User(String nombre, String email, String password, Role role) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // --- GETTERS Y SETTERS MANUALES ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    // --- MÉTODOS DE USER DETAILS (FINAL) ---

    // 1. Autoridades (Añade el prefijo ROLE_ para Spring Security)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name())); 
    }

    // 2. Contraseña (Solo existe esta versión requerida por la interfaz)
    @Override
    public String getPassword() { return password; } 

    // 3. Nombre de usuario (Usamos el email)
    @Override
    public String getUsername() { return email; } 

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}