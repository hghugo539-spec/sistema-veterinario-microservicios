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

import com.example.vet.dto.ClientRequestDTO;
import com.example.vet.dto.ClientResponseDTO;
import com.example.vet.service.ClientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    // Constructor Manual
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getAll() { // Nombre corregido
        return ResponseEntity.ok(clientService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getById(@PathVariable Integer id) { // Nombre corregido
        return ResponseEntity.ok(clientService.getById(id));
    }

    // Método para buscar por email (útil para integraciones)
    @GetMapping("/email/{email}")
    public ResponseEntity<ClientResponseDTO> getByEmail(@PathVariable String email) {
        // En el servicio lo llamamos getByEmail
        return ResponseEntity.ok(clientService.getByEmail(email)); 
    }

    /* NOTA: El método "getClientByUserEmail" que tenías antes solía sacar el email del Token.
       Como en este servicio básico no estamos extrayendo el usuario del token en el Controller,
       usaremos el método de arriba o simplemente el CRUD estándar.
    */

    @PostMapping
    public ResponseEntity<ClientResponseDTO> create(@RequestBody @Valid ClientRequestDTO request) {
        // En el servicio se llama 'save'
        return new ResponseEntity<>(clientService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid ClientRequestDTO request) {
        // En el servicio se llama 'update'
        return ResponseEntity.ok(clientService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        // En el servicio se llama 'delete'
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}