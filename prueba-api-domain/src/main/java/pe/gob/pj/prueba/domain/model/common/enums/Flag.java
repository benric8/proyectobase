package pe.gob.pj.prueba.domain.model.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Flag {
  
  SI("S"), NO("N");

  private final String codigo;

}
