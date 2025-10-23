package com.cybersecurityApp.cybersecurity_App.api;

import com.cybersecurityApp.cybersecurity_App.model.dto.ContenidoDTO;

import java.util.List;

public interface IContenidoService {

    //Operaciones CRUD
    ContenidoDTO queryContenido(ContenidoDTO contenidoDTO);
    List<ContenidoDTO> queryAllContenidos();
    int insertContenido (ContenidoDTO contenidoDTO);
    int updateContenido (ContenidoDTO contenidoDTO);
    int deleteContenido (ContenidoDTO contenidoDTO);
    ContenidoDTO patchContenido(Integer id, ContenidoDTO contenidoPatchDTO);
    ContenidoDTO getContenidoById(Integer id);
}
