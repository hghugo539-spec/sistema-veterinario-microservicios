package com.example.vet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vet.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    
    // Buscar por nombre (Sigue funcionando bien)
    List<Client> findByFirstNameContainingIgnoreCase(String firstName);

    // --- CORRECCIÓN FINAL ---
    
    // ANTES: findByAppUser_Email (Ya no existe AppUser)
    // AHORA: findByUserId (Buscamos por el ID numérico que guardamos)
    Optional<Client> findByUserId(Integer userId);
    
    // ANTES: existsByAppUser_Email
    // AHORA: existsByUserId
    boolean existsByUserId(Integer userId);

    // Si tu cliente tiene un campo "email" propio (independiente del usuario), puedes usar esto:
    Optional<Client> findByEmail(String email);
}