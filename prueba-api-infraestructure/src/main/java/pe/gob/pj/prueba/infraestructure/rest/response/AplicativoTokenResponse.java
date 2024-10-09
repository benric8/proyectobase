package pe.gob.pj.prueba.infraestructure.rest.response;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.model.AplicativoToken;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AplicativoTokenResponse extends GlobalResponse implements Serializable{
  
  private static final long serialVersionUID = 1L;
  
  AplicativoToken data;

}
