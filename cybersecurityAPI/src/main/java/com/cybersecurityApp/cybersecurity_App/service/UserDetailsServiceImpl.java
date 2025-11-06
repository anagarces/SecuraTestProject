package com.cybersecurityApp.cybersecurity_App.service;

import com.cybersecurityApp.cybersecurity_App.model.Usuario;
import com.cybersecurityApp.cybersecurity_App.model.dao.UserDao;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;


//implementamos la interfaz UserDetailsService de Spring Security para que ultimo sepa donde y como buscar informacion de usuario
//para el proceso de login
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;

    // Inyectamos el repositorio para poder acceder a la base de datos
    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscamos el usuario en nuestra BD por su email
        Usuario usuarioFromDb = userDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + email));

        // Obtenemos el rol del usuario desde la base de datos
        String roleName = usuarioFromDb.getRole().name(); // USER o ADMIN

        // Spring Security espera que los roles estén en formato "ROLE_X"
        String springRole = "ROLE_" + roleName;

        // Convertimos el objeto entidad en el formato estándar de Spring Security
        return new User(
                usuarioFromDb.getEmail(),
                usuarioFromDb.getPassword(), // Contraseña cifrada en la BD
                Collections.singletonList(new SimpleGrantedAuthority(springRole))
        );
    }
}