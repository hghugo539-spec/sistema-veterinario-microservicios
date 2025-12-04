package com.example.vet.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Importante para leer el token
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.vet.dto.PetRequestDTO;
import com.example.vet.dto.PetResponseDTO;
import com.example.vet.model.Pet;
import com.example.vet.service.PetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pets")
@Tag(name = "Pets", description = "API para gestionar Mascotas")
@CrossOrigin(origins = "*")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private ModelMapper modelMapper;

    // ----------------------------------------------------------------
    //  NUEVOS ENDPOINTS PARA CLIENTES (Usan el Token JWT)
    // ----------------------------------------------------------------

    @Operation(summary = "Obtener solo las mascotas del cliente autenticado (Token)")
    @GetMapping("/my-pets")
    public ResponseEntity<List<PetResponseDTO>> getMyPets(Authentication authentication) {
        // 1. Obtener el email del usuario desde el token
        String userEmail = authentication.getName();
        
        // 2. Buscar mascotas usando ese email
        List<Pet> pets = petService.findMyPets(userEmail);
        
        // 3. Convertir a DTO
        List<PetResponseDTO> dtos = pets.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Registrar una mascota para el cliente autenticado (Token)")
    @PostMapping("/my-pets")
    public ResponseEntity<PetResponseDTO> createMyPet(
            @Valid @RequestBody PetRequestDTO requestDTO, 
            Authentication authentication) {
        
        // 1. Obtener el email del usuario desde el token
        String userEmail = authentication.getName();
        
        // 2. Guardar la mascota usando ese email (el servicio busca el Cliente)
        Pet newPet = petService.saveMyPet(requestDTO, userEmail);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newPet.getIdPet()).toUri();
        
        return ResponseEntity.created(location).body(convertToDto(newPet));
    }

    // ----------------------------------------------------------------
    //  ENDPOINTS ESTÁNDAR / ADMIN
    // ----------------------------------------------------------------

    @Operation(summary = "Obtener una lista de todas las mascotas (Admin)")
    @GetMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<PetResponseDTO>> getAllPets() {
        List<Pet> pets = petService.findAllPets();
        List<PetResponseDTO> dtos = pets.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener una mascota por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDTO> getPetById(@PathVariable Integer id) {
        return petService.findPetById(id)
                .map(pet -> ResponseEntity.ok(convertToDto(pet)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Obtener todas las mascotas de un cliente específico (Admin)")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PetResponseDTO>> getPetsByClientId(@PathVariable Integer clientId) {
        List<Pet> pets = petService.findPetsByClientId(clientId);
        List<PetResponseDTO> dtos = pets.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Crear una nueva mascota (Admin - requiere ID Cliente)")
    @PostMapping
    public ResponseEntity<PetResponseDTO> createPet(@Valid @RequestBody PetRequestDTO requestDTO) {
        Pet newPet = petService.savePet(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newPet.getIdPet()).toUri();
        return ResponseEntity.created(location).body(convertToDto(newPet));
    }

    @Operation(summary = "Actualizar una mascota existente")
    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> updatePet(@PathVariable Integer id, @Valid @RequestBody PetRequestDTO requestDTO) {
        return petService.updatePet(id, requestDTO)
                .map(updatedPet -> ResponseEntity.ok(convertToDto(updatedPet)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar una mascota por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Integer id) {
        return petService.deletePetById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // --- Método de Conversión ---
    private PetResponseDTO convertToDto(Pet pet) {
        PetResponseDTO dto = modelMapper.map(pet, PetResponseDTO.class);
        
        // Si la mascota tiene dueño, concatenamos su nombre
        if (pet.getClient() != null) {
            String fullName = pet.getClient().getFirstName() + " " + pet.getClient().getLastName();
            dto.setOwnerName(fullName);
        } else {
            dto.setOwnerName("Sin dueño asignado");
        }
        
        return dto;
    }
}