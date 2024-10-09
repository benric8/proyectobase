package pe.gob.pj.prueba.infraestructure.db.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pe.gob.pj.prueba.domain.model.auditoriageneral.AuditoriaAplicativos;
import pe.gob.pj.prueba.domain.port.persistence.AuditoriaGeneralPersistencePort;
import pe.gob.pj.prueba.infraestructure.db.auditoriageneral.entity.MovAuditoriaAplicativosEntity;
import pe.gob.pj.prueba.infraestructure.mapper.AuditoriaGeneralMapper;

@Component
public class AuditoriaGeneralPersistenceAdapter implements AuditoriaGeneralPersistencePort {

  @Autowired
  AuditoriaGeneralMapper auditoriaGeneralMapper;

  @PersistenceContext(unitName = "auditoria")
  private EntityManager entityManager;

  @Override
  public void crear(AuditoriaAplicativos auditoriaAplicativos) throws Exception {
    MovAuditoriaAplicativosEntity mov =
        auditoriaGeneralMapper.toMovAuditoriaAplicativos(auditoriaAplicativos);
    entityManager.persist(mov);
  }

}
