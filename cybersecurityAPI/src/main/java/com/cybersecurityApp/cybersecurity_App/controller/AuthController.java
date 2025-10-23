package com.cybersecurityApp.cybersecurity_App.controller;


import com.cybersecurityApp.cybersecurity_App.model.dto.AuthResponseDTO;
import com.cybersecurityApp.cybersecurity_App.model.dto.LoginRequestDTO;
import com.cybersecurityApp.cybersecurity_App.model.dto.RegisterRequestDTO;
import com.cybersecurityApp.cybersecurity_App.service.AuthUserService;
import com.cybersecurityApp.cybersecurity_App.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth") // Todas las rutas de este controlador empezarán con /api/auth
public class AuthController {

    private final AuthUserService authUserService; //servicio de autenticacion
    private final AuthenticationManager authenticationManager; //validacion de spring security
    private final JwtService jwtService; //generacion de tokens


    // Inyectamos las dependencias que necesitamos
    public AuthController(AuthUserService authUserService, AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.authUserService = authUserService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @PostMapping("/register")
    //Recibe un DTO y se asegura de que sea valido (@valid)
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
            try{
                //delega toda la logica de negocio a este servicio
                authUserService.registerUser(registerRequestDTO);

                return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente.");
            } catch (RuntimeException e){
                //captura el error de negocio (email duplicado) y devuelve 400

                return ResponseEntity.badRequest().body("Error: " + e.getMessage());
            }
    }

    @PostMapping("/login")
    //si las credenciales son correctas, emite un pase de acceso digital (el token)
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {

        // Usamos el AuthenticationManager de Spring Security para validar las credenciales
        //UsernamePasswordAuthenticationToke crea un objeto que empaqueta el email y la contraseña ingresada (sin cifrar) por el usuario
        //authenticate() dispara el flujo
        //llama internamente a UserDetailsServiceImpl (loadUserByUsername) y al PasswordEncoder
        //Obtiene la entidad del usuario de la bbdd (contrasena cifrada)
        //compara las contrasena ingresada con la contrasena cifrada (usando el passwordEncoder)

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Si la autenticación es exitosa, Spring nos devuelve un objeto Authentication.
        //Este objeto authentication es el ingrdiente para crear el token
        // A partir de él, podemos generar el token.
        String token = jwtService.generateToken(authentication);

        // Devolvemos el token en la respuesta
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
