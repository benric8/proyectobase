package pe.gob.pj.prueba.infraestructure.common.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum OperacionBaseDato {
	
	INSERTAR("I"), ACTUALIZAR("U"), ELIMINAR("D");
	
	String nombre;
	
	OperacionBaseDato(String nombre){
		this.nombre = nombre;
	}
	
}
