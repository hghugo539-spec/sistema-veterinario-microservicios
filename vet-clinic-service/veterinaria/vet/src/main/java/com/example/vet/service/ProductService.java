package com.example.vet.service;

import com.example.vet.dto.ProductRequestDTO;
import com.example.vet.dto.ProductResponseDTO;
import com.example.vet.dto.SupplierSimpleResponseDTO;
import com.example.vet.model.Product;
import com.example.vet.model.Supplier;
import com.example.vet.repository.ProductRepository;
import com.example.vet.repository.SupplierRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Product saveProduct(ProductRequestDTO requestDTO) {
        Product product = new Product();
        // CORRECCIÓN: Usamos setProductName
        product.setProductName(requestDTO.getProductName());
        product.setPrice(requestDTO.getPrice());
        product.setStock(requestDTO.getStock());
        
        Supplier supplier = supplierRepository.findById(requestDTO.getIdSupplier())
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado"));
        product.setSupplier(supplier);

        return productRepository.save(product);
    }

    public List<ProductResponseDTO> findAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductResponseDTO> findProductById(Integer id) {
        return productRepository.findById(id).map(this::convertToResponseDTO);
    }

    public List<ProductResponseDTO> findByProductName(String name) {
        // CORRECCIÓN: El repositorio también cambia de nombre
        return productRepository.findByProductNameContainingIgnoreCase(name).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> findBySupplierId(Integer supplierId) {
        return productRepository.findBySupplier_IdSupplier(supplierId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<Product> updateProduct(Integer id, ProductRequestDTO requestDTO) {
        return productRepository.findById(id).map(existingProduct -> {
            // CORRECCIÓN: Usamos setProductName
            existingProduct.setProductName(requestDTO.getProductName());
            existingProduct.setPrice(requestDTO.getPrice());
            existingProduct.setStock(requestDTO.getStock());

            if (!existingProduct.getSupplier().getIdSupplier().equals(requestDTO.getIdSupplier())) {
                Supplier newSupplier = supplierRepository.findById(requestDTO.getIdSupplier())
                        .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado"));
                existingProduct.setSupplier(newSupplier);
            }
            return productRepository.save(existingProduct);
        });
    }

    public boolean deleteProductById(Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ProductResponseDTO convertToResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setIdProduct(product.getIdProduct());
        // CORRECCIÓN: Usamos getProductName
        dto.setProductName(product.getProductName());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        
        if (product.getSupplier() != null) {
            SupplierSimpleResponseDTO supplierDTO = modelMapper.map(product.getSupplier(), SupplierSimpleResponseDTO.class);
            dto.setSupplier(supplierDTO);
        }
        return dto;
    }
}