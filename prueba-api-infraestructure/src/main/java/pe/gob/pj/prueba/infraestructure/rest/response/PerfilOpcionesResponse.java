package pe.gob.pj.prueba.infraestructure.rest.response;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.model.servicio.PerfilOpcions;

@EqualsAndHashCode(callSuper = true)
@Getter @Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PerfilOpcionesResponse extends GlobalResponse implements Serializable {

  static final long serialVersionUID = 1L;
  
  PerfilOpcions data;

}
