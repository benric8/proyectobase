package pe.gob.pj.prueba.infraestructure.rest.responses;

import java.io.Serializable;
import lombok.Data;
import pe.gob.pj.prueba.infraestructure.common.enums.TipoError;

@Data
public class GlobalResponse implements Serializable {
  /**
  * 
  */
  private static final long serialVersionUID = 1L;

  private String codigo;
  private String descripcion;
  private String codigoOperacion;

  public GlobalResponse() {
    this.codigo = TipoError.OPERACION_EXITOSA.getCodigo();
    this.descripcion = TipoError.OPERACION_EXITOSA.getDescripcion();
  }

}
