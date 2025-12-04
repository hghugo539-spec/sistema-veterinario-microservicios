package com.example.vet.repository;

import com.example.vet.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

    // Método existente
    List<Pet> findByClientIdClient(Integer clientId);

    // --- ¡AÑADE ESTE MÉTODO! ---
    // Busca mascotas donde el Cliente asociado tenga un AppUser con este email
    List<Pet> findByClientAppUserEmail(String email);
    
}