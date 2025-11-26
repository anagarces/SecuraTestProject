package com.cybersecurityApp.cybersecurity_App.controller;


import com.cybersecurityApp.cybersecurity_App.model.Contenido;
import com.cybersecurityApp.cybersecurity_App.model.dao.ContenidoDao;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/contenidos")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('ADMIN')")
public class ContenidoAdminController {

    private final ContenidoDao contenidoDao;

    public ContenidoAdminController(ContenidoDao contenidoDao) {
        this.contenidoDao = contenidoDao;
    }

    @GetMapping
    public List<Contenido> getAll() {
        return contenidoDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contenido> getById(@PathVariable Long id) {
        return contenidoDao.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Contenido create(@RequestBody Contenido contenido) {
        return contenidoDao.save(contenido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contenido> update(
            @PathVariable Long id,
            @RequestBody Contenido data) {

        return contenidoDao.findById(id)
                .map(c -> {
                    c.setTitulo(data.getTitulo());
                    c.setCuerpo(data.getCuerpo());
                    c.setTema(data.getTema());
                    c.setNivelDificultad(data.getNivelDificultad());
                    return ResponseEntity.ok(contenidoDao.save(c));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contenidoDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
