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

        //Si encuentra el usuario, el servicio convierte el objeto entidad en objeto estandar de Spring Security
        //y se devuelven los datos correspondientes
        return new User(
                usuarioFromDb.getEmail(),
                usuarioFromDb.getPassword(), //contrasena cifrada que esta en la bbdd
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) //se le asigna un rol por defecto
        );
    }
}