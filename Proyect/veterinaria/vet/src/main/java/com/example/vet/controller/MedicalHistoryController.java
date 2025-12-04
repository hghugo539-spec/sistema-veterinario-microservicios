package com.example.vet.controller;

import com.example.vet.dto.MedicalHistoryRequestDTO;
import com.example.vet.dto.MedicalHistoryResponseDTO;
import com.example.vet.model.MedicalHistory;
import com.example.vet.service.MedicalHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/medical-history")
@Tag(name = "Medical History", description = "API para gestionar Historiales Médicos")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
public class MedicalHistoryController {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @Autowired
    private ModelMapper modelMapper;

    // --- CREAR / AGENDAR (Clientes y Admin) ---
    @Operation(summary = "Crear un nuevo historial médico (Cita)")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')") 
    public ResponseEntity<MedicalHistoryResponseDTO> createMedicalHistory(@Valid @RequestBody MedicalHistoryRequestDTO requestDTO) {
        MedicalHistory newHistory = medicalHistoryService.saveMedicalHistory(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newHistory.getIdHistory()).toUri();
        return ResponseEntity.created(location).body(convertToDto(newHistory));
    }

    // --- LEER TODOS (Solo Admin - para ver la agenda global) ---
    @Operation(summary = "Obtener todos los historiales médicos")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MedicalHistoryResponseDTO>> getAllMedicalHistories() {
        List<MedicalHistory> histories = medicalHistoryService.findAll();
        List<MedicalHistoryResponseDTO> dtos = histories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // --- LEER POR ID (Ambos - para ver detalles) ---
    @Operation(summary = "Obtener un historial médico por su ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<MedicalHistoryResponseDTO> getMedicalHistoryById(@PathVariable Integer id) {
        return medicalHistoryService.findById(id)
                .map(history -> ResponseEntity.ok(convertToDto(history)))
                .orElse(ResponseEntity.notFound().build());
    }

    // --- LEER POR MASCOTA (Ambos - para ver historial de una mascota) ---
    @Operation(summary = "Buscar historiales por ID de mascota")
    @GetMapping("/pet/{petId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<List<MedicalHistoryResponseDTO>> getHistoriesByPetId(@PathVariable Integer petId) {
        List<MedicalHistory> histories = medicalHistoryService.findByPetId(petId);
        List<MedicalHistoryResponseDTO> dtos = histories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // --- ACTUALIZAR / ATENDER (Solo Admin - el cliente no puede auto-diagnosticarse) ---
    @Operation(summary = "Actualizar historial (Admin completa la cita)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicalHistoryResponseDTO> updateMedicalHistory(@PathVariable Integer id, @Valid @RequestBody MedicalHistoryRequestDTO requestDTO) {
        return medicalHistoryService.updateMedicalHistory(id, requestDTO)
                .map(updated -> ResponseEntity.ok(convertToDto(updated)))
                .orElse(ResponseEntity.notFound().build());
    }

    // --- BORRAR / CANCELAR (Ambos - cliente puede cancelar su cita) ---
    @Operation(summary = "Cancelar/Eliminar cita (Cliente o Admin)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<Void> deleteMedicalHistory(@PathVariable Integer id) {
        return medicalHistoryService.deleteMedicalHistoryById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private MedicalHistoryResponseDTO convertToDto(MedicalHistory history) {
        return modelMapper.map(history, MedicalHistoryResponseDTO.class);
    }
}