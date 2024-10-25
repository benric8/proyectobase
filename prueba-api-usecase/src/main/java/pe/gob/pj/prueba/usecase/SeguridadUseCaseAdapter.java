package pe.gob.pj.prueba.usecase;

import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.model.seguridad.Rol;
import pe.gob.pj.prueba.domain.model.seguridad.Usuario;
import pe.gob.pj.prueba.domain.model.seguridad.query.AutenticacionUsuarioQuery;
import pe.gob.pj.prueba.domain.port.persistence.SeguridadPersistencePort;
import pe.gob.pj.prueba.domain.port.usecase.SeguridadUseCasePort;

@Service("seguridadUseCasePort")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeguridadUseCaseAdapter implements SeguridadUseCasePort {

  final SeguridadPersistencePort seguridadPersistencePort;

  @Override
  @Transactional(transactionManager = "txManagerSeguridad", propagation = Propagation.REQUIRED,
      readOnly = true, rollbackFor = {Exception.class, SQLException.class})
  public String autenticarUsuario(String cuo, AutenticacionUsuarioQuery query) throws Exception {
    return seguridadPersistencePort.autenticarUsuario(cuo, query);
  }

  @Override
  @Transactional(transactionManager = "txManagerSeguridad", propagation = Propagation.REQUIRED,
      readOnly = true, rollbackFor = {Exception.class, SQLException.class})
  public Usuario recuperaInfoUsuario(String cuo, String id) throws Exception {
    return seguridadPersistencePort.recuperaInfoUsuario(cuo, id);
  }

  @Override
  @Transactional(transactionManager = "txManagerSeguridad", propagation = Propagation.REQUIRED,
      readOnly = true, rollbackFor = {Exception.class, SQLException.class})
  public List<Rol> recuperarRoles(String cuo, String id) throws Exception {
    return seguridadPersistencePort.recuperarRoles(cuo, id);
  }

  @Override
  @Transactional(transactionManager = "txManagerSeguridad", propagation = Propagation.REQUIRED,
      readOnly = true, rollbackFor = {Exception.class, SQLException.class})
  public String validarAccesoMetodo(String cuo, String usuario, String rol, String operacion)
      throws Exception {
    return seguridadPersistencePort.validarAccesoMetodo(cuo, usuario, rol, operacion);
  }

}
