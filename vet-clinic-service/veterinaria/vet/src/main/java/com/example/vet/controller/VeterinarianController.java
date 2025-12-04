package com.example.vet.controller;

import java.net.URI;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vet.dto.VeterinarianRequestDTO;
import com.example.vet.dto.VeterinarianResponseDTO;
import com.example.vet.service.VeterinarianService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/veterinarians")
@CrossOrigin(origins = "*")
public class VeterinarianController {

    private final VeterinarianService veterinarianService;
    private final ModelMapper modelMapper;

    public VeterinarianController(VeterinarianService veterinarianService, ModelMapper modelMapper) {
        this.veterinarianService = veterinarianService;
        this.modelMapper = modelMapper;
    }

    // -------------------------------------------------------
    // 1. OBTENER TODOS LOS VETERINARIOS
    // -------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<VeterinarianResponseDTO>> getAllVeterinarians() {
        List<VeterinarianResponseDTO> vets = veterinarianService.getAll();
        return ResponseEntity.ok(vets);
    }

    // -------------------------------------------------------
    // 2. OBTENER VETERINARIO POR ID
    // -------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<VeterinarianResponseDTO> getVeterinarianById(@PathVariable Integer id) {
        VeterinarianResponseDTO vet = veterinarianService.getById(id);
        return ResponseEntity.ok(vet);
    }

    // -------------------------------------------------------
    // 3. CREAR VETERINARIO (SOLO ADMIN)
    // -------------------------------------------------------
    @PostMapping
    public ResponseEntity<VeterinarianResponseDTO> createVeterinarian(
            @Valid @RequestBody VeterinarianRequestDTO request) {

        VeterinarianResponseDTO saved = veterinarianService.save(request);

        URI location = URI.create("/api/v1/veterinarians/" + saved.getIdVeterinarian());

        return ResponseEntity.created(location).body(saved);
    }

    // -------------------------------------------------------
    // 4. ACTUALIZAR VETERINARIO
    // -------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<VeterinarianResponseDTO> updateVeterinarian(
            @PathVariable Integer id,
            @Valid @RequestBody VeterinarianRequestDTO request) {

        VeterinarianResponseDTO updated = veterinarianService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    // -------------------------------------------------------
    // 5. ELIMINAR VETERINARIO
    // -------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeterinarian(@PathVariable Integer id) {
        veterinarianService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
