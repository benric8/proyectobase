package pe.gob.pj.prueba.domain.model.seguridad.query;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Builder
@Accessors(fluent = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutenticacionUsuarioQuery {

  String usuario;
  String clave;
  String codigoRol;
  String codigoCliente;

}
