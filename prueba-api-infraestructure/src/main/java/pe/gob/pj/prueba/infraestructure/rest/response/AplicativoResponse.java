package pe.gob.pj.prueba.infraestructure.rest.response;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.model.Aplicativo;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AplicativoResponse extends GlobalResponse implements Serializable {

  private static final long serialVersionUID = 1L;
  Aplicativo data;
  
}
