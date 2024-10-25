package pe.gob.pj.prueba.usecase;


import java.sql.SQLException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.enums.Errors;
import pe.gob.pj.prueba.domain.exceptions.ErrorException;
import pe.gob.pj.prueba.domain.model.auditoriageneral.AuditoriaAplicativos;
import pe.gob.pj.prueba.domain.port.persistence.AuditoriaGeneralPersistencePort;
import pe.gob.pj.prueba.domain.port.usecase.AuditoriaGeneralUseCasePort;

@Service("auditoriaGeneralUseCasePort")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditoriaGeneralUseCaseAdapter implements AuditoriaGeneralUseCasePort {

  final AuditoriaGeneralPersistencePort auditoriaGeneralPersistencePort;

  @Async("poolTask")
  @Override
  @Transactional(transactionManager = "txManagerAuditoriaGeneral",
      propagation = Propagation.REQUIRED, readOnly = false,
      rollbackFor = {Exception.class, SQLException.class})
  public void crear(String cuo, AuditoriaAplicativos auditoriaAplicativos) throws Exception {
    try {
      this.auditoriaGeneralPersistencePort.crear(auditoriaAplicativos);
    } catch (Exception e) {
      throw new ErrorException(Errors.ERROR_AUDITORIA_GENERAL_REGISTRAR.getCodigo(),
          Errors.ERROR_AUDITORIA_GENERAL_REGISTRAR.getNombre(), e);
    }
  }

}
