package com.cybersecurityApp.cybersecurity_App.service;

import com.cybersecurityApp.cybersecurity_App.model.Usuario;
import com.cybersecurityApp.cybersecurity_App.model.dao.UserDao;
import com.cybersecurityApp.cybersecurity_App.model.dto.RegisterRequestDTO;
import org.mapstruct.control.MappingControl;
import org.springframework.security.crypto.password.PasswordEncoder;

//servicio para manejar autenticacion de usuarios
public class AuthUserService {

    //inyectamos dependencias
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;


    public AuthUserService(UserDao userDao, PasswordEncoder passwordEncoder){
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterRequestDTO requestDTO) throws RuntimeException{

        //verificacion de duplicidad
        if(userDao.findByEmail(requestDTO.getEmail()).isPresent()){
            throw new RuntimeException("El email ya esta en uso.");
        }

        //Creacion de la entidad
        Usuario newUsuario = new Usuario();
        newUsuario.setEmail(requestDTO.getEmail());
        newUsuario.setNombre(requestDTO.getNombre());

        //cifrado y guardado
        newUsuario.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
    }
}
