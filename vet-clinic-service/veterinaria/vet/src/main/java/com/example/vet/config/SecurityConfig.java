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
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/api/auth/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(req -> req
                // RUTAS PÚBLICAS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(WHITE_LIST_URL).permitAll()

                // ============================================================
                // CREACIÓN (POST) - USUARIO y ADMIN
                // ============================================================
                .requestMatchers(HttpMethod.POST, "/api/v1/clients/**", "/api/v1/pets/**")
                    .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/medical-history/**")
                    .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                // ============================================================
                // LECTURA (GET) - USUARIO y ADMIN
                // ============================================================
                .requestMatchers(HttpMethod.GET, 
                    "/api/v1/products/**",
                    "/api/v1/services/**",
                    "/api/v1/veterinarians/**",
                    "/api/v1/shifts/**",
                    "/api/v1/clients/**",
                    "/api/v1/pets/**",
                    "/api/v1/species/**",
                    "/api/v1/vaccines/**",
                    "/api/v1/medical-history/**"
                ).hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                // ============================================================
                // SOLO ADMIN
                // ============================================================
                .requestMatchers(HttpMethod.POST, 
                    "/api/v1/species/**",
                    "/api/v1/vaccines/**",
                    "/api/v1/veterinarians/**",
                    "/api/v1/products/**",
                    "/api/v1/shifts/**",
                    "/api/v1/suppliers/**"
                ).hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/**").hasAuthority("ROLE_ADMIN")

                // ============================================================
                // DEFAULT
                // ============================================================
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
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
