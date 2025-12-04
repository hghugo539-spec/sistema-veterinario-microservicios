package com.example.vet.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        // Al tener los campos en el DTO con nombres exactos (veterinarianFirstName),
        // ModelMapper hace la conversión automáticamente sin necesidad de configuración extra.
        return new ModelMapper(); 
    }
}