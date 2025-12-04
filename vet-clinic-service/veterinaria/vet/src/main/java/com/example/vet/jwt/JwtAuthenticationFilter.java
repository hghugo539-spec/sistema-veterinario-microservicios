package com.example.vet.jwt;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 1️⃣ Extraer claims
                Claims claims = jwtService.extractAllClaims(jwt);

                // 2️⃣ Extraer roles del token
                List<String> roles = claims.get("roles", List.class);
                if (roles == null) {
                    roles = Collections.emptyList();
                }

                // 3️⃣ Evitar doble prefijo ROLE_
                var authorities = roles.stream()
                        .map(role -> role.startsWith("ROLE_") ? new SimpleGrantedAuthority(role)
                                                              : new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                // Debug
                System.out.println("Usuario autenticado: " + userEmail);
                System.out.println("Authorities finales: " + authorities);

                // 4️⃣ Crear UserDetails
                UserDetails userDetails = new User(userEmail, "", authorities);

                // 5️⃣ Validar token y setear contexto de seguridad
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            authorities
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            System.out.println("Error procesando JWT: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
