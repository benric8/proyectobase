package pe.gob.pj.prueba.domain.port.persistence;

import pe.gob.pj.prueba.domain.model.auditoriageneral.AuditoriaAplicativos;

public interface AuditoriaGeneralPersistencePort {
	public void crear(AuditoriaAplicativos auditoriaAplicativos) throws Exception;
}
