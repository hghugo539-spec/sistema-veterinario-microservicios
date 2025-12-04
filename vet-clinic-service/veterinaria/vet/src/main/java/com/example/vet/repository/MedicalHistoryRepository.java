package com.example.vet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vet.model.MedicalHistory;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Integer> {

    // CORRECCIÓN: El nombre estándar es findBy + NombreDeLaVariable + Id
    // Esto funcionará si en MedicalHistory tienes "private Pet pet;" o "private Integer petId;"
    
    List<MedicalHistory> findByPetId(Integer petId);
    
}