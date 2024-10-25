package pe.gob.pj.prueba.infraestructure.rest.responses;

import java.io.Serializable;

import lombok.Data;
import pe.gob.pj.prueba.domain.enums.Errors;

@Data
abstract class GlobalResponse implements Serializable {
  /**
  * 
  */
  private static final long serialVersionUID = 1L;

  private String codigo;
  private String descripcion;
  private String codigoOperacion;

  GlobalResponse() {
    this.codigo = Errors.OPERACION_EXITOSA.getCodigo();
    this.descripcion = Errors.OPERACION_EXITOSA.getNombre();
  }
  
  public void errorInesperado(String proceso) {
    this.codigo = Errors.ERROR_INESPERADO.getCodigo();
    this.descripcion = String.format(Errors.ERROR_INESPERADO.getNombre(), proceso);
  }

}
