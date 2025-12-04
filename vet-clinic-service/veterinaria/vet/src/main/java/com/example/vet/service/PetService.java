package com.example.vet.service;

import com.example.vet.dto.PetRequestDTO;
import com.example.vet.dto.PetResponseDTO;
import com.example.vet.model.Client;
import com.example.vet.model.Pet;
import com.example.vet.repository.ClientRepository;
import com.example.vet.repository.PetRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    // Constructor Manual (Sin Lombok)
    public PetService(PetRepository petRepository, ClientRepository clientRepository, ModelMapper modelMapper) {
        this.petRepository = petRepository;
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    public List<PetResponseDTO> getAll() {
        return petRepository.findAll().stream()
                .map(pet -> modelMapper.map(pet, PetResponseDTO.class))
                .collect(Collectors.toList());
    }

    public PetResponseDTO getById(Integer id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        return modelMapper.map(pet, PetResponseDTO.class);
    }

    public PetResponseDTO save(PetRequestDTO request) {
        // Buscamos al dueño (Cliente)
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Pet pet = modelMapper.map(request, Pet.class);
        pet.setClient(client);

        Pet savedPet = petRepository.save(pet);
        return modelMapper.map(savedPet, PetResponseDTO.class);
    }

    public PetResponseDTO update(Integer id, PetRequestDTO request) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        // Verificamos si cambió de dueño
        // CORRECCIÓN AQUÍ: Usamos .getId() en vez de .getIdClient()
        if (pet.getClient() != null && !pet.getClient().getId().equals(request.getClientId())) {
            Client client = clientRepository.findById(request.getClientId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            pet.setClient(client);
        }

        modelMapper.map(request, pet);
        pet.setId(id); // Aseguramos el ID

        Pet updatedPet = petRepository.save(pet);
        return modelMapper.map(updatedPet, PetResponseDTO.class);
    }

    public void delete(Integer id) {
        if (!petRepository.existsById(id)) {
            throw new RuntimeException("Mascota no encontrada");
        }
        petRepository.deleteById(id);
    }
}