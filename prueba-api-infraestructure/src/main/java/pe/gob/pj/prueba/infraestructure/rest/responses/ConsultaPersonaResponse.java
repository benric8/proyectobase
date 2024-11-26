package pe.gob.pj.prueba.infraestructure.rest.responses;

import java.io.Serializable;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.model.negocio.Persona;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultaPersonaResponse extends GlobalResponse implements Serializable{
  
  private static final long serialVersionUID = 1L;
  
  List<Persona> data;

}
