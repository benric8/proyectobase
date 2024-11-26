package pe.gob.pj.prueba.infraestructure.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDefault {
	
	TRAZA("TRAZA_LOG"),
	CAUSA_NO_IDENTIFICADA("Causa: No se produjo una excepción, se lanzo error personalizado."),
	CLASE_METODO_LINEA_NO_IDENTIFICADO("Clase-Metodo-Linea: Un Error personalizado no maneja estos datos."),
	FORMATO_RESPUESTA_NO_IDENTIFICADO("Formato Respuesta: Error al convertir en formato xml.");
	
	private final String nombre;
	
}
