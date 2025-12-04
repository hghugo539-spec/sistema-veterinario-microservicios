package com.example.vet.service;

import com.example.vet.dto.UserLoginRequest;
import com.example.vet.dto.UserLoginResponse;
import com.example.vet.dto.UserRequest;
import com.example.vet.dto.UserResponse;
import com.example.vet.jwt.JwtService;
import com.example.vet.model.User;
import com.example.vet.model.Role;
import com.example.vet.repository.UserRepository;
import com.example.vet.repository.RoleRepository;
import com.example.vet.repository.ClientRepository; // <--- NUEVA IMPORTACIÓN

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClientRepository clientRepository; // <--- NUEVO AUTOWIRED
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtService jwtService;

    @Transactional
    public UserResponse registerUser(UserRequest request) {
        
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está en uso");
        }

        // Si el rol viene nulo o vacío, asigna CLIENT por defecto
        String roleName = request.getRole();
        if (roleName == null || roleName.isEmpty()) {
            roleName = "ROLE_CLIENT";
        }

        // Crea una variable 'final' para usarla en la lambda
        final String finalRoleName = roleName; 
        Role userRole = roleRepository.findByName(finalRoleName)
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado: " + finalRoleName));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setAuthorities(roles);
        newUser.setEnabled(true);

        userRepository.save(newUser);

        return new UserResponse(newUser.getEmail(), userRole.getName());
    }
    
    public UserLoginResponse login(UserLoginRequest request) {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        
        String role = user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

        // --- LÓGICA NUEVA: Verificar si ya tiene perfil ---
        boolean hasProfile = false;
        
        // Solo verificamos perfil si es un CLIENTE. Los Admins (Veterinarios) tienen otra lógica.
        if (role.contains("ROLE_CLIENT")) {
             // Usamos el método que añadimos al ClientRepository
             hasProfile = clientRepository.existsByAppUser_Email(user.getEmail());
        }
        // --------------------------------------------------

        return new UserLoginResponse(user.getEmail(), role, token, jwtService.getExpirationTime(), hasProfile);
    }
}