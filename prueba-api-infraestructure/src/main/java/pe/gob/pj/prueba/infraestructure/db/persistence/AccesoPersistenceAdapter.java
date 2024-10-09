package pe.gob.pj.prueba.infraestructure.db.persistence;

import java.util.Objects;
import org.springframework.stereotype.Component;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.model.servicio.Opcion;
import pe.gob.pj.prueba.domain.model.servicio.PerfilOpcions;
import pe.gob.pj.prueba.domain.model.servicio.PerfilUsuario;
import pe.gob.pj.prueba.domain.model.servicio.Usuario;
import pe.gob.pj.prueba.domain.port.persistence.AccesoPersistencePort;
import pe.gob.pj.prueba.domain.utils.ProjectConstants;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;
import pe.gob.pj.prueba.infraestructure.db.negocio.entity.MaeOpcion;
import pe.gob.pj.prueba.infraestructure.db.negocio.repository.MaePerfilRepository;
import pe.gob.pj.prueba.infraestructure.db.negocio.repository.MovUsuarioRepository;
import pe.gob.pj.prueba.infraestructure.enums.Estado;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AccesoPersistenceAdapter implements AccesoPersistencePort {

  final MovUsuarioRepository movUsuarioRepository;
  final MaePerfilRepository maePerfilRepository;

  @Override
  public Usuario iniciarSesion(String cuo, String usuario) throws Exception {
    Usuario usuarioDTO = new Usuario();
    movUsuarioRepository.findByActivoAndUsuario(Estado.ACTIVO_NUMERICO.getNombre(), usuario)
        .ifPresent(movUsuario -> {
          usuarioDTO.setIdUsuario(movUsuario.getId());
          usuarioDTO.setUsuario(movUsuario.getUsuario());
          usuarioDTO.setClave(movUsuario.getClave());

          usuarioDTO.getPersona().setId(movUsuario.getPersona().getId());
          usuarioDTO.getPersona().setPrimerApellido(movUsuario.getPersona().getPrimerApellido());
          usuarioDTO.getPersona().setSegundoApellido(movUsuario.getPersona().getSegundoApellido());
          usuarioDTO.getPersona().setNombres(movUsuario.getPersona().getNombres());
          usuarioDTO.getPersona().setNumeroDocumento(movUsuario.getPersona().getNumeroDocumento());
          usuarioDTO.getPersona().setTelefono(movUsuario.getPersona().getTelefono());
          usuarioDTO.getPersona().setCorreo(movUsuario.getPersona().getCorreo());
          usuarioDTO.getPersona()
              .setIdTipoDocumento(movUsuario.getPersona().getTipoDocumento().getCodigo());
          usuarioDTO.getPersona()
              .setTipoDocumento(movUsuario.getPersona().getTipoDocumento().getAbreviatura());
          usuarioDTO.getPersona().setFechaNacimiento(
              ProjectUtils.convertDateToString(movUsuario.getPersona().getFechaNacimiento(),
                  ProjectConstants.Formato.FECHA_DD_MM_YYYY));
          usuarioDTO.getPersona().setSexo(movUsuario.getPersona().getSexo());
          usuarioDTO.getPersona().setActivo(movUsuario.getPersona().getActivo());

          movUsuario.getPerfils().forEach(perfilUsuario -> {
            if (perfilUsuario.getActivo().equalsIgnoreCase(Estado.ACTIVO_NUMERICO.getNombre())) {
              usuarioDTO.getPerfiles()
                  .add(new PerfilUsuario(perfilUsuario.getId(), perfilUsuario.getPerfil().getId(),
                      perfilUsuario.getPerfil().getNombre(), perfilUsuario.getPerfil().getRol()));
            }
          });
        });
    return usuarioDTO;
  }

  @Override
  public PerfilOpcions obtenerOpciones(String cuo, Integer idPerfil) throws Exception {
    PerfilOpcions perfilOpciones = new PerfilOpcions();
    maePerfilRepository.findById(idPerfil).ifPresent(maePerfil -> {
      perfilOpciones.setRol(maePerfil.getRol());
      maePerfil.getPerfilsOpcion().forEach(x -> {
        if (x.getActivo().equalsIgnoreCase(Estado.ACTIVO_NUMERICO.getNombre())) {
          MaeOpcion maeOpcion = x.getOpcion();
          Opcion opcion = new Opcion();
          opcion.setId(maeOpcion.getId());
          opcion.setCodigo(maeOpcion.getCodigo());
          opcion.setUrl(maeOpcion.getUrl());
          opcion.setIcono(maeOpcion.getIcono());
          opcion.setNombre(maeOpcion.getNombre());
          opcion.setOrden(maeOpcion.getOrden());
          opcion.setActivo(maeOpcion.getActivo());
          opcion.setIdOpcionSuperior(
              Objects.nonNull(maeOpcion.getOpcionSuperior()) ? maeOpcion.getOpcionSuperior().getId()
                  : null);
          opcion.setNombreOpcionSuperior(Objects.nonNull(maeOpcion.getOpcionSuperior())
              ? maeOpcion.getOpcionSuperior().getNombre()
              : null);
          perfilOpciones.getOpciones().add(opcion);
        }
      });
    });
    return perfilOpciones;
  }

}
