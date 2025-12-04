package com.example.vet.controller;

import java.util.List; // Import correcto

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

import com.example.vet.dto.ShiftRequestDTO;
import com.example.vet.dto.ShiftResponseDTO;
import com.example.vet.service.ShiftService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shifts") // Ajusta la ruta si es necesario
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping
    public ResponseEntity<List<ShiftResponseDTO>> getAll() { // Nombre corregido
        return ResponseEntity.ok(shiftService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftResponseDTO> getById(@PathVariable Integer id) { // Nombre corregido
        return ResponseEntity.ok(shiftService.getById(id));
    }

    // Si necesitas buscar por veterinario, asegúrate de crear ese método en el servicio primero.
    // Por ahora lo comento para que no de error si no existe en el servicio.
    /*
    @GetMapping("/veterinarian/{id}")
    public ResponseEntity<List<ShiftResponseDTO>> getByVeterinarian(@PathVariable Integer id) {
        // return ResponseEntity.ok(shiftService.getShiftsByVeterinarianId(id)); 
    }
    */

    @PostMapping
    public ResponseEntity<ShiftResponseDTO> create(@RequestBody @Valid ShiftRequestDTO request) { // DTO correcto
        return new ResponseEntity<>(shiftService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShiftResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid ShiftRequestDTO request) {
        return ResponseEntity.ok(shiftService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        shiftService.delete(id);
        return ResponseEntity.noContent().build();
    }
}