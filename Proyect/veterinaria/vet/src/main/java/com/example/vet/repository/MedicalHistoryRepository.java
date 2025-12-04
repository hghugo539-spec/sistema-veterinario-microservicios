package com.example.vet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vet.model.MedicalHistory;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Integer> {

    // --- ¡ESTE ES EL MÉTODO QUE FALTABA! ---
    // Busca historiales donde la mascota (pet) tenga este ID (idPet)
    List<MedicalHistory> findByPetIdPet(Integer idPet);

    // Opcional: Buscar por ID de veterinario si lo necesitas
    // List<MedicalHistory> findByVeterinarianIdVeterinarian(Integer idVeterinarian);
}