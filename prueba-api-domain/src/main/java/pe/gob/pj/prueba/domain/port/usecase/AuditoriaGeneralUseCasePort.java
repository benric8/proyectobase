package pe.gob.pj.prueba.domain.port.usecase;

import pe.gob.pj.prueba.domain.model.auditoriageneral.AuditoriaAplicativos;

public interface AuditoriaGeneralUseCasePort {
	public void crear(String cuo, AuditoriaAplicativos auditoriaAplicativos) throws Exception;
}
