package com.example.vet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vet.model.Shift;
import com.example.vet.model.Veterinarian;
import com.example.vet.repository.ShiftRepository;
import com.example.vet.repository.VeterinarianRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    // 1. Obtener todos los turnos
    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    // 2. Obtener turno por ID
    public Shift getShiftById(Integer id) {
        return shiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turno no encontrado con ID: " + id));
    }

    // 3. Crear turno
    public Shift createShift(Shift shift) {
        if (shift.getVeterinarian() != null && shift.getVeterinarian().getIdVeterinarian() != null) {
            Integer vetId = shift.getVeterinarian().getIdVeterinarian();
            Veterinarian veterinarian = veterinarianRepository.findById(vetId)
                    .orElseThrow(() -> new EntityNotFoundException("Veterinario no encontrado con ID: " + vetId));
            shift.setVeterinarian(veterinarian);
        }
        return shiftRepository.save(shift);
    }

    // 4. Actualizar turno
    public Shift updateShift(Integer id, Shift shiftRequest) {
        Shift shiftExisting = getShiftById(id);

        shiftExisting.setDayOfWeek(shiftRequest.getDayOfWeek());
        shiftExisting.setStartTime(shiftRequest.getStartTime());
        shiftExisting.setEndTime(shiftRequest.getEndTime());

        if (shiftRequest.getVeterinarian() != null && shiftRequest.getVeterinarian().getIdVeterinarian() != null) {
            Integer vetId = shiftRequest.getVeterinarian().getIdVeterinarian();
            Veterinarian veterinarian = veterinarianRepository.findById(vetId)
                    .orElseThrow(() -> new EntityNotFoundException("Veterinario no encontrado con ID: " + vetId));
            shiftExisting.setVeterinarian(veterinarian);
        }

        return shiftRepository.save(shiftExisting);
    }

    // 5. Eliminar turno
    public void deleteShift(Integer id) {
        if (!shiftRepository.existsById(id)) {
            throw new EntityNotFoundException("Turno no encontrado con ID: " + id);
        }
        shiftRepository.deleteById(id);
    }

    // --- NUEVO MÉTODO: Filtrar por Veterinario ---
    public List<Shift> getShiftsByVeterinarianId(Integer vetId) {
        // Validamos que el veterinario exista
        if (!veterinarianRepository.existsById(vetId)) {
            throw new EntityNotFoundException("Veterinario no encontrado con ID: " + vetId);
        }
        // Usamos el repositorio actualizado
        return shiftRepository.findByVeterinarian_IdVeterinarian(vetId);
    }
}