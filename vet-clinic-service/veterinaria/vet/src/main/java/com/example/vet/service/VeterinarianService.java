package com.example.vet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.vet.dto.VeterinarianRequestDTO;
import com.example.vet.dto.VeterinarianResponseDTO;
import com.example.vet.model.Veterinarian;
import com.example.vet.repository.VeterinarianRepository;

@Service
public class VeterinarianService {

    private final VeterinarianRepository veterinarianRepository;
    private final ModelMapper modelMapper;

    // Constructor Manual (Sin Lombok)
    public VeterinarianService(VeterinarianRepository veterinarianRepository, ModelMapper modelMapper) {
        this.veterinarianRepository = veterinarianRepository;
        this.modelMapper = modelMapper;
    }

    // 1. Método getAll
    public List<VeterinarianResponseDTO> getAll() {
        return veterinarianRepository.findAll().stream()
                .map(vet -> modelMapper.map(vet, VeterinarianResponseDTO.class))
                .collect(Collectors.toList());
    }

    // 2. Método getById
    public VeterinarianResponseDTO getById(Integer id) {
        Veterinarian vet = veterinarianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con ID: " + id));
        return modelMapper.map(vet, VeterinarianResponseDTO.class);
    }

    // 3. Método save (Crear)
    public VeterinarianResponseDTO save(VeterinarianRequestDTO request) {
        // Convertimos DTO a Entidad
        Veterinarian vet = modelMapper.map(request, Veterinarian.class);
        
        // Guardamos en BD
        Veterinarian savedVet = veterinarianRepository.save(vet);
        
        // Devolvemos DTO
        return modelMapper.map(savedVet, VeterinarianResponseDTO.class);
    }

    // 4. Método update (Actualizar)
    public VeterinarianResponseDTO update(Integer id, VeterinarianRequestDTO request) {
        Veterinarian vet = veterinarianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con ID: " + id));
        
        // Actualizamos los campos
        modelMapper.map(request, vet);
        vet.setId(id); // Aseguramos que el ID no se pierda
        
        Veterinarian updatedVet = veterinarianRepository.save(vet);
        return modelMapper.map(updatedVet, VeterinarianResponseDTO.class);
    }

    // 5. Método delete (Eliminar)
    public void delete(Integer id) {
        if (!veterinarianRepository.existsById(id)) {
            throw new RuntimeException("Veterinario no encontrado");
        }
        veterinarianRepository.deleteById(id);
    }
}