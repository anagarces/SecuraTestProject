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
            @NonNull HttpServletRequest request, //contiene lo que llega del front: header, body, url, etc.
            @NonNull HttpServletResponse response, //lo que devolveras al cliente
            @NonNull FilterChain filterChain //la cadena para pasar la solicitud al siguiente filtro
    ) throws ServletException, IOException {

        //detectar la url solicitada, ej, el endpoint especificado
        final String requestPath = request.getServletPath();

        // Ignorar los endpoints públicos (login, registro, etc.)
        //Si es alguna de estas rutas, no intentes validar ningun token.
        //Pasa la solicitud al siguiente filtro
        if (requestPath.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }


        //Buscar si en la solicitud trae un token en el header
        final String authHeader = request.getHeader("Authorization");
        //Si no hay token, dejar pasar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //extraccion del token, corta los primeros 7 caracteres para obtener la cadena de valor del token
        final String jwt = authHeader.substring(7);
        //llama al service jwt y extrae el nombre de usuario del cuerpo del token
        final String userEmail = jwtService.extractUsername(jwt);

        //Verificar si el usuario esta autenticado en el contexto
        //1. Comprueba que el token contenia un usuario valido
        //2. Que todavia no hay un usuario autenticado en el contexto de srguridad actual
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //validar el token y autenticar
            //cargar detalles del usuario desde la fuente de datos
            //comprobar si el token corresponde con el usuario y no ha expirado
            //si es correcto, crea un objeto Authentication con datos del usuario y se mete en el securitycontextholder
            //a partir de ahora, esta peticion tiene un usuario autenticado con estos roles
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

        //Pasar la peticion al siguiente filtro o controlador
        filterChain.doFilter(request, response);
    }
}