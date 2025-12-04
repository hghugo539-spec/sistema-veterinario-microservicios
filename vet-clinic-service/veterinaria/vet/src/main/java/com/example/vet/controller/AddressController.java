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

import com.example.vet.dto.AddressRequestDTO;
import com.example.vet.dto.AddressResponseDTO;
import com.example.vet.service.AddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/addresses") // Ruta base para direcciones
public class AddressController {

    private final AddressService addressService;

    // Constructor Manual
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<AddressResponseDTO> create(@RequestBody @Valid AddressRequestDTO request) {
        // Usamos create y pasamos el DTO
        AddressResponseDTO newAddress = addressService.create(request);
        return new ResponseEntity<>(newAddress, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAll() { // Nombre corregido
        return ResponseEntity.ok(addressService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> getById(@PathVariable Integer id) { // Nombre corregido
        return ResponseEntity.ok(addressService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid AddressRequestDTO request) {
        return ResponseEntity.ok(addressService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        addressService.delete(id); // Nombre corregido
        return ResponseEntity.noContent().build();
    }
}