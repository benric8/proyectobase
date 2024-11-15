package pe.gob.pj.prueba.domain.model.auditoriageneral;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class PeticionServicios {

  String cuo;
  String ip;
  String usuario;
  String uri;
  String params;
  String herramienta;
  String ips;
  String jwt;
  
}
