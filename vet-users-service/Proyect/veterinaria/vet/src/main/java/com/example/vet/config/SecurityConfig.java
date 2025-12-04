package com.example.vet.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.vet.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    private static final String[] WHITE_LIST_URL = {
        "/api/v1/auth/**",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

            .authorizeHttpRequests(req -> req
                // 1. REGLAS TÉCNICAS Y PÚBLICAS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(WHITE_LIST_URL).permitAll()
                
                // 1.B. LA REGLA FALTANTE: Permite acceso a /api/auth/register y /login sin versión
                .requestMatchers("/api/auth/**").permitAll() 
                
                // 2. LECTURA DE CATÁLOGOS PÚBLICOS/COMPARTIDOS (GET)
                .requestMatchers(HttpMethod.GET, 
                    "/api/v1/products/**", 
                    "/api/v1/services/**", 
                    "/api/v1/veterinarians/**", 
                    "/api/v1/shifts/**"
                ).hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT")

                // 3. REGLAS DE ACCESO GENERAL (Cliente y Admin)
                .requestMatchers("/api/v1/clients/**", "/api/v1/pets/**", "/api/v1/vaccines/**", "/api/v1/species/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT")
                .requestMatchers(HttpMethod.POST, "/api/v1/medical-history/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/medical-history/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT")
                
                // 4. REGLAS EXCLUSIVAS DE ADMIN (Gestiión de datos críticos)
                .requestMatchers(HttpMethod.PUT, "/api/v1/medical-history/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/v1/products/**").hasAuthority("ROLE_ADMIN") 
                .requestMatchers("/api/v1/services/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/v1/veterinarians/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/v1/shifts/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/v1/suppliers/**").hasAuthority("ROLE_ADMIN")

                // 5. CUALQUIER OTRA COSA DEBE ESTAR AUTENTICADA
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        configuration.setAllowCredentials(true );

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}