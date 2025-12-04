package com.example.vet.service;

import com.example.vet.repository.UserRepository; // <-- CORREGIDO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // <-- CORREGIDO

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Ahora busca en tu UserRepository
        return userRepository.findByEmail(email) // <-- CORREGIDO
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
    }
}