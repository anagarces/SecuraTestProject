package com.cybersecurityApp.cybersecurity_App.service;

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
import java.util.function.Function;

//clase que gestiona la logica del JWT
//responsabble de creer, leer y validar los tokens
@Service
public class JwtService {

    // Inyectamos el valor de nuestra clave secreta desde application.properties
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

   /*Recibe el objeto Authentication que prueba que el usuario ha iniciado sesion con exito*/
    public String generateToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName()) // establece el sujeto del token. en este caso, el email.
                .setIssuedAt(new Date(System.currentTimeMillis())) //establece fecha de emision del token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // fecha de expiracion del token
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // usa la clave secreta y un algoritmo para firmar el token.
                .compact(); //finaliza la construccion y produce la cadena JWT codificada final
    }

   /* extrae el email del usuario del payload del token */
    public String extractUsername(String token) {

        //decodificacion. utiliza la clave secreta para verificar firma
        //si es valida, decodifica el token y devuelve todos los claim si no, se lanza excepcion
        return extractClaim(token, Claims::getSubject);

    }

 /*verifica si el token es usable*/
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); //extrae el email del token
        //el token sera valido solo si el email extraido coincide con el de UserDetails cargado de la bbdd o el token no ha expirado
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    //comprueba si la fecha de expiracion del token es anterior a la hora actual
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /*utilidad para extraer cualquier claim especifico por ej, el sujeto o la expiracion del cuerpo del token decodificado*/
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

    //convierte la secret key en un objeto de tipo Key que la libreria JJWT puede usar internamente para la firma
    //y verificacion criptografica
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}