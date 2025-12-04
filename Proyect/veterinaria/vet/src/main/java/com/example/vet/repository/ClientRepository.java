package com.example.vet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vet.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    
    // Buscar por nombre (para búsquedas generales)
    List<Client> findByFirstNameContainingIgnoreCase(String firstName);

    // --- CORRECCIÓN: ELIMINAMOS findByEmail PORQUE LA TABLA CLIENT NO TIENE EMAIL DIRECTO ---
    // Optional<Client> findByEmail(String email);  <-- ESTA LÍNEA CAUSABA EL ERROR

    // --- MÉTODOS CORRECTOS (Buscan en la relación con el Usuario) ---
    
    // Busca el cliente asociado al usuario autenticado (AppUser)
    Optional<Client> findByAppUser_Email(String email);
    
    // Verifica si existe un cliente linkeado a ese email
    boolean existsByAppUser_Email(String email);
}