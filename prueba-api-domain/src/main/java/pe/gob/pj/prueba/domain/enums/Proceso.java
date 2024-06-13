package pe.gob.pj.prueba.domain.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Proceso {
	
	HEALTHCHECK("Probar Disponibilidad"),
	REFRESH("Refrescar Token"),
	INICIAR_SESION("Iniciar Sesión"),
	ELEGIR_PERFIL("Elegir Perfil"),
	OBTENER_OPCIONES("Obtener Opciones"),
	PERSONA_CONSULTAR("Consultar Persona"),
	PERSONA_REGISTRAR("Registrar Persona"),
	PERSONA_ACTUALIZAR("Actualizar Persona");
	
	String nombre;
	
	Proceso(String nombre){
		this.nombre = nombre;
	}
	
}
