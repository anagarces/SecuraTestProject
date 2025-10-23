package com.cybersecurityApp.cybersecurity_App.controller;

import com.cybersecurityApp.cybersecurity_App.model.dto.ContenidoDTO;
import com.cybersecurityApp.cybersecurity_App.api.IContenidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contenidos")
public class ContenidoPublicoController {

    @Autowired
    private IContenidoService contenidoService;

    @GetMapping
    public String testController(){
        return "Contenidos controller works!";
    }

    @PostMapping
    public String testControllerPost(@RequestBody String name){
        return "Contenidos controller POST works! " + name;
    }

    @GetMapping(value = "/testMethod")
    public String testControllerMethod(){
        return "Contenidos controller with sub-route works!";
    }

    //mantener este endpoint controlado para futuras pruebas
    @PostMapping(value = "/get")
    public ContenidoDTO queryContenido(@RequestBody ContenidoDTO contenidoDTO){
        return contenidoService.queryContenido(contenidoDTO);
    }

    @GetMapping(value = "/getAll")
    public List<ContenidoDTO> queryAllContenidos(){
        return contenidoService.queryAllContenidos();
    }

    @PostMapping(value = "/add")
    public int addContenido(@RequestBody ContenidoDTO contenidoDTO){
        return contenidoService.insertContenido(contenidoDTO);
    }

    @PutMapping(value = "/update")
    public int updateContenido(@RequestBody ContenidoDTO contenidoDTO){
        return contenidoService.updateContenido(contenidoDTO);
    }

    @DeleteMapping(value = "/delete")
    public int deleteContenido(@RequestBody ContenidoDTO contenidoDTO){
        return contenidoService.deleteContenido(contenidoDTO);
    }

    /*Ideal para modificar propiedades especificas*/
    @PatchMapping("/{id}")
    public ResponseEntity<ContenidoDTO> patchContenido(@PathVariable Integer id, @RequestBody ContenidoDTO contenidoPatchDTO) {
        ContenidoDTO updatedContenido = contenidoService.patchContenido(id, contenidoPatchDTO);
        // El servicio se encargará de lanzar una excepción si no lo encuentra,
        // pero podrías manejarlo aquí también si quisieras.
        return ResponseEntity.ok(updatedContenido);
    }

    /*obtener contenido por id*/
    @GetMapping("/{id}")
    public ContenidoDTO getContenidoById(@PathVariable Integer id){

        return contenidoService.getContenidoById(id);
    }

}
