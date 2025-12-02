package com.cybersecurityApp.cybersecurity_App.controller;

import com.cybersecurityApp.cybersecurity_App.model.Role; // Asegúrate de tener este Enum creado
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

    @GetMapping
    public List<UsuarioDTO> getAll() {
        return userDao.findAll().stream().map(this::convertToDTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable Long id) { // ID es Long
        return userDao.findById(id)
                .map(u -> ResponseEntity.ok(convertToDTO(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> create(@RequestBody UsuarioDTO data) {
        Usuario u = new Usuario();
        u.setEmail(data.getEmail());
        u.setNombre(data.getNombre());

        // Conversión segura de String (DTO) a Enum (Entidad)
        try {
            u.setRole(Role.valueOf(data.getRole()));
        } catch (Exception e) {
            u.setRole(Role.USER); // Rol por defecto si envían algo raro
        }

        u.setActive(true);
        // Password temporal
        u.setPassword(passwordEncoder.encode("123456"));

        Usuario saved = userDao.save(u);

        data.setId(saved.getId());
        return ResponseEntity.ok(data);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> roleBody) {

        String newRoleStr = roleBody.get("role");

        return userDao.findById(id)
                .map(u -> {
                    try {
                        u.setRole(Role.valueOf(newRoleStr));
                        userDao.save(u);
                        return ResponseEntity.ok().build();
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("Rol no válido");
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UsuarioDTO dto) {

        return userDao.findById(id)
                .map(u -> {
                    u.setEmail(dto.getEmail());
                    u.setNombre(dto.getNombre());
                    // Nota: No actualizamos password ni rol aquí
                    userDao.save(u);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> disable(@PathVariable Long id) {
        return userDao.findById(id)
                .map(u -> {
                    u.setActive(false);
                    userDao.save(u);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Método auxiliar para no repetir código de mapeo
    private UsuarioDTO convertToDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getId());
        dto.setEmail(u.getEmail());
        dto.setNombre(u.getNombre());
        dto.setRole(u.getRole().name()); // Convertimos Enum a String
        dto.setActive(u.isActive());
        return dto;
    }
}
