package com.example.vet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vet.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    // CORRECCIÓN: Cambiado 'Name' por 'ProductName' para coincidir con la entidad
    List<Product> findByProductNameContainingIgnoreCase(String productName);
    
    List<Product> findBySupplier_IdSupplier(Integer idSupplier);
}