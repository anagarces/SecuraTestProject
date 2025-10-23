package com.cybersecurityApp.cybersecurity_App.model.dao;

import com.cybersecurityApp.cybersecurity_App.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
}
