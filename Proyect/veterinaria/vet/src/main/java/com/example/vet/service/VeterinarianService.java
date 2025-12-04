package com.example.vet.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vet.dto.RegisterAdminDTO;
import com.example.vet.model.Veterinarian;
import com.example.vet.repository.VeterinarianRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VeterinarianService {

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    @Autowired
    private ModelMapper modelMapper;

    // 1. Obtener todos
    public List<Veterinarian> findAllVeterinarians() {
        return veterinarianRepository.findAll();
    }

    // 2. Guardar (Usado en el registro)
    public Veterinarian saveVeterinarian(RegisterAdminDTO requestDTO) {
        // Mapeo básico. Si tienes lógica de creación de Usuario, mantenla aquí.
        Veterinarian veterinarian = modelMapper.map(requestDTO, Veterinarian.class);
        return veterinarianRepository.save(veterinarian);
    }

    // --- MÉTODOS FALTANTES QUE CAUSAN EL ERROR ---

    // 3. Obtener por ID (Devuelve Veterinarian, no Optional)
    public Veterinarian getVeterinarianById(Integer id) {
        return veterinarianRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veterinario no encontrado con ID: " + id));
    }

    // 4. Actualizar
    public Veterinarian updateVeterinarian(Integer id, Veterinarian vetRequest) {
        Veterinarian existingVet = getVeterinarianById(id); // Reutilizamos el método que lanza excepción si no existe

        // Actualizamos los campos permitidos
        existingVet.setFirstName(vetRequest.getFirstName());
        existingVet.setLastName(vetRequest.getLastName());
        existingVet.setLicenseNumber(vetRequest.getLicenseNumber());
        
        // Si manejas usuario/email, actualízalos aquí también si es necesario
        
        return veterinarianRepository.save(existingVet);
    }

    // 5. Eliminar
    public void deleteVeterinarian(Integer id) {
        if (!veterinarianRepository.existsById(id)) {
            throw new EntityNotFoundException("Veterinario no encontrado con ID: " + id);
        }
        veterinarianRepository.deleteById(id);
    }
}