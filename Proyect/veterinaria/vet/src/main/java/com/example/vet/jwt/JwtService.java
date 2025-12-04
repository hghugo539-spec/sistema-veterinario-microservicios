package com.example.vet.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${security.jwt.expiration-time}")
    private long EXPIRATION_TIME;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, EXPIRATION_TIME);
    }
    
    public long getExpirationTime() {
        return EXPIRATION_TIME;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        // --- CORRECCIÓN: Forma moderna de construir el token ---
        return Jwts.builder()
                .claims(extraClaims) // Nuevo método (reemplaza setClaims)
                .subject(userDetails.getUsername()) // Nuevo método (reemplaza setSubject)
                .issuedAt(new Date(System.currentTimeMillis())) // Nuevo método (reemplaza setIssuedAt)
                .expiration(new Date(System.currentTimeMillis() + expiration)) // Nuevo método (reemplaza setExpiration)
                .signWith(getSignInKey()) // Nuevo método (reemplaza signWith con 2 args)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        // --- CORRECCIÓN: Forma moderna de leer (parsear) el token ---
        return Jwts.parser() // Nuevo método (reemplaza parserBuilder)
                .setSigningKey(getSignInKey())
                .build()
                .parseSignedClaims(token) // Nuevo método (reemplaza parseClaimsJws)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}