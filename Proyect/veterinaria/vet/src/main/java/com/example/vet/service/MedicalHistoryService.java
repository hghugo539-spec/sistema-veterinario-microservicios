package com.example.vet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.vet.dto.MedicalHistoryRequestDTO;
import com.example.vet.model.MedicalHistory;
import com.example.vet.model.Pet;
import com.example.vet.model.Veterinarian;
import com.example.vet.repository.MedicalHistoryRepository;
import com.example.vet.repository.PetRepository;
import com.example.vet.repository.VeterinarianRepository;

@Service
public class MedicalHistoryService {

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    // --- CREAR (Agendar Cita o Historial Directo) ---
    @Transactional
    public MedicalHistory saveMedicalHistory(MedicalHistoryRequestDTO requestDTO) {
        // 1. Validar que existan mascota y veterinario
        Pet pet = petRepository.findById(requestDTO.getIdPet())
            .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        Veterinarian vet = veterinarianRepository.findById(requestDTO.getIdVeterinarian())
            .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));

        // 2. MAPEO MANUAL (Para evitar errores de librerías)
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setDate(requestDTO.getDate());
        medicalHistory.setDiagnosis(requestDTO.getDiagnosis());
        medicalHistory.setTreatment(requestDTO.getTreatment());
        
        // 3. Lógica de Estado
        if (requestDTO.getStatus() == null || requestDTO.getStatus().isEmpty()) {
            // Si no se especifica (ej. Admin crea directo), es completada por defecto
            medicalHistory.setStatus("COMPLETADA");
        } else {
            // Si viene del cliente (Agendar), se guarda como se pida (PENDIENTE)
            medicalHistory.setStatus(requestDTO.getStatus());
        }
        
        // 4. Asignar Relaciones
        medicalHistory.setPet(pet);
        medicalHistory.setVeterinarian(vet);

        return medicalHistoryRepository.save(medicalHistory);
    }

    // --- ACTUALIZAR (Atender Cita / Finalizar Consulta) ---
    @Transactional
    public Optional<MedicalHistory> updateMedicalHistory(Integer id, MedicalHistoryRequestDTO requestDTO) {
        return medicalHistoryRepository.findById(id).map(existingHistory -> {
            
            // Actualizar datos médicos
            existingHistory.setDate(requestDTO.getDate());
            existingHistory.setDiagnosis(requestDTO.getDiagnosis());
            existingHistory.setTreatment(requestDTO.getTreatment());
            
            // Actualizar estado (Esto mueve la cita de Pendiente a Historial)
            if (requestDTO.getStatus() != null && !requestDTO.getStatus().isEmpty()) {
                existingHistory.setStatus(requestDTO.getStatus());
            } else {
                // Si estamos editando y no mandan status, asumimos que se completa
                existingHistory.setStatus("COMPLETADA");
            }
            
            // Actualizar veterinario si cambió
            if (requestDTO.getIdVeterinarian() != null) {
                 Veterinarian vet = veterinarianRepository.findById(requestDTO.getIdVeterinarian())
                    .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
                 existingHistory.setVeterinarian(vet);
            }

            return medicalHistoryRepository.save(existingHistory);
        });
    }

    // --- MÉTODOS DE LECTURA ---

    public List<MedicalHistory> findAll() {
        return medicalHistoryRepository.findAll();
    }

    public Optional<MedicalHistory> findById(Integer id) {
        return medicalHistoryRepository.findById(id);
    }
    
    public List<MedicalHistory> findByPetId(Integer petId) {
        return medicalHistoryRepository.findByPetIdPet(petId);
    }

    // --- BORRAR (Cancelar Cita) ---
    public boolean deleteMedicalHistoryById(Integer id) {
        if (medicalHistoryRepository.existsById(id)) {
            medicalHistoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}