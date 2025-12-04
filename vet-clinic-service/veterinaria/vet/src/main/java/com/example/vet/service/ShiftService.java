package com.example.vet.service;

import com.example.vet.dto.ShiftRequestDTO;
import com.example.vet.dto.ShiftResponseDTO;
import com.example.vet.model.Shift;
import com.example.vet.model.Veterinarian;
import com.example.vet.repository.ShiftRepository;
import com.example.vet.repository.VeterinarianRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final ModelMapper modelMapper;

    // Constructor Manual (Sin Lombok)
    public ShiftService(ShiftRepository shiftRepository, 
                        VeterinarianRepository veterinarianRepository, 
                        ModelMapper modelMapper) {
        this.shiftRepository = shiftRepository;
        this.veterinarianRepository = veterinarianRepository;
        this.modelMapper = modelMapper;
    }

    public List<ShiftResponseDTO> getAll() {
        return shiftRepository.findAll().stream()
                .map(shift -> modelMapper.map(shift, ShiftResponseDTO.class))
                .collect(Collectors.toList());
    }

    public ShiftResponseDTO getById(Integer id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        return modelMapper.map(shift, ShiftResponseDTO.class);
    }

    public ShiftResponseDTO create(ShiftRequestDTO request) {
        // Buscamos el veterinario por ID
        Veterinarian vet = veterinarianRepository.findById(request.getIdVeterinarian())
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));

        Shift shift = modelMapper.map(request, Shift.class);
        shift.setVeterinarian(vet);

        Shift savedShift = shiftRepository.save(shift);
        return modelMapper.map(savedShift, ShiftResponseDTO.class);
    }

    public ShiftResponseDTO update(Integer id, ShiftRequestDTO request) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        // Validamos si cambió el veterinario
        // AQUÍ ESTABA EL ERROR: Usamos .getId() en lugar de .getIdVeterinarian()
        if (shift.getVeterinarian() != null && !shift.getVeterinarian().getId().equals(request.getIdVeterinarian())) {
            Veterinarian vet = veterinarianRepository.findById(request.getIdVeterinarian())
                    .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
            shift.setVeterinarian(vet);
        }

        // Mapeamos los otros datos (fecha, hora, etc.)
        modelMapper.map(request, shift);
        shift.setId(id); // Aseguramos el ID

        Shift updatedShift = shiftRepository.save(shift);
        return modelMapper.map(updatedShift, ShiftResponseDTO.class);
    }

    public void delete(Integer id) {
        if (!shiftRepository.existsById(id)) {
            throw new RuntimeException("Turno no encontrado");
        }
        shiftRepository.deleteById(id);
    }
}