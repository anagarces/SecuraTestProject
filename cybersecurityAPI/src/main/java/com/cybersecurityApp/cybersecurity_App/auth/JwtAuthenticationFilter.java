package com.cybersecurityApp.cybersecurity_App.auth;


import com.cybersecurityApp.cybersecurity_App.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Esta clase actua como una primera capa antes de llegar al controlador, intercepta las peticiones
//y comprueban que tengan un token valido para mostrar los recursos protegidos
//Extiende de OncePerRequestFilter, por lo que el filtro se ejecuta una vez por cada solicitud http
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //Inyectamos las dependencias
    //JwtService es un servicio personalizado que sabe como generar, validar y leer datos del token JWT
    private final JwtService jwtService;
    //Esta dependencia saber como obtener los detalles del usuario (roles, contrasenas cifradas, etc, de la bbdd)
    private final UserDetailsService userDetailsService;


    //constructor para inyectar las dependencias
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    //contiene logica del filtro que se ejecuta en la peticion, respuesta y cadena de filtros
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        //Intenta obtener el encabezado http authorization que es donde el cliente envia el token
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //extraccion del token, corta los primeros 7 caracteres para obtener la cadena de valor del token
        final String jwt = authHeader.substring(7);
        //llama al service jwt y extrae el nombre de usuario del cuerpo del token
        final String userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}