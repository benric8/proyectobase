package pe.gob.pj.prueba.infraestructure.db.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.common.utils.ProjectConstants;
import pe.gob.pj.prueba.domain.common.utils.ProjectUtils;
import pe.gob.pj.prueba.domain.model.negocio.Persona;
import pe.gob.pj.prueba.domain.model.negocio.query.ConsultarPersonaQuery;
import pe.gob.pj.prueba.domain.port.persistence.GestionPersonaPersistencePort;
import pe.gob.pj.prueba.infraestructure.common.enums.Estado;
import pe.gob.pj.prueba.infraestructure.db.negocio.entities.MaeTipoDocumentoPersonaEntity;
import pe.gob.pj.prueba.infraestructure.db.negocio.entities.MovPersonaEntity;
import pe.gob.pj.prueba.infraestructure.db.negocio.repositories.MaeTipoDocumentoRepository;
import pe.gob.pj.prueba.infraestructure.db.negocio.repositories.MovPersonaRepository;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GestionPersonaPersistenceAdapter implements GestionPersonaPersistencePort {

  final MovPersonaRepository movPersonaRepository;
  final MaeTipoDocumentoRepository maeTipoDocumentoRepository;

  @Override
  public List<Persona> buscarPersona(String cuo, ConsultarPersonaQuery filters) {
    return movPersonaRepository.buscarPersona(filters);
  }

  @Override
  public void registrarPersona(String cuo, Persona persona) {
    var maeTipoDocumento =
        maeTipoDocumentoRepository.findById(persona.getIdTipoDocumento()).orElseThrow();
    var movPersona = new MovPersonaEntity();
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
    movPersonaRepository.save(movPersona);
    persona.setId(movPersona.getId());
  }

  @Override
  public void actualizarPersona(String cuo, Persona persona) {

    var movPersona2 = movPersonaRepository.findById(persona.getId());
    movPersona2.ifPresent(mov -> {
      if (!mov.getTipoDocumento().getCodigo().equals(persona.getIdTipoDocumento())) {
        var maeTipoDocumento = new MaeTipoDocumentoPersonaEntity();
        maeTipoDocumento.setCodigo(persona.getIdTipoDocumento());
        mov.setTipoDocumento(maeTipoDocumento);
      }
      mov.setNumeroDocumento(persona.getNumeroDocumento());
      mov.setPrimerApellido(persona.getPrimerApellido());
      mov.setSegundoApellido(persona.getSegundoApellido());
      mov.setNombres(persona.getNombres());
      mov.setSexo(persona.getSexo());
      mov.setCorreo(persona.getCorreo());
      mov.setTelefono(persona.getTelefono());
      mov.setFechaNacimiento(ProjectUtils.parseStringToDate(persona.getFechaNacimiento(),
          ProjectConstants.Formato.FECHA_DD_MM_YYYY));
      mov.setActivo(!Estado.INACTIVO_NUMERICO.getNombre().equals(persona.getActivo())
          ? Estado.ACTIVO_NUMERICO.getNombre()
          : Estado.INACTIVO_NUMERICO.getNombre());
    });

  }

}
