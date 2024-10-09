package pe.gob.pj.prueba.infraestructure.rest;

import pe.gob.pj.prueba.domain.exceptions.ErrorException;
import pe.gob.pj.prueba.infraestructure.utils.UtilInfraestructure;

public interface Base {

	public default void handleException(String cuo, ErrorException e) {
		UtilInfraestructure.handleException(cuo, e, e.getCodigo(), e.getDescripcion());
	}
	
}
