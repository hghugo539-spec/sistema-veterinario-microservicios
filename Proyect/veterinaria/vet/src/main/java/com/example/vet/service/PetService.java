package com.example.vet.service;

import com.example.vet.dto.PetRequestDTO;
import com.example.vet.model.Client;
import com.example.vet.model.Pet;
import com.example.vet.model.Species;
import com.example.vet.repository.ClientRepository;
import com.example.vet.repository.PetRepository;
import com.example.vet.repository.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private ClientRepository clientRepository; 
    @Autowired
    private SpeciesRepository speciesRepository; 

    // --- GUARDAR (ADMIN) ---
    @Transactional
    public Pet savePet(PetRequestDTO requestDTO) {
        // CORREGIDO: getIdClient() (sin guion bajo)
        Client client = clientRepository.findById(requestDTO.getIdClient())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // CORREGIDO: getIdSpecies()
        Species species = speciesRepository.findById(requestDTO.getIdSpecies())
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));

        Pet newPet = new Pet();
        newPet.setName(requestDTO.getName());
        // CORREGIDO: getBirthDate() y setBirthDate()
        newPet.setBirthDate(requestDTO.getBirthDate());
        newPet.setBreed(requestDTO.getBreed());
        newPet.setClient(client);
        newPet.setSpecies(species);

        return petRepository.save(newPet);
    }

    // --- GUARDAR (CLIENTE - EL QUE TE DA ERROR 403) ---
    @Transactional
    public Pet saveMyPet(PetRequestDTO requestDTO, String userEmail) {
        // Buscamos al cliente por su email (del token)
        Client client = clientRepository.findByAppUser_Email(userEmail)
                .orElseThrow(() -> new RuntimeException("No se encontró perfil de cliente."));

        // CORREGIDO: getIdSpecies()
        Species species = speciesRepository.findById(requestDTO.getIdSpecies())
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));

        Pet newPet = new Pet();
        newPet.setName(requestDTO.getName());
        newPet.setBirthDate(requestDTO.getBirthDate());
        newPet.setBreed(requestDTO.getBreed());
        newPet.setClient(client);
        newPet.setSpecies(species);

        return petRepository.save(newPet);
    }

    public List<Pet> findMyPets(String userEmail) {
        return petRepository.findByClientAppUserEmail(userEmail);
    }

    public List<Pet> findAllPets() {
        return petRepository.findAll();
    }

    public Optional<Pet> findPetById(Integer id) {
        return petRepository.findById(id);
    }

    public List<Pet> findPetsByClientId(Integer clientId) {
        return petRepository.findByClientIdClient(clientId);
    }

    @Transactional
    public Optional<Pet> updatePet(Integer id, PetRequestDTO requestDTO) {
        return petRepository.findById(id)
            .map(existingPet -> {
                existingPet.setName(requestDTO.getName());
                // CORREGIDO: getBirthDate()
                existingPet.setBirthDate(requestDTO.getBirthDate());
                existingPet.setBreed(requestDTO.getBreed());

                // CORREGIDO: getIdClient()
                if (requestDTO.getIdClient() != null && !requestDTO.getIdClient().equals(existingPet.getClient().getIdClient())) {
                    Client newClient = clientRepository.findById(requestDTO.getIdClient())
                            .orElseThrow(() -> new RuntimeException("Nuevo cliente no encontrado"));
                    existingPet.setClient(newClient);
                }

                // CORREGIDO: getIdSpecies()
                if (requestDTO.getIdSpecies() != null && !requestDTO.getIdSpecies().equals(existingPet.getSpecies().getIdSpecies())) {
                    Species newSpecies = speciesRepository.findById(requestDTO.getIdSpecies())
                            .orElseThrow(() -> new RuntimeException("Nueva especie no encontrada"));
                    existingPet.setSpecies(newSpecies);
                }

                return petRepository.save(existingPet);
            });
    }

    public boolean deletePetById(Integer id) {
        if (petRepository.existsById(id)) {
            petRepository.deleteById(id);
            return true;
        }
        return false;
    }
}