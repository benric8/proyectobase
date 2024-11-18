package pe.gob.pj.prueba.domain.model.auditoriageneral;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter @Setter
@ToString
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
