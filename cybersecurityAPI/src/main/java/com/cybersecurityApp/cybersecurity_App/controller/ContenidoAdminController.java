package com.cybersecurityApp.cybersecurity_App.controller;


import com.cybersecurityApp.cybersecurity_App.model.Contenido;
import com.cybersecurityApp.cybersecurity_App.model.dao.ContenidoDao;
import com.cybersecurityApp.cybersecurity_App.model.dto.ContenidoDTO;
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
    public ResponseEntity<Contenido> getById(@PathVariable Integer id) {
        return contenidoDao.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Contenido> create(@RequestBody ContenidoDTO dto) {

        Contenido contenido = new Contenido();
        contenido.setTitulo(dto.getTitulo());
        contenido.setCuerpo(dto.getCuerpo());
        contenido.setTema(dto.getTema());
        contenido.setNivelDificultad(dto.getNivelDificultad());

        Contenido saved = contenidoDao.save(contenido);

        return ResponseEntity.ok(saved);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Contenido> update(
            @PathVariable Integer id,
            @RequestBody ContenidoDTO dto) {

        return contenidoDao.findById(id)
                .map(c -> {
                    c.setTitulo(dto.getTitulo());
                    c.setCuerpo(dto.getCuerpo());
                    c.setTema(dto.getTema());
                    c.setNivelDificultad(dto.getNivelDificultad());
                    return ResponseEntity.ok(contenidoDao.save(c));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        contenidoDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
