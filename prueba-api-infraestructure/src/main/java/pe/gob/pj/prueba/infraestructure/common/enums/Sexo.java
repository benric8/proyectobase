package pe.gob.pj.prueba.infraestructure.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sexo {
	
	MASCULIO("M","Masculino"), FEMENINO("F","Femenino");
	
	private final String nombre;
	private final String descripcion;
}
