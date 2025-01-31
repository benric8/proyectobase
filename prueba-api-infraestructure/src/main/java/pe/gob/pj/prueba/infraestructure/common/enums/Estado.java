package pe.gob.pj.prueba.infraestructure.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Estado {

	ACTIVO_NUMERICO("1", "Flag númerico activo."), INACTIVO_NUMERICO("0", "Flag númerico inactivo"),
	ACTIVO_LETRA("S", "Flag letra activo."), INACTIVO_LETRA("N", "Flag letra inactivo");

	private final String nombre;
	private final String descripcion;

}
