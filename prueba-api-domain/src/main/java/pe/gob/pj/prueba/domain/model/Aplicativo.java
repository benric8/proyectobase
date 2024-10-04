package pe.gob.pj.prueba.domain.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Aplicativo {

  String nombre;
  String estado;
  String version;
  
}
