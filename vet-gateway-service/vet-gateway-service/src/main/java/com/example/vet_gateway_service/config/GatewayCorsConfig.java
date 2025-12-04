package com.example.vet_gateway_service.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration; // <--- Clase de SPRING
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
// La clase local ahora se llama GatewayCorsConfig para evitar conflictos
public class GatewayCorsConfig { 

    @Bean
    public CorsWebFilter corsWebFilter() {
        // Fuente de configuración CORS
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        CorsConfiguration config = new CorsConfiguration(); // <-- Usamos la clase de SPRING
        
        // --- REGLAS DE ACCESO ---
        
        // Permite los orígenes de tu frontend
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000"));
        
        // Métodos permitidos (POST, GET, OPTIONS, etc.)
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Cabeceras que el cliente puede enviar y que Spring debe devolver
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        
        // Expone cabeceras para JWT
        config.setExposedHeaders(Collections.singletonList("Authorization"));
        
        // Permite enviar cookies y credenciales (si usas sesión)
        config.setAllowCredentials(true);
        
        // Registra la configuración para todas las rutas del Gateway (/**)
        source.registerCorsConfiguration("/**", config);
        
        return new CorsWebFilter(source);
    }
}