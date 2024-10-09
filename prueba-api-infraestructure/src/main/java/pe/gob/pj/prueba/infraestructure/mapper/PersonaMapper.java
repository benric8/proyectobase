package pe.gob.pj.prueba.infraestructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import pe.gob.pj.prueba.domain.model.servicio.Persona;
import pe.gob.pj.prueba.infraestructure.rest.request.PersonaRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonaMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "tipoDocumento", ignore = true)
  Persona toPersona(PersonaRequest personaRequest);

}
