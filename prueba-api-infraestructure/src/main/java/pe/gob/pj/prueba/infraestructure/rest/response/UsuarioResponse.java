package pe.gob.pj.prueba.infraestructure.rest.response;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.model.servicio.Usuario;


@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsuarioResponse extends GlobalResponse implements Serializable {

  static final long serialVersionUID = 1L;
  
  Usuario data;

}
