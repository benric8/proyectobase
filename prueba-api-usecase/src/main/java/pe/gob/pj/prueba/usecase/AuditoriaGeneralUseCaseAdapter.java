package pe.gob.pj.prueba.usecase;


import java.sql.SQLException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.prueba.domain.model.auditoriageneral.AuditoriaAplicativos;
import pe.gob.pj.prueba.domain.port.persistence.AuditoriaGeneralPersistencePort;
import pe.gob.pj.prueba.domain.port.usecase.AuditoriaGeneralUseCasePort;

@Service("auditoriaGeneralUseCasePort")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AuditoriaGeneralUseCaseAdapter implements AuditoriaGeneralUseCasePort {

  final AuditoriaGeneralPersistencePort auditoriaGeneralPersistencePort;

  @Async("poolTask")
  @Override
  @Transactional(transactionManager = "txManagerAuditoriaGeneral",
      propagation = Propagation.REQUIRED, readOnly = false,
      rollbackFor = {Exception.class, SQLException.class})
  public void crear(String cuo, AuditoriaAplicativos auditoriaAplicativos) {
    try {
      this.auditoriaGeneralPersistencePort.crear(auditoriaAplicativos);
    } catch (Exception e) {
      log.error(
          "{} No se pudo guardar la trazabilidad {} en auditoria_general debido al error [{}] ",
          cuo, auditoriaAplicativos.toString(), e);
    }
  }

}
