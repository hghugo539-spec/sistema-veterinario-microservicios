package com.example.vet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vet.model.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

    // Método seguro para buscar mascotas por ID de Cliente
    List<Pet> findByClientId(Integer clientId); 
    
    // Y cualquier otro método que no use la ruta rota de AppUser/Email.
}