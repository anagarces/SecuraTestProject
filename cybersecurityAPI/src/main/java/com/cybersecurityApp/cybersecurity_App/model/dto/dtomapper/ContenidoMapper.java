package com.cybersecurityApp.cybersecurity_App.model.dto.dtomapper;

import com.cybersecurityApp.cybersecurity_App.model.Contenido;
import com.cybersecurityApp.cybersecurity_App.model.dto.ContenidoDTO;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapper;
//Hacemos uso de la libreria MapStruct. Automatiza la conversion de objetos entre la capa entidad y la capa DTO
import java.util.List;

@Mapper //anotacion que identifica a esta clase como un mapper
public interface ContenidoMapper {

    //creamos una instancia estatica del mapper
    ContenidoMapper INSTANCE = Mappers.getMapper(ContenidoMapper.class);

    //@Mapping(source = "id", target = "idContenido")
    ContenidoDTO toDTO(Contenido contenido);

    List<ContenidoDTO> toDTOList(List<Contenido> contenidos);

    Contenido toEntity(ContenidoDTO contenidoDTO);
}
