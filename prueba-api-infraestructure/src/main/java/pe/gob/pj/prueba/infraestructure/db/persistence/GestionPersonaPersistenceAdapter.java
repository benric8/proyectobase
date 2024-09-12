package pe.gob.pj.prueba.infraestructure.db.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pe.gob.pj.prueba.domain.model.servicio.Persona;
import pe.gob.pj.prueba.domain.port.persistence.GestionPersonaPersistencePort;
import pe.gob.pj.prueba.domain.utils.ProjectConstants;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;
import pe.gob.pj.prueba.infraestructure.db.entity.servicio.MaeTipoDocumentoPersona;
import pe.gob.pj.prueba.infraestructure.db.entity.servicio.MovPersona;
import pe.gob.pj.prueba.infraestructure.enums.Estado;

@Component("gestionPersonaPersistencePort")
public class GestionPersonaPersistenceAdapter implements GestionPersonaPersistencePort {

  @PersistenceContext(unitName = "negocio")
  private EntityManager entityManager;

  @Override
  public List<Persona> buscarPersona(String cuo, Map<String, Object> filters) throws Exception {
    var lista = new ArrayList<Persona>();
    if (!ProjectUtils.isNullOrEmpty(filters.get(Persona.P_NUMERO_DOCUMENTO))) {
      entityManager.unwrap(Session.class).enableFilter(MovPersona.F_DOCUMENTO_IDENTIDAD)
          .setParameter(MovPersona.P_DOCUMENTO_IDENTIDAD, filters.get(Persona.P_NUMERO_DOCUMENTO));
    }
    TypedQuery<MovPersona> query =
        entityManager.createNamedQuery(MovPersona.Q_ALL, MovPersona.class);
    query.getResultStream().forEach(movUsuario -> {
      var personaDto = new Persona();
      personaDto.setId(movUsuario.getId());
      personaDto.setPrimerApellido(movUsuario.getPrimerApellido());
      personaDto.setSegundoApellido(movUsuario.getSegundoApellido());
      personaDto.setNombres(movUsuario.getNombres());
      personaDto.setNumeroDocumento(movUsuario.getNumeroDocumento());
      personaDto.setTelefono(movUsuario.getTelefono());
      personaDto.setCorreo(movUsuario.getCorreo());
      personaDto.setIdTipoDocumento(movUsuario.getTipoDocumento().getCodigo());
      personaDto.setTipoDocumento(movUsuario.getTipoDocumento().getAbreviatura());
      personaDto.setFechaNacimiento(ProjectUtils.convertDateToString(
          movUsuario.getFechaNacimiento(), ProjectConstants.Formato.FECHA_DD_MM_YYYY));
      personaDto.setSexo(movUsuario.getSexo());
      personaDto.setActivo(movUsuario.getActivo());
      lista.add(personaDto);
    });
    return lista;
  }

  @Override
  public void registrarPersona(String cuo, Persona persona) throws Exception {
    var maeTipoDocumento = new MaeTipoDocumentoPersona();
    maeTipoDocumento.setCodigo(persona.getIdTipoDocumento());
    var movPersona = new MovPersona();
    movPersona.setTipoDocumento(maeTipoDocumento);
    movPersona.setNumeroDocumento(persona.getNumeroDocumento());
    movPersona.setPrimerApellido(persona.getPrimerApellido());
    movPersona.setSegundoApellido(persona.getSegundoApellido());
    movPersona.setNombres(persona.getNombres());
    movPersona.setSexo(persona.getSexo());
    movPersona.setCorreo(persona.getCorreo());
    movPersona.setTelefono(persona.getTelefono());
    movPersona.setFechaNacimiento(ProjectUtils.parseStringToDate(persona.getFechaNacimiento(),
        ProjectConstants.Formato.FECHA_DD_MM_YYYY));
    movPersona.setActivo(!Estado.INACTIVO_NUMERICO.getNombre().equals(persona.getActivo())
        ? Estado.ACTIVO_NUMERICO.getNombre()
        : Estado.INACTIVO_NUMERICO.getNombre());
    entityManager.persist(movPersona);
    persona.setId(movPersona.getId());
  }

  @Override
  public void actualizarPersona(String cuo, Persona persona) throws Exception {
    entityManager.unwrap(Session.class).enableFilter(MovPersona.F_ID).setParameter(MovPersona.P_ID,
        persona.getId());
    TypedQuery<MovPersona> query =
        entityManager.createNamedQuery(MovPersona.Q_ALL, MovPersona.class);
    var movPersona = query.getSingleResult();
    if (!movPersona.getTipoDocumento().getCodigo().equals(persona.getIdTipoDocumento())) {
      var maeTipoDocumento = new MaeTipoDocumentoPersona();
      maeTipoDocumento.setCodigo(persona.getIdTipoDocumento());
      movPersona.setTipoDocumento(maeTipoDocumento);
    }
    movPersona.setNumeroDocumento(persona.getNumeroDocumento());
    movPersona.setPrimerApellido(persona.getPrimerApellido());
    movPersona.setSegundoApellido(persona.getSegundoApellido());
    movPersona.setNombres(persona.getNombres());
    movPersona.setSexo(persona.getSexo());
    movPersona.setCorreo(persona.getCorreo());
    movPersona.setTelefono(persona.getTelefono());
    movPersona.setFechaNacimiento(ProjectUtils.parseStringToDate(persona.getFechaNacimiento(),
        ProjectConstants.Formato.FECHA_DD_MM_YYYY));
    movPersona.setActivo(!Estado.INACTIVO_NUMERICO.getNombre().equals(persona.getActivo())
        ? Estado.ACTIVO_NUMERICO.getNombre()
        : Estado.INACTIVO_NUMERICO.getNombre());
    entityManager.merge(movPersona);
  }

}
