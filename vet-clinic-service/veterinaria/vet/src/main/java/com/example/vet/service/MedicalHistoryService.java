package com.example.vet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.vet.dto.MedicalHistoryRequestDTO;
import com.example.vet.dto.MedicalHistoryResponseDTO;
import com.example.vet.model.MedicalHistory;
import com.example.vet.model.Pet;
import com.example.vet.repository.MedicalHistoryRepository;
import com.example.vet.repository.PetRepository;

@Service
public class MedicalHistoryService {

    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PetRepository petRepository;
    private final ModelMapper modelMapper;

    // Constructor Manual
    public MedicalHistoryService(MedicalHistoryRepository medicalHistoryRepository, 
                                 PetRepository petRepository, 
                                 ModelMapper modelMapper) {
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.petRepository = petRepository;
        this.modelMapper = modelMapper;
    }

    public List<MedicalHistoryResponseDTO> getAll() {
        return medicalHistoryRepository.findAll().stream()
                .map(history -> modelMapper.map(history, MedicalHistoryResponseDTO.class))
                .collect(Collectors.toList());
    }

    public MedicalHistoryResponseDTO getById(Integer id) {
        MedicalHistory history = medicalHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado"));
        return modelMapper.map(history, MedicalHistoryResponseDTO.class);
    }

    // CORRECCIÓN AQUÍ: Usamos findByPetId
    public List<MedicalHistoryResponseDTO> getByPetId(Integer petId) {
        // Antes llamabas a findByPetIdPet, ahora usamos el nombre correcto:
        return medicalHistoryRepository.findByPetId(petId).stream()
                .map(history -> modelMapper.map(history, MedicalHistoryResponseDTO.class))
                .collect(Collectors.toList());
    }

    public MedicalHistoryResponseDTO create(MedicalHistoryRequestDTO request) {
        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        MedicalHistory history = modelMapper.map(request, MedicalHistory.class);
        history.setPet(pet);

        MedicalHistory savedHistory = medicalHistoryRepository.save(history);
        return modelMapper.map(savedHistory, MedicalHistoryResponseDTO.class);
    }

    public MedicalHistoryResponseDTO update(Integer id, MedicalHistoryRequestDTO request) {
        MedicalHistory history = medicalHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado"));

        if (history.getPet() != null && !history.getPet().getId().equals(request.getPetId())) {
            Pet pet = petRepository.findById(request.getPetId())
                    .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
            history.setPet(pet);
        }

        modelMapper.map(request, history);
        history.setId(id);

        MedicalHistory updatedHistory = medicalHistoryRepository.save(history);
        return modelMapper.map(updatedHistory, MedicalHistoryResponseDTO.class);
    }

    public void delete(Integer id) {
        if (!medicalHistoryRepository.existsById(id)) {
            throw new RuntimeException("Historial no encontrado");
        }
        medicalHistoryRepository.deleteById(id);
    }
}