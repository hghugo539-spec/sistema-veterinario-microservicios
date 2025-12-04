package com.example.vet.controller;

import java.util.List;

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

import com.example.vet.dto.MedicalHistoryRequestDTO;
import com.example.vet.dto.MedicalHistoryResponseDTO;
import com.example.vet.service.MedicalHistoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/medical-history")
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;

    // Constructor Manual
    public MedicalHistoryController(MedicalHistoryService medicalHistoryService) {
        this.medicalHistoryService = medicalHistoryService;
    }

    @PostMapping
    public ResponseEntity<MedicalHistoryResponseDTO> create(@RequestBody @Valid MedicalHistoryRequestDTO request) {
        // En el servicio se llama 'create'
        return new ResponseEntity<>(medicalHistoryService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MedicalHistoryResponseDTO>> getAll() { // Nombre corregido
        return ResponseEntity.ok(medicalHistoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalHistoryResponseDTO> getById(@PathVariable Integer id) { // Nombre corregido
        return ResponseEntity.ok(medicalHistoryService.getById(id));
    }

    // Método para buscar por mascota
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<MedicalHistoryResponseDTO>> getByPetId(@PathVariable Integer petId) {
        // En el servicio se llama 'getByPetId'
        return ResponseEntity.ok(medicalHistoryService.getByPetId(petId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalHistoryResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid MedicalHistoryRequestDTO request) {
        // En el servicio se llama 'update'
        return ResponseEntity.ok(medicalHistoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        // En el servicio se llama 'delete'
        medicalHistoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}