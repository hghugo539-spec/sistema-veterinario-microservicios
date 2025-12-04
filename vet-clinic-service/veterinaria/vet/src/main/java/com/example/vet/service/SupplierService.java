package com.example.vet.service;

import com.example.vet.dto.SupplierRequestDTO;
import com.example.vet.model.Supplier;
import com.example.vet.repository.SupplierRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;
    
    @Autowired
    private ModelMapper modelMapper; 

    // --- MÉTODO CORREGIDO: Recibe SupplierRequestDTO ---
    public Supplier saveSupplier(SupplierRequestDTO requestDTO) {
        // Convertimos el DTO a Entidad aquí en el servicio
        Supplier supplier = modelMapper.map(requestDTO, Supplier.class);
        return supplierRepository.save(supplier);
    }

    public List<Supplier> findAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> findSupplierById(Integer id) {
        return supplierRepository.findById(id);
    }

    // --- MÉTODO CORREGIDO: Recibe SupplierRequestDTO ---
    public Optional<Supplier> updateSupplier(Integer id, SupplierRequestDTO requestDTO) {
        return supplierRepository.findById(id)
                .map(existingSupplier -> {
                    // Actualizamos los datos usando el DTO
                    modelMapper.map(requestDTO, existingSupplier);
                    return supplierRepository.save(existingSupplier);
                });
    }

    public boolean deleteSupplierById(Integer id) {
        if (supplierRepository.existsById(id)) {
            supplierRepository.deleteById(id);
            return true;
        }
        return false;
    }
}