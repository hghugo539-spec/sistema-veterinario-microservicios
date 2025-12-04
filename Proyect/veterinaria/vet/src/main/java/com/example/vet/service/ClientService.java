package com.example.vet.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vet.dto.ClientRequestDTO;
import com.example.vet.model.Address;
import com.example.vet.model.Client;
import com.example.vet.repository.ClientRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ModelMapper modelMapper;

    // --- NUEVO MÉTODO: Obtener cliente por Email de Usuario (Para el Perfil) ---
    public Client getClientByUserEmail(String email) {
        // Busca en la relación con AppUser
        return clientRepository.findByAppUser_Email(email)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró un perfil de cliente asociado al usuario: " + email));
    }

    // 1. Obtener todos
    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    // 2. Obtener por ID
    public Client getClientById(Integer id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + id));
    }

    // 3. Crear usando DTO de Request
    public Client createClient(ClientRequestDTO requestDTO) {
        // ModelMapper convertirá automáticamente ClientRequestDTO -> Client
        Client clientToSave = modelMapper.map(requestDTO, Client.class);
        return clientRepository.save(clientToSave);
    }

    // 4. Actualizar usando DTO de Request
    public Client updateClient(Integer id, ClientRequestDTO requestDTO) {
        Client existingClient = getClientById(id);
        
        // Actualizamos datos básicos
        existingClient.setFirstName(requestDTO.getFirstName());
        existingClient.setLastName(requestDTO.getLastName());
        existingClient.setPhone(requestDTO.getPhone());
        
        // Actualizamos la Dirección
        if (requestDTO.getAddress() != null) {
            if (existingClient.getAddress() == null) {
                Address newAddress = modelMapper.map(requestDTO.getAddress(), Address.class);
                existingClient.setAddress(newAddress);
            } else {
                modelMapper.map(requestDTO.getAddress(), existingClient.getAddress());
            }
        }
        
        return clientRepository.save(existingClient);
    }

    // 5. Eliminar
    public void deleteClient(Integer id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente no encontrado con ID: " + id);
        }
        clientRepository.deleteById(id);
    }
}