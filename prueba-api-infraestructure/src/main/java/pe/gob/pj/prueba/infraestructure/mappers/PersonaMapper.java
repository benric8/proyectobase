package pe.gob.pj.prueba.infraestructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import pe.gob.pj.prueba.domain.model.negocio.Persona;
import pe.gob.pj.prueba.infraestructure.rest.requests.PersonaRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonaMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "tipoDocumento", ignore = true)
  Persona toPersona(PersonaRequest personaRequest);

}
