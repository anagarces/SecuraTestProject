package com.cybersecurityApp.cybersecurity_App.controller;

import com.cybersecurityApp.cybersecurity_App.model.Role;
import com.cybersecurityApp.cybersecurity_App.model.Usuario;
import com.cybersecurityApp.cybersecurity_App.model.dao.UserDao;
import com.cybersecurityApp.cybersecurity_App.model.dto.UsuarioDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserAdminController(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // LISTAR TODOS LOS USUARIOS
    // =========================
    @GetMapping
    public List<UsuarioDTO> getAllUsers() {
        return userDao.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // =========================
    // OBTENER USUARIO POR ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUser(@PathVariable Long id) {
        return userDao.findById(id)
                .map(u -> ResponseEntity.ok(convertToDTO(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // CREAR USUARIO MANUALMENTE (ADMIN)
    // =========================
    @PostMapping
    public ResponseEntity<UsuarioDTO> createUser(@RequestBody UsuarioDTO data) {

        if (data.getEmail() == null || data.getEmail().isBlank())
            return ResponseEntity.badRequest().body(null);

        Usuario user = new Usuario();
        user.setEmail(data.getEmail());
        user.setNombre(data.getNombre());
        user.setActive(true);

        // Rol validado
        try {
            user.setRole(Role.valueOf(data.getRole()));
        } catch (Exception e) {
            user.setRole(Role.USER); // por defecto
        }

        // Password temporal autogenerada
        user.setPassword(passwordEncoder.encode("123456"));

        Usuario saved = userDao.save(user);

        return ResponseEntity.ok(convertToDTO(saved));
    }

    // =========================
    // ACTUALIZAR DATOS GENERALES DEL USUARIO
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UsuarioDTO dto) {

        return userDao.findById(id)
                .map(u -> {

                    u.setEmail(dto.getEmail());
                    u.setNombre(dto.getNombre());
                    // NOTA: No se cambian password ni rol desde aquí

                    userDao.save(u);

                    return ResponseEntity.ok(convertToDTO(u));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // ACTUALIZAR SOLO EL ROL
    // =========================
    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String newRole = body.get("role");

        if (newRole == null)
            return ResponseEntity.badRequest().body("Campo 'role' obligatorio");

        return userDao.findById(id)
                .map(u -> {
                    try {
                        u.setRole(Role.valueOf(newRole));
                        userDao.save(u);
                        return ResponseEntity.ok(convertToDTO(u));

                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("Rol inválido");
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // DESACTIVAR USUARIO (NO ELIMINAR)
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        return userDao.findById(id)
                .map(u -> {
                    u.setActive(false);
                    userDao.save(u);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // Metodo auxiliar
    // =========================
    private UsuarioDTO convertToDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getId());
        dto.setEmail(u.getEmail());
        dto.setNombre(u.getNombre());
        dto.setRole(u.getRole().name());
        dto.setActive(u.isActive());
        return dto;
    }

    @PutMapping("/{id}/reactivate")
    public ResponseEntity<?> reactivate(@PathVariable Long id) {
        return userDao.findById(id)
                .map(u -> {
                    u.setActive(true);
                    userDao.save(u);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
