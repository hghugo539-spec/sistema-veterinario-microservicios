package com.example.vet_catalog_service.resolver; // <--- DEBE TERMINAR EN 'resolver'

import java.util.List; // Asegúrate de que esta ruta sea 100% correcta
import java.util.Optional;
import com.example.vet_catalog_service.model.Product;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;


import com.example.vet_catalog_service.repository.ProductRepository;

@Controller
public class ProductResolver {

    private final ProductRepository productRepository;

    public ProductResolver(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Resuelve List<Product>
    @QueryMapping
    public List<Product> allProducts() {
        // El repositorio ya sabe que tiene que devolver List<Product>
        return productRepository.findAll(); 
    }

    // Resuelve Optional<Product>
    @QueryMapping
    public Optional<Product> productById(@Argument Integer id) {
        return productRepository.findById(id);
    }
}