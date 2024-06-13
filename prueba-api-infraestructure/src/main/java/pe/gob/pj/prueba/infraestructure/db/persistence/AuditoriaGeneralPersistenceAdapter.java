package pe.gob.pj.prueba.infraestructure.db.persistence;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import pe.gob.pj.prueba.domain.model.auditoriageneral.AuditoriaAplicativos;
import pe.gob.pj.prueba.domain.port.persistence.AuditoriaGeneralPersistencePort;
import pe.gob.pj.prueba.infraestructure.db.entity.auditoriageneral.MovAuditoriaAplicativosEntity;
import pe.gob.pj.prueba.infraestructure.mapper.AuditoriaGeneralMapper;

@Component("auditoriaGeneralPersistencePort")
public class AuditoriaGeneralPersistenceAdapter implements AuditoriaGeneralPersistencePort {

	@Autowired
	AuditoriaGeneralMapper auditoriaGeneralMapper;

	@Autowired
	@Qualifier("sessionAuditoriaGeneral")
	SessionFactory sessionFactory;

	@Override
	public void crear(AuditoriaAplicativos auditoriaAplicativos) throws Exception {
		MovAuditoriaAplicativosEntity mov = auditoriaGeneralMapper.toMovAuditoriaAplicativos(auditoriaAplicativos);
		sessionFactory.getCurrentSession().save(mov);
	}

}
