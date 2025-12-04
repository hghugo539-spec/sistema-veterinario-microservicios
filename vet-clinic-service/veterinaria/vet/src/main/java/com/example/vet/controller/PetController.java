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

import com.example.vet.dto.PetRequestDTO;
import com.example.vet.dto.PetResponseDTO;
import com.example.vet.service.PetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public ResponseEntity<List<PetResponseDTO>> getAll() {
        return ResponseEntity.ok(petService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(petService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PetResponseDTO> create(@RequestBody @Valid PetRequestDTO request) {
        return new ResponseEntity<>(petService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid PetRequestDTO request) {
        return ResponseEntity.ok(petService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }
}