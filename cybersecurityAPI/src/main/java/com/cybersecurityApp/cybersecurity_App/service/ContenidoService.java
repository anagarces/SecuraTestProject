package com.cybersecurityApp.cybersecurity_App.service;


import com.cybersecurityApp.cybersecurity_App.api.IContenidoService;
import com.cybersecurityApp.cybersecurity_App.model.Contenido;
import com.cybersecurityApp.cybersecurity_App.model.dao.ContenidoDao;
import com.cybersecurityApp.cybersecurity_App.model.dto.ContenidoDTO;
import com.cybersecurityApp.cybersecurity_App.model.dto.dtomapper.ContenidoMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Lazy
public class ContenidoService implements IContenidoService {

    @Autowired
    private ContenidoDao contenidoDao;

    @Override
    public ContenidoDTO queryContenido(ContenidoDTO contenidoDTO){

        Contenido contenido = ContenidoMapper.INSTANCE.toEntity(contenidoDTO);
        return  ContenidoMapper.INSTANCE.toDTO(contenidoDao.getReferenceById(contenido.getIdContenido()));
    };

    @Override
    public List<ContenidoDTO> queryAllContenidos(){
        return ContenidoMapper.INSTANCE.toDTOList(contenidoDao.findAll());
    };

    @Override
    public int insertContenido (ContenidoDTO contenidoDTO){
        Contenido contenido = ContenidoMapper.INSTANCE.toEntity(contenidoDTO);
        contenidoDao.saveAndFlush(contenido);

        return  contenido.getIdContenido();
    }

    @Override
    public int updateContenido (ContenidoDTO contenidoDTO){
        return insertContenido(contenidoDTO);
    };

    @Override
    public int deleteContenido (ContenidoDTO contenidoDTO){
        int id = contenidoDTO.getIdContenido();
        Contenido contenido = ContenidoMapper.INSTANCE.toEntity(contenidoDTO);
        contenidoDao.delete(contenido);
        return id;
    };

    @Override
    @Transactional // Es una operación de solo lectura, pero @Transactional es buena práctica
    public ContenidoDTO getContenidoById(Integer id){
        Contenido contenidoEncontrado = contenidoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Contenido no encontrado con id: " + id));

        return ContenidoMapper.INSTANCE.toDTO(contenidoEncontrado);
    }

    @Override
    @Transactional // Es una operación de lectura y escritura, por lo que es transaccional
    public ContenidoDTO patchContenido(Integer id, ContenidoDTO patchDTO) {
        // 1. Obtener la entidad existente de la BBDD.
        // Si no la encuentra, .orElseThrow lanzará una excepción y la operación se detendrá.
        Contenido existingContenido = contenidoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado - Contenido con id: " + id));

        // 2. Aplicar los cambios del DTO SOLO SI los campos no son nulos
        if (patchDTO.getTitulo() != null) {
            existingContenido.setTitulo(patchDTO.getTitulo());
        }
        if (patchDTO.getCuerpo() != null) {
            existingContenido.setCuerpo(patchDTO.getCuerpo());
        }
        if (patchDTO.getTema() != null) {
            existingContenido.setTema(patchDTO.getTema());
        }
        if (patchDTO.getNivelDificultad() != null) {
            existingContenido.setNivelDificultad(patchDTO.getNivelDificultad());
        }

        // 3. Guardar la entidad con los campos ya actualizados
        Contenido updatedContenido = contenidoDao.save(existingContenido);

        // 4. Devolver el DTO del objeto completo y actualizado
        return ContenidoMapper.INSTANCE.toDTO(updatedContenido);
    }

}
