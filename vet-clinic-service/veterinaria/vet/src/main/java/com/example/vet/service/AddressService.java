package com.example.vet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.vet.dto.AddressRequestDTO;
import com.example.vet.dto.AddressResponseDTO;
import com.example.vet.model.Address;
import com.example.vet.repository.AddressRepository;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    // Constructor Manual (Sin Lombok)
    public AddressService(AddressRepository addressRepository, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }

    public List<AddressResponseDTO> getAll() {
        return addressRepository.findAll().stream()
                .map(address -> modelMapper.map(address, AddressResponseDTO.class))
                .collect(Collectors.toList());
    }

    public AddressResponseDTO getById(Integer id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));
        return modelMapper.map(address, AddressResponseDTO.class);
    }

    public AddressResponseDTO create(AddressRequestDTO request) {
        Address address = modelMapper.map(request, Address.class);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressResponseDTO.class);
    }

    public AddressResponseDTO update(Integer id, AddressRequestDTO request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));
        
        // Actualizamos los datos
        modelMapper.map(request, address);
        address.setId(id); // Aseguramos que el ID no cambie
        
        Address updatedAddress = addressRepository.save(address);
        return modelMapper.map(updatedAddress, AddressResponseDTO.class);
    }

    public void delete(Integer id) {
        if (!addressRepository.existsById(id)) {
            throw new RuntimeException("Dirección no encontrada");
        }
        addressRepository.deleteById(id);
    }
}