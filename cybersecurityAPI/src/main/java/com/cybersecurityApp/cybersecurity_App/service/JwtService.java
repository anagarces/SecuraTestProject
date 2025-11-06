package com.cybersecurityApp.cybersecurity_App.service;

import com.cybersecurityApp.cybersecurity_App.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// Clase que gestiona la lógica del JWT
// Responsable de crear, leer y validar los tokens
@Service
public class JwtService {

    // Inyectamos el valor de nuestra clave secreta desde application.properties
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    /* Recibe el objeto Authentication que prueba que el usuario ha iniciado sesión con éxito */
    public String generateToken(Authentication authentication) {

        // Obtenemos los detalles del usuario autenticado
        Object principal = authentication.getPrincipal();
        String email = authentication.getName(); // el email siempre es el "subject" del token
        String role = "USER"; // valor por defecto

        // Si el objeto principal es nuestra entidad Usuario, extraemos el rol real
        if (principal instanceof Usuario usuario) {
            role = usuario.getRole().name(); // USER o ADMIN
        }

        // Creamos un mapa con claims adicionales
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email) // establece el sujeto del token (el email)
                .setIssuedAt(new Date(System.currentTimeMillis())) // fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // firma con clave secreta
                .compact(); // finaliza y produce la cadena JWT
    }

    /* Extrae el email del usuario del payload del token */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /* Extrae el rol desde el token */
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    /* Verifica si el token es usable */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Comprueba si la fecha de expiración del token es anterior a la hora actual
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /* Utilidad para extraer cualquier claim específico (por ej. el sujeto o la expiración) */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Convierte la secret key en un objeto de tipo Key que la librería JJWT puede usar internamente
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}