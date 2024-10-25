package pe.gob.pj.prueba.infraestructure.db.persistence;

import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.model.auditoriageneral.AuditoriaAplicativos;
import pe.gob.pj.prueba.domain.port.persistence.AuditoriaGeneralPersistencePort;
import pe.gob.pj.prueba.infraestructure.db.auditoriageneral.entities.MovAuditoriaAplicativosEntity;
import pe.gob.pj.prueba.infraestructure.mappers.AuditoriaGeneralMapper;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuditoriaGeneralPersistenceAdapter implements AuditoriaGeneralPersistencePort {

  AuditoriaGeneralMapper auditoriaGeneralMapper;
  @PersistenceContext(unitName = "auditoria")
  EntityManager entityManager;

  @Override
  public void crear(AuditoriaAplicativos auditoriaAplicativos) throws Exception {
    MovAuditoriaAplicativosEntity mov =
        auditoriaGeneralMapper.toMovAuditoriaAplicativos(auditoriaAplicativos);
    entityManager.persist(mov);
  }

}
