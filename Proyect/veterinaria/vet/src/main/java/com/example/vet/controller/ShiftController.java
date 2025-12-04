package com.example.vet.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vet.dto.ShiftDTO;
import com.example.vet.dto.ShiftResponseDTO;
import com.example.vet.model.Shift;
import com.example.vet.service.ShiftService;

@RestController
@RequestMapping("/api/v1/shifts")
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private ModelMapper modelMapper;

    // 1. Obtener todos los turnos
    @GetMapping
    public ResponseEntity<List<ShiftResponseDTO>> getAllShifts() {
        List<Shift> shifts = shiftService.getAllShifts();
        List<ShiftResponseDTO> response = shifts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // 2. Obtener turno por ID
    @GetMapping("/{id}")
    public ResponseEntity<ShiftResponseDTO> getShiftById(@PathVariable Integer id) {
        Shift shift = shiftService.getShiftById(id);
        return ResponseEntity.ok(convertToDto(shift));
    }

    // --- NUEVO ENDPOINT: Obtener turnos DE UN VETERINARIO ---
    // GET /api/v1/shifts/veterinarian/1
    @GetMapping("/veterinarian/{vetId}")
    public ResponseEntity<List<ShiftResponseDTO>> getShiftsByVeterinarian(@PathVariable Integer vetId) {
        List<Shift> shifts = shiftService.getShiftsByVeterinarianId(vetId);
        
        List<ShiftResponseDTO> response = shifts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(response);
    }

    // 3. Crear un turno
    @PostMapping
    public ResponseEntity<ShiftResponseDTO> createShift(@RequestBody ShiftDTO shiftDTO) {
        Shift shiftRequest = modelMapper.map(shiftDTO, Shift.class);
        Shift shiftCreated = shiftService.createShift(shiftRequest);
        return new ResponseEntity<>(convertToDto(shiftCreated), HttpStatus.CREATED);
    }

    // 4. Actualizar un turno
    @PutMapping("/{id}")
    public ResponseEntity<ShiftResponseDTO> updateShift(@PathVariable Integer id, @RequestBody ShiftDTO shiftDTO) {
        Shift shiftRequest = modelMapper.map(shiftDTO, Shift.class);
        Shift shiftUpdated = shiftService.updateShift(id, shiftRequest);
        return ResponseEntity.ok(convertToDto(shiftUpdated));
    }

    // 5. Eliminar un turno
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Integer id) {
        shiftService.deleteShift(id);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar de conversión
    private ShiftResponseDTO convertToDto(Shift shift) {
        // ModelMapper mapea automáticamente veterinarianFirstName y LastName
        // gracias a la configuración que hicimos antes.
        return modelMapper.map(shift, ShiftResponseDTO.class);
    }
}