package pe.gob.pj.prueba.infraestructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import pe.gob.pj.prueba.domain.model.servicio.Persona;
import pe.gob.pj.prueba.infraestructure.rest.request.PersonaRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonaMapper {

  Persona toPersona(PersonaRequest personaRequest);

}
