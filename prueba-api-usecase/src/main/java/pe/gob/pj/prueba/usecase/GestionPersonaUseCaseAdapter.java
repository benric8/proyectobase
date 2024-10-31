package pe.gob.pj.prueba.usecase;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.exceptions.DatosNoEncontradosException;
import pe.gob.pj.prueba.domain.exceptions.PersonaYaExisteException;
import pe.gob.pj.prueba.domain.model.servicio.Persona;
import pe.gob.pj.prueba.domain.model.servicio.query.ConsultarPersonaQuery;
import pe.gob.pj.prueba.domain.port.persistence.GestionPersonaPersistencePort;
import pe.gob.pj.prueba.domain.port.usecase.GestionPersonaUseCasePort;

@Service("gestionPersonaUseCasePort")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GestionPersonaUseCaseAdapter implements GestionPersonaUseCasePort {

  final GestionPersonaPersistencePort gestionPersonaPersistencePort;

  @Override
  @Transactional(transactionManager = "txManagerNegocio", propagation = Propagation.REQUIRES_NEW,
      readOnly = true, rollbackFor = {Exception.class, SQLException.class})
  public List<Persona> buscarPersona(String cuo, ConsultarPersonaQuery filters) {
    var lista = gestionPersonaPersistencePort.buscarPersona(cuo, filters);
    if (lista.isEmpty()) {
      throw new DatosNoEncontradosException();
    }
    return lista;
  }

  @Override
  @Transactional(transactionManager = "txManagerNegocio", propagation = Propagation.REQUIRES_NEW,
      readOnly = false, rollbackFor = {Exception.class, SQLException.class})
  public void registrarPersona(String cuo, Persona persona) {
    var filters = new HashMap<String, Object>();
    filters.put(Persona.P_NUMERO_DOCUMENTO, persona.getNumeroDocumento());
    if (!gestionPersonaPersistencePort.buscarPersona(cuo,
        ConsultarPersonaQuery.builder().documentoIdentidad(persona.getNumeroDocumento()).build())
        .isEmpty()) {
      throw new PersonaYaExisteException();
    }
    gestionPersonaPersistencePort.registrarPersona(cuo, persona);
  }

  @Override
  @Transactional(transactionManager = "txManagerNegocio", propagation = Propagation.REQUIRES_NEW,
      readOnly = false, rollbackFor = {Exception.class, SQLException.class})
  public void actualizarPersona(String cuo, Persona persona) {
    gestionPersonaPersistencePort.actualizarPersona(cuo, persona);
  }

}
