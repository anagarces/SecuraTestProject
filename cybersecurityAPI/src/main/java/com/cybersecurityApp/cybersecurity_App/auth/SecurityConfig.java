 /*Configuracion principal para definir endpoints publicos y privados*/


 package com.cybersecurityApp.cybersecurity_App.auth;

 import com.cybersecurityApp.cybersecurity_App.service.UserDetailsServiceImpl;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.authentication.AuthenticationProvider;
 import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
 import org.springframework.security.config.Customizer;
 import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
 import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
 import org.springframework.security.config.annotation.web.builders.HttpSecurity;
 import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
 import org.springframework.security.config.http.SessionCreationPolicy;
 import org.springframework.security.core.userdetails.UserDetailsService;
 import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.security.web.SecurityFilterChain;
 import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


 //En esta clase definimos las configuraciones de seguridad, los algoritmos de cifrado a usar para comparar las contrasenas
 //ingresadas por el usuario con la contrasena guardada en la bbdd, gestion de sesion, acceso a endpoints...
 @Configuration
 @EnableWebSecurity
 @EnableMethodSecurity
 public class SecurityConfig {

     private final UserDetailsService userDetailsService;
     private final JwtAuthenticationFilter jwtAuthFilter;

     // Inyectamos el UserDetailsService que creamos antes
     public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtAuthenticationFilter jwtAuthFilter) {
         this.userDetailsService = userDetailsService;
         this.jwtAuthFilter = jwtAuthFilter;
     }

     // --- BEANS DE CONFIGURACIÓN ---


     //Definimos el estandar de cifrado y verificacion de contrasenas que es BCrypt
     //Este Bean es usado por AuthUserService para guardar nuevas contraseñas y por el AuthenticationProvider del controlador para compararlas.
     @Bean
     public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
     }

     //El ejecutor de la autenticacion
     //Se configura como debe realizarse la autenticacion
     //Le estamos diciendo, usa userDetailService pero mi implementacion para buscar los datos del usuario
     //Y usar el passwordEncoder para verificar la contrasena
     @Bean
     public AuthenticationProvider authenticationProvider() {
         DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
         authProvider.setUserDetailsService(userDetailsService);
         authProvider.setPasswordEncoder(passwordEncoder());
         return authProvider;
     }

     //El coordinador
     //Expone al manager como un Bean que puede ser inyectado y usado en el AuthController
     //Tiene como funcion coordinar al provider para realizar el proceso de autenticacion
     @Bean
     public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
         return config.getAuthenticationManager();
     }


     // --- REGLAS DE SEGURIDAD HTTP ---
     // Aquí definimos las reglas de acceso a los endpoints
     //Define que, como y cuando se aplica la seguridad
     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http.csrf(csrf -> csrf.disable())
                 .cors(Customizer.withDefaults())
                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .authenticationProvider(authenticationProvider())
                 .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                 .authorizeHttpRequests(auth -> auth

                         // 1. ENDPOINTS PÚBLICOS
                         .requestMatchers("/api/auth/**").permitAll()

                         // 2. ENDPOINTS SOLO PARA ADMIN
                         // ⚠️ ESTO DEBE IR ANTES QUE CUALQUIER requestMatchers().authenticated()
                         .requestMatchers("/api/admin/**").hasRole("ADMIN")

                         // 3. ENDPOINTS PROTEGIDOS NORMALES
                         .requestMatchers("/api/quizzes/**").authenticated()
                         .requestMatchers("/api/submissions/**").authenticated()

                         //LO DEMÁS REQUIERE LOGIN
                         .anyRequest().authenticated()
                 );

         return http.build();
     }

 }
