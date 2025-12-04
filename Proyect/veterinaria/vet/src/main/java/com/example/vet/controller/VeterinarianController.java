package com.example.vet.controller;

import com.example.vet.dto.RegisterAdminDTO; 
import com.example.vet.dto.VeterinarianResponseDTO;
import com.example.vet.model.Veterinarian;
import com.example.vet.service.VeterinarianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/veterinarians")
@Tag(name = "Veterinarians", description = "API para gestionar Veterinarios")
@CrossOrigin(origins = "*") // Configuración CORS local para el controlador
public class VeterinarianController {

    @Autowired
    private VeterinarianService veterinarianService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Crear un nuevo veterinario (y su cuenta de usuario)")
    @PostMapping
    public ResponseEntity<VeterinarianResponseDTO> createVeterinarian(@Valid @RequestBody RegisterAdminDTO requestDTO) {
        Veterinarian newVeterinarian = veterinarianService.saveVeterinarian(requestDTO);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newVeterinarian.getIdVeterinarian()).toUri();
        return ResponseEntity.created(location).body(modelMapper.map(newVeterinarian, VeterinarianResponseDTO.class));
    }

    @Operation(summary = "Obtener una lista de todos los veterinarios")
    @GetMapping
    public ResponseEntity<List<VeterinarianResponseDTO>> getAllVeterinarians() {
        List<Veterinarian> veterinarians = veterinarianService.findAllVeterinarians(); // Asumiendo que tu servicio usa este nombre
        List<VeterinarianResponseDTO> dtos = veterinarians.stream()
                .map(vet -> modelMapper.map(vet, VeterinarianResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // --- MÉTODOS AGREGADOS PARA COMPLETAR LA FUNCIONALIDAD ---

    @Operation(summary = "Obtener un veterinario por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<VeterinarianResponseDTO> getVeterinarianById(@PathVariable Integer id) {
        // Asegúrate de que tu servicio tenga un método getVeterinarianById o findVeterinarianById
        Veterinarian veterinarian = veterinarianService.getVeterinarianById(id);
        return ResponseEntity.ok(modelMapper.map(veterinarian, VeterinarianResponseDTO.class));
    }

    @Operation(summary = "Actualizar un veterinario existente")
    @PutMapping("/{id}")
    public ResponseEntity<VeterinarianResponseDTO> updateVeterinarian(@PathVariable Integer id, @RequestBody RegisterAdminDTO requestDTO) {
        // Mapeamos el DTO de entrada a la entidad para pasarla al servicio
        Veterinarian veterinarianRequest = modelMapper.map(requestDTO, Veterinarian.class);
        
        // Asegúrate de que tu servicio tenga updateVeterinarian
        Veterinarian updatedVeterinarian = veterinarianService.updateVeterinarian(id, veterinarianRequest);
        
        return ResponseEntity.ok(modelMapper.map(updatedVeterinarian, VeterinarianResponseDTO.class));
    }

    @Operation(summary = "Eliminar un veterinario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeterinarian(@PathVariable Integer id) {
        veterinarianService.deleteVeterinarian(id);
        return ResponseEntity.noContent().build();
    }
}