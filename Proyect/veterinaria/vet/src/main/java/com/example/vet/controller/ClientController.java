package com.example.vet.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Importante para leer el token
import org.springframework.web.bind.annotation.*;

import com.example.vet.dto.ClientRequestDTO;
import com.example.vet.dto.ClientResponseDTO;
import com.example.vet.model.Client;
import com.example.vet.service.ClientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ModelMapper modelMapper;

    // --- NUEVO ENDPOINT: MI PERFIL (Soluciona el 403 en VerPerfil) ---
    @GetMapping("/profile")
    public ResponseEntity<ClientResponseDTO> getMyProfile(Authentication authentication) {
        // Extraemos el email del token JWT
        String userEmail = authentication.getName();
        
        // Llamamos al servicio para buscar el cliente asociado a este usuario
        Client client = clientService.getClientByUserEmail(userEmail);
        
        return ResponseEntity.ok(modelMapper.map(client, ClientResponseDTO.class));
    }

    // 1. Obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {
        List<Client> clients = clientService.findAllClients();
        List<ClientResponseDTO> dtos = clients.stream()
                .map(client -> modelMapper.map(client, ClientResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // 2. Obtener cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Integer id) {
        Client client = clientService.getClientById(id);
        return ResponseEntity.ok(modelMapper.map(client, ClientResponseDTO.class));
    }

    // 3. Crear Cliente (Admin)
    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO requestDTO) {
        Client clientCreated = clientService.createClient(requestDTO);
        return new ResponseEntity<>(modelMapper.map(clientCreated, ClientResponseDTO.class), HttpStatus.CREATED);
    }

    // 4. Actualizar Cliente
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable Integer id, @Valid @RequestBody ClientRequestDTO requestDTO) {
        Client clientUpdated = clientService.updateClient(id, requestDTO);
        return ResponseEntity.ok(modelMapper.map(clientUpdated, ClientResponseDTO.class));
    }

    // 5. Eliminar Cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}