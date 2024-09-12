package pe.gob.pj.prueba.usecase;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.enums.Errors;
import pe.gob.pj.prueba.domain.enums.Proceso;
import pe.gob.pj.prueba.domain.exceptions.ErrorException;
import pe.gob.pj.prueba.domain.model.servicio.Persona;
import pe.gob.pj.prueba.domain.port.persistence.GestionPersonaPersistencePort;
import pe.gob.pj.prueba.domain.port.usecase.GestionPersonaUseCasePort;

@Service("gestionPersonaUseCasePort")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GestionPersonaUseCaseAdapter implements GestionPersonaUseCasePort {

  @Qualifier("gestionPersonaPersistencePort")
  final GestionPersonaPersistencePort gestionPersonaPersistencePort;

  @Override
  @Transactional(transactionManager = "txManagerNegocio", propagation = Propagation.REQUIRES_NEW,
      readOnly = true, rollbackFor = {Exception.class, SQLException.class})
  public List<Persona> buscarPersona(String cuo, Map<String, Object> filters) throws Exception {
    var lista = gestionPersonaPersistencePort.buscarPersona(cuo, filters);
    if (lista.isEmpty()) {
      throw new ErrorException(Errors.DATOS_NO_ENCONTRADOS.getCodigo(), String
          .format(Errors.DATOS_NO_ENCONTRADOS.getNombre(), Proceso.PERSONA_CONSULTAR.getNombre()));
    }
    return lista;
  }

  @Override
  @Transactional(transactionManager = "txManagerNegocio", propagation = Propagation.REQUIRES_NEW,
      readOnly = false, rollbackFor = {Exception.class, SQLException.class})
  public void registrarPersona(String cuo, Persona persona) throws Exception {
    var filters = new HashMap<String, Object>();
    filters.put(Persona.P_NUMERO_DOCUMENTO, persona.getNumeroDocumento());
    if (!gestionPersonaPersistencePort.buscarPersona(cuo, filters).isEmpty()) {
      throw new ErrorException(Errors.NEGOCIO_PERSONA_YA_REGISTRADA.getCodigo(), String.format(
          Errors.NEGOCIO_PERSONA_YA_REGISTRADA.getNombre(), Proceso.PERSONA_REGISTRAR.getNombre()));
    }
    gestionPersonaPersistencePort.registrarPersona(cuo, persona);
  }

  @Override
  @Transactional(transactionManager = "txManagerNegocio", propagation = Propagation.REQUIRES_NEW,
      readOnly = false, rollbackFor = {Exception.class, SQLException.class})
  public void actualizarPersona(String cuo, Persona persona) throws Exception {
    gestionPersonaPersistencePort.actualizarPersona(cuo, persona);
  }

}
