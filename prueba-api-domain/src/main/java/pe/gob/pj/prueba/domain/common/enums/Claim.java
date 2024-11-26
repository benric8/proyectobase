package pe.gob.pj.prueba.domain.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Claim {

	ROLES("rols"),
	ROL_SELECCIONADO("rolSeleccionado"),
	USUARIO_REALIZA_PETICION("usuario"),
	IP_REALIZA_PETICION("remoteIp"),
	LIMITE_TOKEN("limit");
	
	private final String nombre;
	
}
